package br.com.moraesit.payment.service.infra.messaging.mapper;

import br.com.moraesit.commons.domain.events.payload.OrderPaymentEventPayload;
import br.com.moraesit.commons.domain.valueobject.PaymentOrderStatus;
import br.com.moraesit.payment.service.application.dto.PaymentRequest;
import debezium.order.payment_outbox.Value;

import java.time.Instant;

public class PaymentMessagingMapper {

    public static PaymentRequest paymentRequestAvroModelToPaymentRequest(OrderPaymentEventPayload payload, Value request) {
        return PaymentRequest.builder()
                .id(request.getId())
                .sagaId(request.getSagaId())
                .customerId(payload.getCustomerId())
                .orderId(payload.getOrderId())
                .price(payload.getPrice())
                .createdAt(Instant.parse(request.getCreatedAt()))
                .paymentOrderStatus(PaymentOrderStatus.valueOf(payload.getPaymentOrderStatus()))
                .build();
    }

}
