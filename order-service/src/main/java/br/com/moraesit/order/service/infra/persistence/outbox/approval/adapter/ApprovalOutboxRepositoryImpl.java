package br.com.moraesit.order.service.infra.persistence.outbox.approval.adapter;

import br.com.moraesit.commons.outbox.OutboxStatus;
import br.com.moraesit.commons.saga.SagaStatus;
import br.com.moraesit.order.service.application.outbox.model.approval.OrderApprovalOutboxMessage;
import br.com.moraesit.order.service.application.ports.output.repository.ApprovalOutboxRepository;
import br.com.moraesit.order.service.infra.persistence.outbox.approval.exception.ApprovalOutboxNotFoundException;
import br.com.moraesit.order.service.infra.persistence.outbox.approval.mapper.ApprovalOutboxPersistenceMapper;
import br.com.moraesit.order.service.infra.persistence.outbox.approval.repository.ApprovalOutboxJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ApprovalOutboxRepositoryImpl implements ApprovalOutboxRepository {

    private final ApprovalOutboxJpaRepository approvalOutboxJpaRepository;

    public ApprovalOutboxRepositoryImpl(ApprovalOutboxJpaRepository approvalOutboxJpaRepository) {
        this.approvalOutboxJpaRepository = approvalOutboxJpaRepository;
    }

    @Override
    public OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        return ApprovalOutboxPersistenceMapper
                .approvalOutboxEntityToOrderApprovalOutboxMessage(approvalOutboxJpaRepository
                        .save(ApprovalOutboxPersistenceMapper
                                .orderCreatedOutboxMessageToApprovalOutboxEntity(orderApprovalOutboxMessage)));
    }

    @Override
    public Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus, SagaStatus... sagaStatuses) {
        return Optional.of(approvalOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus, Arrays.asList(sagaStatuses))
                .orElseThrow(() -> new ApprovalOutboxNotFoundException("Approval outbox could not be found for saga type: " + type))
                .stream()
                .map(ApprovalOutboxPersistenceMapper::approvalOutboxEntityToOrderApprovalOutboxMessage)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type, UUID sagaId, SagaStatus... sagaStatuses) {
        return approvalOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(type, sagaId, Arrays.asList(sagaStatuses))
                .map(ApprovalOutboxPersistenceMapper::approvalOutboxEntityToOrderApprovalOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus, SagaStatus... sagaStatuses) {
        approvalOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus, Arrays.asList(sagaStatuses));
    }
}
