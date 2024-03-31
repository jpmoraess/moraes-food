package br.com.moraesit.order.service.application.outbox.scheduler.payment;

import br.com.moraesit.commons.outbox.OutboxStatus;
import br.com.moraesit.commons.saga.SagaStatus;
import br.com.moraesit.order.service.application.outbox.helper.payment.PaymentOutboxHelper;
import br.com.moraesit.order.service.application.outbox.model.payment.OrderPaymentOutboxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PaymentOutboxCleanerScheduler {

    private final PaymentOutboxHelper paymentOutboxHelper;

    public PaymentOutboxCleanerScheduler(PaymentOutboxHelper paymentOutboxHelper) {
        this.paymentOutboxHelper = paymentOutboxHelper;
    }

    //@Scheduled(cron = "@midnight")
    @Scheduled(cron = "*/180 * * * * *")
    public void processOutboxMessage() {
        Optional<List<OrderPaymentOutboxMessage>> paymentOutboxMessages = paymentOutboxHelper
                .getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
                        OutboxStatus.COMPLETED,
                        SagaStatus.SUCCEEDED,
                        SagaStatus.FAILED,
                        SagaStatus.COMPENSATED);
        if (paymentOutboxMessages.isPresent()) {
            List<OrderPaymentOutboxMessage> outboxMessages = paymentOutboxMessages.get();
            log.info("Received {} OrderPaymentOutboxMessage for clean-up. The payloads: {}",
                    outboxMessages.size(),
                    outboxMessages.stream()
                            .map(OrderPaymentOutboxMessage::getPayload)
                            .collect(Collectors.joining("\n")));
            paymentOutboxHelper.deletePaymentOutboxMessageByOutboxStatusAndSagaStatus(
                    OutboxStatus.COMPLETED,
                    SagaStatus.SUCCEEDED,
                    SagaStatus.FAILED,
                    SagaStatus.COMPENSATED);
            log.info("{} OrderPaymentOutboxMessage deleted!", outboxMessages.size());
        }
    }
}
