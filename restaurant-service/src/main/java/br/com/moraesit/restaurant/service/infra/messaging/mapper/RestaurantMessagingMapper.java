package br.com.moraesit.restaurant.service.infra.messaging.mapper;

import br.com.moraesit.commons.domain.events.payload.OrderApprovalEventPayload;
import br.com.moraesit.commons.domain.valueobject.ProductId;
import br.com.moraesit.commons.domain.valueobject.RestaurantOrderStatus;
import br.com.moraesit.restaurant.service.application.dto.RestaurantApprovalRequest;
import br.com.moraesit.restaurant.service.domain.entity.Product;
import debezium.order.restaurant_approval_outbox.Value;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

public class RestaurantMessagingMapper {

    public static RestaurantApprovalRequest restaurantApprovalRequestAvroModelToRestaurantApprovalRequest(
            OrderApprovalEventPayload orderApprovalEventPayload, Value restaurantApprovalRequestAvroModel) {
        return RestaurantApprovalRequest.builder()
                .id(restaurantApprovalRequestAvroModel.getId())
                .sagaId(restaurantApprovalRequestAvroModel.getSagaId())
                .restaurantId(orderApprovalEventPayload.getRestaurantId())
                .orderId(orderApprovalEventPayload.getOrderId())
                .restaurantOrderStatus(RestaurantOrderStatus.valueOf(orderApprovalEventPayload.getRestaurantOrderStatus()))
                .products(orderApprovalEventPayload.getProducts().stream().map(avroModel ->
                                Product.builder()
                                        .productId(new ProductId(UUID.fromString(avroModel.getId())))
                                        .quantity(avroModel.getQuantity())
                                        .build())
                        .collect(Collectors.toList()))
                .price(orderApprovalEventPayload.getPrice())
                .createdAt(Instant.parse(restaurantApprovalRequestAvroModel.getCreatedAt()))
                .build();
    }

}
