package br.com.moraesit.restaurant.service.application.outbox.helper;

import br.com.moraesit.commons.domain.valueobject.OrderApprovalStatus;
import br.com.moraesit.commons.outbox.OutboxStatus;
import br.com.moraesit.restaurant.service.application.outbox.model.OrderEventPayload;
import br.com.moraesit.restaurant.service.application.outbox.model.OrderOutboxMessage;
import br.com.moraesit.restaurant.service.application.ports.output.repository.OrderOutboxRepository;
import br.com.moraesit.restaurant.service.domain.exception.RestaurantDomainException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.moraesit.commons.domain.DomainConstants.UTC;
import static br.com.moraesit.commons.saga.SagaConstants.ORDER_SAGA_NAME;

@Slf4j
@Component
public class OrderOutboxHelper {

    private final ObjectMapper objectMapper;
    private final OrderOutboxRepository orderOutboxRepository;

    public OrderOutboxHelper(ObjectMapper objectMapper, OrderOutboxRepository orderOutboxRepository) {
        this.objectMapper = objectMapper;
        this.orderOutboxRepository = orderOutboxRepository;
    }

    @Transactional(readOnly = true)
    public Optional<OrderOutboxMessage> getCompletedOrderOutboxMessageBySagaIdAndOutboxStatus(UUID sagaId, OutboxStatus outboxStatus) {
        return orderOutboxRepository.findByTypeAndSagaIdAndOutboxStatus(ORDER_SAGA_NAME, sagaId, outboxStatus);
    }

    @Transactional(readOnly = true)
    public Optional<List<OrderOutboxMessage>> getOrderOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
        return orderOutboxRepository.findByTypeAndOutboxStatus(ORDER_SAGA_NAME, outboxStatus);
    }

    @Transactional
    public void deleteOrderOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
        orderOutboxRepository.deleteByTypeAndOutboxStatus(ORDER_SAGA_NAME, outboxStatus);
    }

    @Transactional
    public void saveOrderOutboxMessage(OrderEventPayload orderEventPayload, OrderApprovalStatus orderApprovalStatus, OutboxStatus outboxStatus, UUID sagaId) {
        save(OrderOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .createdAt(orderEventPayload.getCreatedAt())
                .processedAt(ZonedDateTime.now(ZoneId.of(UTC)))
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(orderEventPayload))
                .approvalStatus(orderApprovalStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    @Transactional
    public void updateOutboxStatus(OrderOutboxMessage orderOutboxMessage, OutboxStatus outboxStatus) {
        orderOutboxMessage.setOutboxStatus(outboxStatus);
        save(orderOutboxMessage);
        log.info("Order outbox table status is updated as: {}", outboxStatus.name());
    }

    private void save(OrderOutboxMessage orderOutboxMessage) {
        OrderOutboxMessage orderOutboxMessageResponse = orderOutboxRepository.save(orderOutboxMessage);
        if (orderOutboxMessageResponse == null) {
            throw new RestaurantDomainException("Could not save OrderOutboxMessage!");
        }
        log.info("OrderOutboxMessage saved with id: {}", orderOutboxMessage.getId().toString());
    }

    private String createPayload(OrderEventPayload orderEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderEventPayload);
        } catch (JsonProcessingException e) {
            log.error("Could not create OrderEventPayload json!", e);
            throw new RestaurantDomainException("Could not create OrderEventPayload json!", e);
        }
    }
}
