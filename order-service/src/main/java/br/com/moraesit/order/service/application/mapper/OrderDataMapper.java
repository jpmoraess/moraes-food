package br.com.moraesit.order.service.application.mapper;

import br.com.moraesit.commons.domain.events.payload.OrderApprovalEventPayload;
import br.com.moraesit.commons.domain.events.payload.OrderApprovalEventProduct;
import br.com.moraesit.commons.domain.events.payload.OrderPaymentEventPayload;
import br.com.moraesit.commons.domain.valueobject.*;
import br.com.moraesit.order.service.application.dto.create.CreateOrderInput;
import br.com.moraesit.order.service.application.dto.create.CreateOrderOutput;
import br.com.moraesit.order.service.application.dto.create.OrderAddressInput;
import br.com.moraesit.order.service.application.dto.create.OrderItemInput;
import br.com.moraesit.order.service.application.dto.track.TrackOrderOutput;
import br.com.moraesit.order.service.domain.entity.Order;
import br.com.moraesit.order.service.domain.entity.OrderItem;
import br.com.moraesit.order.service.domain.entity.Product;
import br.com.moraesit.order.service.domain.entity.Restaurant;
import br.com.moraesit.order.service.domain.event.OrderCancelledEvent;
import br.com.moraesit.order.service.domain.event.OrderCreatedEvent;
import br.com.moraesit.order.service.domain.event.OrderPaidEvent;
import br.com.moraesit.order.service.domain.valueobject.StreetAddress;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderDataMapper {

    public static Restaurant createOrderInputToRestaurant(CreateOrderInput input) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(input.getRestaurantId()))
                .products(orderItemInputListToProductList(input.getItems()))
                .build();
    }

    public static Order createOrderInputToOrder(CreateOrderInput input) {
        return Order.builder()
                .customerId(new CustomerId(input.getCustomerId()))
                .restaurantId(new RestaurantId(input.getRestaurantId()))
                .deliveryAddress(orderAddressInputListToStreetAddress(input.getAddress()))
                .price(new Money(input.getPrice()))
                .items(orderItemInputListToOrderItemList(input.getItems()))
                .build();
    }

    public static CreateOrderOutput orderToCreateOrderOutput(Order order, String message) {
        return new CreateOrderOutput(order.getTrackingId().getValue(), order.getOrderStatus(), message);
    }

    public static TrackOrderOutput orderToTrackOrderOutput(Order order) {
        return TrackOrderOutput.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }

    public static OrderPaymentEventPayload orderCreatedEventToOrderPaymentEventPayload(OrderCreatedEvent orderCreatedEvent) {
        return OrderPaymentEventPayload.builder()
                .customerId(orderCreatedEvent.getOrder().getCustomerId().getValue().toString())
                .orderId(orderCreatedEvent.getOrder().getId().getValue().toString())
                .price(orderCreatedEvent.getOrder().getPrice().getAmount())
                .createdAt(orderCreatedEvent.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.PENDING.name())
                .build();
    }

    public static OrderApprovalEventPayload orderPaidEventToOrderApprovalEventPayload(OrderPaidEvent orderPaidEvent) {
        return OrderApprovalEventPayload.builder()
                .orderId(orderPaidEvent.getOrder().getId().getValue().toString())
                .restaurantId(orderPaidEvent.getOrder().getRestaurantId().getValue().toString())
                .restaurantOrderStatus(RestaurantOrderStatus.PAID.name())
                .products(orderPaidEvent.getOrder().getItems().stream()
                        .map(orderItem -> OrderApprovalEventProduct.builder()
                                .id(orderItem.getProduct().getId().getValue().toString())
                                .quantity(orderItem.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .price(orderPaidEvent.getOrder().getPrice().getAmount())
                .createdAt(orderPaidEvent.getCreatedAt())
                .build();
    }

    public static OrderPaymentEventPayload orderCancelledEventToOrderPaymentEventPayload(OrderCancelledEvent orderCancelledEvent) {
        return OrderPaymentEventPayload.builder()
                .customerId(orderCancelledEvent.getOrder().getCustomerId().getValue().toString())
                .orderId(orderCancelledEvent.getOrder().getId().getValue().toString())
                .price(orderCancelledEvent.getOrder().getPrice().getAmount())
                .createdAt(orderCancelledEvent.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.CANCELLED.name())
                .build();
    }

    private static List<Product> orderItemInputListToProductList(List<OrderItemInput> orderItemInputList) {
        return orderItemInputList.stream()
                .map(orderItemInput -> new Product(new ProductId(orderItemInput.getProductId())))
                .collect(Collectors.toList());
    }

    private static StreetAddress orderAddressInputListToStreetAddress(OrderAddressInput input) {
        return new StreetAddress(UUID.randomUUID(), input.getStreet(), input.getPostalCode(), input.getCity());
    }

    private static List<OrderItem> orderItemInputListToOrderItemList(List<OrderItemInput> orderItemInputList) {
        return orderItemInputList.stream()
                .map(orderItemInput -> OrderItem.builder()
                        .product(new Product(new ProductId(orderItemInput.getProductId())))
                        .quantity(orderItemInput.getQuantity())
                        .price(new Money(orderItemInput.getPrice()))
                        .subTotal(new Money(orderItemInput.getSubTotal()))
                        .build())
                .collect(Collectors.toList());
    }
}
