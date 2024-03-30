package br.com.moraesit.restaurant.service.application.mapper;

import br.com.moraesit.commons.domain.valueobject.Money;
import br.com.moraesit.commons.domain.valueobject.OrderId;
import br.com.moraesit.commons.domain.valueobject.OrderStatus;
import br.com.moraesit.commons.domain.valueobject.RestaurantId;
import br.com.moraesit.restaurant.service.application.dto.RestaurantApprovalRequest;
import br.com.moraesit.restaurant.service.application.outbox.model.OrderEventPayload;
import br.com.moraesit.restaurant.service.domain.entity.OrderDetail;
import br.com.moraesit.restaurant.service.domain.entity.Product;
import br.com.moraesit.restaurant.service.domain.entity.Restaurant;
import br.com.moraesit.restaurant.service.domain.event.OrderApprovalEvent;

import java.util.UUID;
import java.util.stream.Collectors;

public class RestaurantDataMapper {

    public static Restaurant restaurantApprovalRequestToRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(UUID.fromString(restaurantApprovalRequest.getRestaurantId())))
                .orderDetail(OrderDetail.builder()
                        .orderId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())))
                        .products(restaurantApprovalRequest.getProducts().stream().map(
                                        product -> Product.builder()
                                                .productId(product.getId())
                                                .quantity(product.getQuantity())
                                                .build())
                                .collect(Collectors.toList()))
                        .totalAmount(new Money(restaurantApprovalRequest.getPrice()))
                        .orderStatus(OrderStatus.valueOf(restaurantApprovalRequest.getRestaurantOrderStatus().name()))
                        .build())
                .build();
    }

    public static OrderEventPayload orderApprovalEventToOrderEventPayload(OrderApprovalEvent orderApprovalEvent) {
        return OrderEventPayload.builder()
                .orderId(orderApprovalEvent.getOrderApproval().getOrderId().getValue().toString())
                .restaurantId(orderApprovalEvent.getOrderApproval().getRestaurantId().getValue().toString())
                .createdAt(orderApprovalEvent.getCreatedAt())
                .orderApprovalStatus(orderApprovalEvent.getOrderApproval().getApprovalStatus().name())
                .failureMessages(orderApprovalEvent.getFailureMessages())
                .build();
    }
}
