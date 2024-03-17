package br.com.moraesit.payment.service.infra.persistence.payment.adapter;

import br.com.moraesit.payment.service.application.ports.output.repository.PaymentRepository;
import br.com.moraesit.payment.service.domain.entity.Payment;
import br.com.moraesit.payment.service.infra.persistence.payment.mapper.PaymentPersistenceMapper;
import br.com.moraesit.payment.service.infra.persistence.payment.repository.PaymentJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    public PaymentRepositoryImpl(PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    public Payment save(Payment payment) {
        return PaymentPersistenceMapper
                .paymentEntityToPayment(paymentJpaRepository
                        .save(PaymentPersistenceMapper.paymentToPaymentEntity(payment)));
    }

    @Override
    public Optional<Payment> findByOrderId(UUID orderId) {
        return paymentJpaRepository.findByOrderId(orderId)
                .map(PaymentPersistenceMapper::paymentEntityToPayment);
    }
}
