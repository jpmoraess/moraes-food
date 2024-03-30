package br.com.moraesit.restaurant.service.infra.persistence.outbox.adapter;

import br.com.moraesit.commons.outbox.OutboxStatus;
import br.com.moraesit.restaurant.service.application.outbox.model.OrderOutboxMessage;
import br.com.moraesit.restaurant.service.application.ports.output.repository.OrderOutboxRepository;
import br.com.moraesit.restaurant.service.infra.persistence.outbox.exception.OrderOutboxNotFoundException;
import br.com.moraesit.restaurant.service.infra.persistence.outbox.mapper.OrderOutboxPersistenceMapper;
import br.com.moraesit.restaurant.service.infra.persistence.outbox.repository.OrderOutboxJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderOutboxRepositoryImpl implements OrderOutboxRepository {

    private final OrderOutboxJpaRepository orderOutboxJpaRepository;

    public OrderOutboxRepositoryImpl(OrderOutboxJpaRepository orderOutboxJpaRepository) {
        this.orderOutboxJpaRepository = orderOutboxJpaRepository;
    }

    @Override
    public OrderOutboxMessage save(OrderOutboxMessage orderOutboxMessage) {
        return OrderOutboxPersistenceMapper
                .orderOutboxEntityToOrderOutboxMessage(orderOutboxJpaRepository
                        .save(OrderOutboxPersistenceMapper
                                .orderOutboxMessageToOrderOutboxEntity(orderOutboxMessage)));
    }

    @Override
    public Optional<List<OrderOutboxMessage>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus) {
        return Optional.of(orderOutboxJpaRepository.findByTypeAndOutboxStatus(type, outboxStatus)
                .orElseThrow(() -> new OrderOutboxNotFoundException("Approval outbox object " +
                        "cannot be found for saga type " + type))
                .stream()
                .map(OrderOutboxPersistenceMapper::orderOutboxEntityToOrderOutboxMessage)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderOutboxMessage> findByTypeAndSagaIdAndOutboxStatus(String type, UUID sagaId, OutboxStatus outboxStatus) {
        return orderOutboxJpaRepository.findByTypeAndSagaIdAndOutboxStatus(type, sagaId, outboxStatus)
                .map(OrderOutboxPersistenceMapper::orderOutboxEntityToOrderOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus) {
        orderOutboxJpaRepository.deleteByTypeAndOutboxStatus(type, outboxStatus);
    }
}
