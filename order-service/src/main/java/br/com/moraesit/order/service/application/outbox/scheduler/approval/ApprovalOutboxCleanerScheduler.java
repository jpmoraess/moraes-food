package br.com.moraesit.order.service.application.outbox.scheduler.approval;

import br.com.moraesit.commons.outbox.OutboxStatus;
import br.com.moraesit.commons.saga.SagaStatus;
import br.com.moraesit.order.service.application.outbox.helper.approval.ApprovalOutboxHelper;
import br.com.moraesit.order.service.application.outbox.model.approval.OrderApprovalOutboxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ApprovalOutboxCleanerScheduler {

    private final ApprovalOutboxHelper approvalOutboxHelper;

    public ApprovalOutboxCleanerScheduler(ApprovalOutboxHelper approvalOutboxHelper) {
        this.approvalOutboxHelper = approvalOutboxHelper;
    }

    //@Scheduled(cron = "@midnight")
    @Scheduled(cron = "*/180 * * * * *")
    public void processOutboxMessage() {
        Optional<List<OrderApprovalOutboxMessage>> orderApprovalOutboxMessages = approvalOutboxHelper
                .getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                        OutboxStatus.COMPLETED,
                        SagaStatus.SUCCEEDED,
                        SagaStatus.FAILED,
                        SagaStatus.COMPENSATED);
        if (orderApprovalOutboxMessages.isPresent()) {
            List<OrderApprovalOutboxMessage> outboxMessages = orderApprovalOutboxMessages.get();
            log.info("Received {} OrderApprovalOutboxMessage for clean-up. The payloads: {}",
                    outboxMessages.size(),
                    outboxMessages.stream()
                            .map(OrderApprovalOutboxMessage::getPayload)
                            .collect(Collectors.joining("\n")));
            approvalOutboxHelper.deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                    OutboxStatus.COMPLETED,
                    SagaStatus.SUCCEEDED,
                    SagaStatus.FAILED,
                    SagaStatus.COMPENSATED);
            log.info("{} OrderApprovalOutboxMessage deleted!", outboxMessages.size());
        }
    }
}
