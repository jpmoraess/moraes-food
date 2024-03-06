package br.com.moraesit.order.service.infra.persistence.order.mapper;

import br.com.moraesit.commons.domain.valueobject.CustomerId;
import br.com.moraesit.commons.domain.valueobject.Money;
import br.com.moraesit.commons.domain.valueobject.ProductId;
import br.com.moraesit.commons.domain.valueobject.RestaurantId;
import br.com.moraesit.order.service.domain.entity.Order;
import br.com.moraesit.order.service.domain.entity.OrderItem;
import br.com.moraesit.order.service.domain.entity.Product;
import br.com.moraesit.order.service.domain.valueobject.OrderItemId;
import br.com.moraesit.order.service.domain.valueobject.StreetAddress;
import br.com.moraesit.order.service.domain.valueobject.TrackingId;
import br.com.moraesit.order.service.infra.persistence.order.entity.OrderAddressEntity;
import br.com.moraesit.order.service.infra.persistence.order.entity.OrderEntity;
import br.com.moraesit.order.service.infra.persistence.order.entity.OrderItemEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderPersistenceMapper {

    public static OrderEntity orderToOrderEntity(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().getValue())
                .customerId(order.getCustomerId().getValue())
                .restaurantId(order.getRestaurantId().getValue())
                .trackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(formatFailureMessages(order))
                .address(deliveryAddressToOrderAddressEntity(order.getDeliveryAddress()))
                .price(order.getPrice().getAmount())
                .items(orderItemsToOrderItemsEntity(order.getItems()))
                .build();
        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));
        return orderEntity;
    }

    public static Order orderEntityToOrder(OrderEntity orderEntity) {
        return Order.builder()
                .customerId(new CustomerId(orderEntity.getCustomerId()))
                .restaurantId(new RestaurantId(orderEntity.getRestaurantId()))
                .deliveryAddress(orderAddressEntityToDeliveryAddress(orderEntity.getAddress()))
                .price(new Money(orderEntity.getPrice()))
                .items(orderItemsEntityToOrderItems(orderEntity.getItems()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .orderStatus(orderEntity.getOrderStatus())
                .failureMessages(formatFailureMessages(orderEntity))
                .build();
    }

    private static OrderAddressEntity deliveryAddressToOrderAddressEntity(StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
                .id(deliveryAddress.getId())
                .street(deliveryAddress.getStreet())
                .postalCode(deliveryAddress.getPostalCode())
                .city(deliveryAddress.getCity())
                .build();
    }

    private static StreetAddress orderAddressEntityToDeliveryAddress(OrderAddressEntity orderAddressEntity) {
        return StreetAddress.builder()
                .id(orderAddressEntity.getId())
                .street(orderAddressEntity.getStreet())
                .postalCode(orderAddressEntity.getPostalCode())
                .city(orderAddressEntity.getCity())
                .build();
    }

    private static List<OrderItemEntity> orderItemsToOrderItemsEntity(List<OrderItem> items) {
        return items.stream()
                .map(orderItem -> OrderItemEntity.builder()
                        .id(orderItem.getId().getValue())
                        .productId(orderItem.getProduct().getId().getValue())
                        .price(orderItem.getPrice().getAmount())
                        .quantity(orderItem.getQuantity())
                        .subTotal(orderItem.getSubTotal().getAmount())
                        .build())
                .collect(Collectors.toList());
    }

    private static List<OrderItem> orderItemsEntityToOrderItems(List<OrderItemEntity> items) {
        return items.stream()
                .map(orderItemEntity -> OrderItem.builder()
                        .orderItemId(new OrderItemId(orderItemEntity.getId()))
                        .product(new Product(new ProductId(orderItemEntity.getProductId())))
                        .price(new Money(orderItemEntity.getPrice()))
                        .quantity(orderItemEntity.getQuantity())
                        .subTotal(new Money(orderItemEntity.getSubTotal()))
                        .build())
                .collect(Collectors.toList());
    }

    private static String formatFailureMessages(Order order) {
        return order.getFailureMessages() != null ? String.join(",", order.getFailureMessages()) : "";
    }

    private static List<String> formatFailureMessages(OrderEntity orderEntity) {
        return (orderEntity.getFailureMessages() != null && !orderEntity.getFailureMessages().isEmpty())
                ? new ArrayList<>(Arrays.asList(orderEntity.getFailureMessages().split(",")))
                : new ArrayList<>();
    }
}
