package br.com.moraesit.payment.service.application;

import br.com.moraesit.commons.domain.valueobject.CustomerId;
import br.com.moraesit.payment.service.application.dto.PaymentRequest;
import br.com.moraesit.payment.service.application.exception.PaymentApplicationServiceException;
import br.com.moraesit.payment.service.application.mapper.PaymentDataMapper;
import br.com.moraesit.payment.service.application.ports.output.repository.CreditEntryRepository;
import br.com.moraesit.payment.service.application.ports.output.repository.CreditHistoryRepository;
import br.com.moraesit.payment.service.application.ports.output.repository.PaymentRepository;
import br.com.moraesit.payment.service.domain.PaymentDomainService;
import br.com.moraesit.payment.service.domain.entity.CreditEntry;
import br.com.moraesit.payment.service.domain.entity.CreditHistory;
import br.com.moraesit.payment.service.domain.entity.Payment;
import br.com.moraesit.payment.service.domain.event.PaymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentRequestHelper {

    private static final Logger logger = LoggerFactory.getLogger(PaymentRequestHelper.class);

    private final PaymentDomainService paymentDomainService;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;

    public PaymentRequestHelper(PaymentDomainService paymentDomainService,
                                PaymentRepository paymentRepository,
                                CreditEntryRepository creditEntryRepository,
                                CreditHistoryRepository creditHistoryRepository) {
        this.paymentDomainService = paymentDomainService;
        this.paymentRepository = paymentRepository;
        this.creditEntryRepository = creditEntryRepository;
        this.creditHistoryRepository = creditHistoryRepository;
    }

    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        logger.info("Received payment complete event for order id: {}", paymentRequest.getOrderId());
        Payment payment = PaymentDataMapper.paymentRequestToPayment(paymentRequest);
        return processPayment(payment, paymentDomainService::validateAndInitiatePayment);
    }

    @Transactional
    public PaymentEvent persistCancelPayment(PaymentRequest paymentRequest) {
        logger.info("Received payment rollback event for order id: {}", paymentRequest.getOrderId());
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId()));
        if (paymentOptional.isEmpty()) {
            logger.error("Payment with order id: {} could not be found!", paymentRequest.getOrderId());
            throw new PaymentApplicationServiceException("Payment with order id: " + paymentRequest.getOrderId() +
                    " could not be found!");
        }
        Payment payment = paymentOptional.get();
        return processPayment(payment, paymentDomainService::validateAndCancelPayment);
    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId.getValue());
        if (creditEntry.isEmpty()) {
            logger.error("Could not find credit entry for customer: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Could not find credit entry for customer: " + customerId.getValue());
        }
        return creditEntry.get();
    }

    private List<CreditHistory> getCreditHistory(CustomerId customerId) {
        Optional<List<CreditHistory>> creditHistory = creditHistoryRepository.findByCustomerId(customerId.getValue());
        if (creditHistory.isEmpty()) {
            logger.error("Could not find credit history for customer: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Could not find credit history for customer: " + customerId.getValue());
        }
        return creditHistory.get();
    }

    private void persistDbObjects(Payment payment, CreditEntry creditEntry,
                                  List<CreditHistory> creditHistoryList, List<String> failureMessages) {
        paymentRepository.save(payment);
        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistoryList.getLast());
        }
    }

    private PaymentEvent processPayment(Payment payment, PaymentOperation paymentOperation) {
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistoryList = getCreditHistory(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        persistDbObjects(payment, creditEntry, creditHistoryList, failureMessages);
        return paymentOperation.execute(payment, creditEntry, creditHistoryList, failureMessages);
    }
}
