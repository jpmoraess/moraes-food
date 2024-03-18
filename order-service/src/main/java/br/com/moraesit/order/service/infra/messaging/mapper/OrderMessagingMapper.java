package br.com.moraesit.order.service.infra.messaging.mapper;

import br.com.moraesit.commons.domain.events.payload.PaymentOrderEventPayload;
import br.com.moraesit.commons.domain.valueobject.PaymentStatus;
import br.com.moraesit.order.service.application.dto.message.PaymentResponse;
import debezium.payment.order_outbox.Value;

import java.time.Instant;

public class OrderMessagingMapper {

    public static PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentOrderEventPayload paymentOrderEventPayload, Value paymentResponseAvroModel) {
        return PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId())
                .sagaId(paymentResponseAvroModel.getSagaId())
                .paymentId(paymentOrderEventPayload.getPaymentId())
                .customerId(paymentOrderEventPayload.getCustomerId())
                .orderId(paymentOrderEventPayload.getOrderId())
                .price(paymentOrderEventPayload.getPrice())
                .createdAt(Instant.parse(paymentResponseAvroModel.getCreatedAt()))
                .paymentStatus(PaymentStatus.valueOf(paymentOrderEventPayload.getPaymentStatus()))
                .failureMessages(paymentOrderEventPayload.getFailureMessages())
                .build();
    }
}
