package br.com.moraesit.order.service.infra.persistence.outbox.payment.adapter;

import br.com.moraesit.commons.outbox.OutboxStatus;
import br.com.moraesit.commons.saga.SagaStatus;
import br.com.moraesit.order.service.application.outbox.model.payment.OrderPaymentOutboxMessage;
import br.com.moraesit.order.service.application.ports.output.repository.PaymentOutboxRepository;
import br.com.moraesit.order.service.infra.persistence.outbox.payment.exception.PaymentOutboxNotFoundException;
import br.com.moraesit.order.service.infra.persistence.outbox.payment.mapper.PaymentOutboxPersistenceMapper;
import br.com.moraesit.order.service.infra.persistence.outbox.payment.repository.PaymentOutboxJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PaymentOutboxRepositoryImpl implements PaymentOutboxRepository {

    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    public PaymentOutboxRepositoryImpl(PaymentOutboxJpaRepository paymentOutboxJpaRepository) {
        this.paymentOutboxJpaRepository = paymentOutboxJpaRepository;
    }

    @Override
    public OrderPaymentOutboxMessage save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        return PaymentOutboxPersistenceMapper
                .paymentOutboxEntityToOrderPaymentOutboxMessage(paymentOutboxJpaRepository
                        .save(PaymentOutboxPersistenceMapper
                                .orderPaymentOutboxMessageToPaymentOutboxEntity(orderPaymentOutboxMessage)));
    }

    @Override
    public Optional<List<OrderPaymentOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus, SagaStatus... sagaStatuses) {
        return Optional.of(paymentOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus, Arrays.asList(sagaStatuses))
                .orElseThrow(() -> new PaymentOutboxNotFoundException("Payment outbox could not be found for saga type: " + type))
                .stream()
                .map(PaymentOutboxPersistenceMapper::paymentOutboxEntityToOrderPaymentOutboxMessage)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderPaymentOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type, UUID sagaId, SagaStatus... sagaStatuses) {
        return paymentOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(type, sagaId, Arrays.asList(sagaStatuses))
                .map(PaymentOutboxPersistenceMapper::paymentOutboxEntityToOrderPaymentOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus, SagaStatus... sagaStatuses) {
        paymentOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus, Arrays.asList(sagaStatuses));
    }
}
