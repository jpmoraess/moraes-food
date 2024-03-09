package br.com.moraesit.payment.service.application.mapper;

import br.com.moraesit.commons.domain.valueobject.CustomerId;
import br.com.moraesit.commons.domain.valueobject.Money;
import br.com.moraesit.commons.domain.valueobject.OrderId;
import br.com.moraesit.payment.service.application.dto.PaymentRequest;
import br.com.moraesit.payment.service.domain.entity.Payment;

import java.util.UUID;

public class PaymentDataMapper {

    public static Payment paymentRequestToPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
                .customerId(new CustomerId(UUID.fromString(paymentRequest.getCustomerId())))
                .price(new Money(paymentRequest.getPrice()))
                .build();
    }
}
