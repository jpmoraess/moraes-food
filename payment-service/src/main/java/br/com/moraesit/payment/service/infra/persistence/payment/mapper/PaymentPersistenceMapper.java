package br.com.moraesit.payment.service.infra.persistence.payment.mapper;

import br.com.moraesit.commons.domain.valueobject.CustomerId;
import br.com.moraesit.commons.domain.valueobject.Money;
import br.com.moraesit.commons.domain.valueobject.OrderId;
import br.com.moraesit.payment.service.domain.entity.Payment;
import br.com.moraesit.payment.service.domain.valueobject.PaymentId;
import br.com.moraesit.payment.service.infra.persistence.payment.entity.PaymentEntity;

public class PaymentPersistenceMapper {

    public static PaymentEntity paymentToPaymentEntity(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId().getValue())
                .customerId(payment.getCustomerId().getValue())
                .orderId(payment.getOrderId().getValue())
                .price(payment.getPrice().getAmount())
                .status(payment.getPaymentStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public static Payment paymentEntityToPayment(PaymentEntity paymentEntity) {
        return Payment.builder()
                .paymentId(new PaymentId(paymentEntity.getId()))
                .customerId(new CustomerId(paymentEntity.getCustomerId()))
                .orderId(new OrderId(paymentEntity.getOrderId()))
                .price(new Money(paymentEntity.getPrice()))
                .createdAt(paymentEntity.getCreatedAt())
                .build();
    }
}
