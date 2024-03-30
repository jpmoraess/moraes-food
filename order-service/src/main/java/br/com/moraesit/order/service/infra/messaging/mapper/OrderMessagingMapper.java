package br.com.moraesit.order.service.infra.messaging.mapper;

import br.com.moraesit.commons.domain.events.payload.PaymentOrderEventPayload;
import br.com.moraesit.commons.domain.events.payload.RestaurantOrderEventPayload;
import br.com.moraesit.commons.domain.valueobject.OrderApprovalStatus;
import br.com.moraesit.commons.domain.valueobject.PaymentStatus;
import br.com.moraesit.order.service.application.dto.message.PaymentResponse;
import br.com.moraesit.order.service.application.dto.message.RestaurantApprovalResponse;

import java.time.Instant;

public class OrderMessagingMapper {

    public static PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentOrderEventPayload paymentOrderEventPayload,
                                                                            debezium.payment.order_outbox.Value paymentResponseAvroModel) {
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

    public static RestaurantApprovalResponse approvalResponseAvroModelToApprovalResponse(RestaurantOrderEventPayload restaurantOrderEventPayload,
                                                                                         debezium.restaurant.order_outbox.Value restaurantApprovalResponseAvroModel) {
        return RestaurantApprovalResponse.builder()
                .id(restaurantApprovalResponseAvroModel.getId())
                .sagaId(restaurantApprovalResponseAvroModel.getSagaId())
                .restaurantId(restaurantOrderEventPayload.getRestaurantId())
                .orderId(restaurantOrderEventPayload.getOrderId())
                .createdAt(Instant.parse(restaurantApprovalResponseAvroModel.getCreatedAt()))
                .orderApprovalStatus(OrderApprovalStatus.valueOf(restaurantOrderEventPayload.getOrderApprovalStatus()))
                .failureMessages(restaurantOrderEventPayload.getFailureMessages())
                .build();
    }
}
