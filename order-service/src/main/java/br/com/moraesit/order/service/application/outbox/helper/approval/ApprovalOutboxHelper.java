package br.com.moraesit.order.service.application.outbox.helper.approval;

import br.com.moraesit.commons.domain.events.payload.OrderApprovalEventPayload;
import br.com.moraesit.commons.domain.valueobject.OrderStatus;
import br.com.moraesit.commons.outbox.OutboxStatus;
import br.com.moraesit.commons.saga.SagaStatus;
import br.com.moraesit.order.service.application.outbox.model.approval.OrderApprovalOutboxMessage;
import br.com.moraesit.order.service.application.ports.output.repository.ApprovalOutboxRepository;
import br.com.moraesit.order.service.domain.exception.OrderDomainException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.moraesit.commons.saga.SagaConstants.ORDER_SAGA_NAME;

@Component
public class ApprovalOutboxHelper {
    private final Logger logger = LoggerFactory.getLogger(ApprovalOutboxHelper.class);

    private final ObjectMapper objectMapper;
    private final ApprovalOutboxRepository approvalOutboxRepository;

    public ApprovalOutboxHelper(ObjectMapper objectMapper, ApprovalOutboxRepository approvalOutboxRepository) {
        this.objectMapper = objectMapper;
        this.approvalOutboxRepository = approvalOutboxRepository;
    }

    @Transactional(readOnly = true)
    public Optional<List<OrderApprovalOutboxMessage>> getApprovalOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        return approvalOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME, outboxStatus, sagaStatus);
    }

    @Transactional(readOnly = true)
    public Optional<OrderApprovalOutboxMessage> getApprovalOutboxMessageBySagaIdAndSagaStatus(UUID sagaId, SagaStatus... sagaStatus) {
        return approvalOutboxRepository.findByTypeAndSagaIdAndSagaStatus(ORDER_SAGA_NAME, sagaId, sagaStatus);
    }

    @Transactional
    public void save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        OrderApprovalOutboxMessage response = approvalOutboxRepository.save(orderApprovalOutboxMessage);
        if (response == null) {
            logger.error("Could not save OrderApprovalOutboxMessage with outbox id: {}", orderApprovalOutboxMessage.getId());
            throw new OrderDomainException("Could not save OrderApprovalOutboxMessage with outbox id: " + orderApprovalOutboxMessage.getId());
        }
        logger.info("OrderApprovalOutboxMessage saved with outbox id: {}", response.getId());
    }

    @Transactional
    public void saveApprovalOutboxMessage(OrderApprovalEventPayload orderApprovalEventPayload, OrderStatus orderStatus, SagaStatus sagaStatus, OutboxStatus outboxStatus, UUID sagaId) {
        save(OrderApprovalOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .createdAt(orderApprovalEventPayload.getCreatedAt())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(orderApprovalEventPayload))
                .orderStatus(orderStatus)
                .sagaStatus(sagaStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    @Transactional
    public void deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        approvalOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME, outboxStatus, sagaStatus);
    }

    private String createPayload(OrderApprovalEventPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            logger.error("Could not create OrderApprovalEventPayload for order id: {}", payload.getOrderId(), e);
            throw new OrderDomainException("Could not create OrderApprovalEventPayload for order id: " + payload.getOrderId(), e);
        }
    }
}
