package br.com.moraesit.payment.service.application.ports.output.repository;

import br.com.moraesit.commons.domain.valueobject.PaymentStatus;
import br.com.moraesit.commons.outbox.OutboxStatus;
import br.com.moraesit.payment.service.application.outbox.model.OrderOutboxMessage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderOutboxRepository {

    OrderOutboxMessage save(OrderOutboxMessage orderOutboxMessage);

    Optional<List<OrderOutboxMessage>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

    Optional<OrderOutboxMessage> findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(String type, UUID sagaId, PaymentStatus paymentStatus, OutboxStatus outboxStatus);

    void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);
}
