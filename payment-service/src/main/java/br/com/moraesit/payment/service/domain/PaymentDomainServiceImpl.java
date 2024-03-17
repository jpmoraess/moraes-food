package br.com.moraesit.payment.service.domain;

import br.com.moraesit.commons.domain.valueobject.Money;
import br.com.moraesit.commons.domain.valueobject.PaymentStatus;
import br.com.moraesit.payment.service.domain.entity.CreditEntry;
import br.com.moraesit.payment.service.domain.entity.CreditHistory;
import br.com.moraesit.payment.service.domain.entity.Payment;
import br.com.moraesit.payment.service.domain.event.PaymentCancelledEvent;
import br.com.moraesit.payment.service.domain.event.PaymentCompletedEvent;
import br.com.moraesit.payment.service.domain.event.PaymentEvent;
import br.com.moraesit.payment.service.domain.event.PaymentFailedEvent;
import br.com.moraesit.payment.service.domain.valueobject.CreditHistoryId;
import br.com.moraesit.payment.service.domain.valueobject.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static br.com.moraesit.commons.domain.DomainConstants.UTC;

@Service // Tirar Daqui
public class PaymentDomainServiceImpl implements PaymentDomainService {

    private final Logger logger = LoggerFactory.getLogger(PaymentDomainServiceImpl.class);

    @Override
    public PaymentEvent validateAndInitiatePayment(Payment payment, CreditEntry creditEntry,
                                                   List<CreditHistory> creditHistoryList, List<String> failureMessages) {
        payment.validatePayment(failureMessages);
        payment.initializePayment();
        validateCreditEntry(payment, creditEntry, failureMessages);
        subtractCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistoryList, TransactionType.DEBIT);
        validateCreditHistory(creditEntry, creditHistoryList, failureMessages);

        if (failureMessages.isEmpty()) {
            logger.info("Payment is initiated for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.COMPLETED);
            return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)));
        } else {
            logger.info("Payment initiation is failed for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), failureMessages);
        }
    }

    @Override
    public PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
                                                 List<CreditHistory> creditHistoryList, List<String> failureMessages) {
        payment.validatePayment(failureMessages);
        addCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistoryList, TransactionType.CREDIT);

        if (failureMessages.isEmpty()) {
            logger.info("Payment is cancelled for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.CANCELLED);
            return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)));
        } else {
            logger.info("Payment cancellation is failed for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), failureMessages);
        }
    }

    private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
        if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
            failureMessages.add("Customer with id: " + payment.getCustomerId().getValue() +
                    " doesn't have enough credit for payment!");
            logger.error("Customer with id: {} doesn't have enough credit for payment!",
                    payment.getCustomerId().getValue());
        }
    }

    private void addCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.addCreditAmount(payment.getPrice());
    }

    private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.subtractCreditAmount(payment.getPrice());
    }

    private void updateCreditHistory(Payment payment, List<CreditHistory> creditHistoryList, TransactionType transactionType) {
        creditHistoryList.add(CreditHistory.builder()
                .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
                .customerId(payment.getCustomerId())
                .amount(payment.getPrice())
                .transactionType(transactionType)
                .build());
    }

    private void validateCreditHistory(CreditEntry creditEntry, List<CreditHistory> creditHistoryList, List<String> failureMessages) {
        Money totalDebitHistory = getTotalHistoryAmount(creditHistoryList, TransactionType.DEBIT);
        Money totalCreditHistory = getTotalHistoryAmount(creditHistoryList, TransactionType.CREDIT);

        if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
            failureMessages.add("Customer with id: " + creditEntry.getCustomerId().getValue() +
                    " doesn't have enough credit according to credit history!");
            logger.error("Customer with id: {} doesn't have enough credit according to credit history!",
                    creditEntry.getCustomerId().getValue());
        }

        if (!creditEntry.getTotalCreditAmount().equals(totalCreditHistory.subtract(totalDebitHistory))) {
            failureMessages.add("Credit history total is not equal to current credit for customer id: " +
                    creditEntry.getCustomerId().getValue());
            logger.error("Credit history total is not equal to current credit for customer id: {}!",
                    creditEntry.getCustomerId().getValue());
        }
    }

    private Money getTotalHistoryAmount(List<CreditHistory> creditHistoryList, TransactionType transactionType) {
        return creditHistoryList.stream()
                .filter(creditHistory -> transactionType.equals(creditHistory.getTransactionType()))
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);
    }
}
