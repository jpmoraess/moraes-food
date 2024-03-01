package br.com.moraesit.order.service.application.mapper;

import br.com.moraesit.commons.domain.valueobject.CustomerId;
import br.com.moraesit.commons.domain.valueobject.Money;
import br.com.moraesit.commons.domain.valueobject.ProductId;
import br.com.moraesit.commons.domain.valueobject.RestaurantId;
import br.com.moraesit.order.service.application.dto.create.CreateOrderInput;
import br.com.moraesit.order.service.application.dto.create.CreateOrderOutput;
import br.com.moraesit.order.service.application.dto.create.OrderAddressInput;
import br.com.moraesit.order.service.application.dto.create.OrderItemInput;
import br.com.moraesit.order.service.domain.entity.Order;
import br.com.moraesit.order.service.domain.entity.OrderItem;
import br.com.moraesit.order.service.domain.entity.Product;
import br.com.moraesit.order.service.domain.entity.Restaurant;
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
                .deliveryAddress(orderAddressInputListToStreetAddress(input.getOrderAddress()))
                .price(new Money(input.getPrice()))
                .items(orderItemInputListToOrderItemList(input.getItems()))
                .build();
    }

    public static CreateOrderOutput orderToCreateOrderOutput(Order order) {
        return new CreateOrderOutput(order.getTrackingId().getValue(), order.getOrderStatus());
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
