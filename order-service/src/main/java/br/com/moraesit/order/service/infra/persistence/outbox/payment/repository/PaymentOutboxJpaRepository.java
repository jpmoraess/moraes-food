package br.com.moraesit.order.service.infra.persistence.outbox.payment.repository;

import br.com.moraesit.commons.outbox.OutboxStatus;
import br.com.moraesit.commons.saga.SagaStatus;
import br.com.moraesit.order.service.infra.persistence.outbox.payment.entity.PaymentOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentOutboxJpaRepository extends JpaRepository<PaymentOutboxEntity, UUID> {

    Optional<List<PaymentOutboxEntity>> findByTypeAndOutboxStatusAndSagaStatusIn(String type, OutboxStatus outboxStatus, List<SagaStatus> sagaStatus);

    Optional<PaymentOutboxEntity> findByTypeAndSagaIdAndSagaStatusIn(String type, UUID sagaId, List<SagaStatus> sagaStatus);

    void deleteByTypeAndOutboxStatusAndSagaStatusIn(String type, OutboxStatus outboxStatus, List<SagaStatus> sagaStatus);
}
