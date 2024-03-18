package br.com.moraesit.payment.service.application;

import br.com.moraesit.commons.domain.valueobject.CustomerId;
import br.com.moraesit.commons.domain.valueobject.PaymentStatus;
import br.com.moraesit.commons.outbox.OutboxStatus;
import br.com.moraesit.payment.service.application.dto.PaymentRequest;
import br.com.moraesit.payment.service.application.exception.PaymentApplicationServiceException;
import br.com.moraesit.payment.service.application.mapper.PaymentDataMapper;
import br.com.moraesit.payment.service.application.outbox.helper.OrderOutboxHelper;
import br.com.moraesit.payment.service.application.outbox.model.OrderOutboxMessage;
import br.com.moraesit.payment.service.application.ports.output.repository.CreditEntryRepository;
import br.com.moraesit.payment.service.application.ports.output.repository.CreditHistoryRepository;
import br.com.moraesit.payment.service.application.ports.output.repository.PaymentRepository;
import br.com.moraesit.payment.service.domain.PaymentDomainService;
import br.com.moraesit.payment.service.domain.entity.CreditEntry;
import br.com.moraesit.payment.service.domain.entity.CreditHistory;
import br.com.moraesit.payment.service.domain.entity.Payment;
import br.com.moraesit.payment.service.domain.event.PaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class PaymentRequestHelper {

    private final PaymentDomainService paymentDomainService;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final OrderOutboxHelper orderOutboxHelper;

    public PaymentRequestHelper(PaymentDomainService paymentDomainService,
                                PaymentRepository paymentRepository,
                                CreditEntryRepository creditEntryRepository,
                                CreditHistoryRepository creditHistoryRepository, OrderOutboxHelper orderOutboxHelper) {
        this.paymentDomainService = paymentDomainService;
        this.paymentRepository = paymentRepository;
        this.creditEntryRepository = creditEntryRepository;
        this.creditHistoryRepository = creditHistoryRepository;
        this.orderOutboxHelper = orderOutboxHelper;
    }

    @Transactional
    public void persistPayment(PaymentRequest paymentRequest) {
        if (isOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.COMPLETED)) {
            log.info("An outbox message with saga id: {} is already saved to database!", paymentRequest.getSagaId());
            return;
        }
        log.info("Received payment complete event for order id: {}", paymentRequest.getOrderId());
        Payment payment = PaymentDataMapper.paymentRequestToPayment(paymentRequest);
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent =
                paymentDomainService.validateAndInitiatePayment(payment, creditEntry, creditHistories, failureMessages);
        persistDbObjects(payment, creditEntry, creditHistories, failureMessages);

        orderOutboxHelper.saveOrderOutboxMessage(
                PaymentDataMapper.paymentEventToOrderEventPayload(paymentEvent),
                paymentEvent.getPayment().getPaymentStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(paymentRequest.getSagaId())
        );
    }

    @Transactional
    public void persistCancelPayment(PaymentRequest paymentRequest) {
        log.info("Received payment rollback event for order id: {}", paymentRequest.getOrderId());
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId()));
        if (paymentOptional.isEmpty()) {
            log.error("Payment with order id: {} could not be found!", paymentRequest.getOrderId());
            throw new PaymentApplicationServiceException("Payment with order id: " + paymentRequest.getOrderId() +
                    " could not be found!");
        }
        Payment payment = paymentOptional.get();
        processPayment(payment, paymentDomainService::validateAndCancelPayment);
    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId.getValue());
        if (creditEntry.isEmpty()) {
            log.error("Could not find credit entry for customer: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Could not find credit entry for customer: " + customerId.getValue());
        }
        return creditEntry.get();
    }

    private List<CreditHistory> getCreditHistory(CustomerId customerId) {
        Optional<List<CreditHistory>> creditHistory = creditHistoryRepository.findByCustomerId(customerId.getValue());
        if (creditHistory.isEmpty()) {
            log.error("Could not find credit history for customer: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Could not find credit history for customer: " + customerId.getValue());
        }
        return creditHistory.get();
    }

    private void persistDbObjects(Payment payment, CreditEntry creditEntry,
                                  List<CreditHistory> creditHistoryList, List<String> failureMessages) {
        paymentRepository.save(payment);
        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistoryList.get(creditHistoryList.size() - 1));
        }
    }

    private PaymentEvent processPayment(Payment payment, PaymentOperation paymentOperation) {
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistoryList = getCreditHistory(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        persistDbObjects(payment, creditEntry, creditHistoryList, failureMessages);
        return paymentOperation.execute(payment, creditEntry, creditHistoryList, failureMessages);
    }

    private boolean isOutboxMessageProcessedForPayment(PaymentRequest paymentRequest, PaymentStatus paymentStatus) {
        Optional<OrderOutboxMessage> orderOutboxMessage = orderOutboxHelper
                .getCompletedOrderOutboxMessageBySagaIdAndPaymentStatus(UUID.fromString(paymentRequest.getSagaId()), paymentStatus);
        return orderOutboxMessage.isPresent();
    }
}
