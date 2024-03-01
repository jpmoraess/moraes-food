package br.com.moraesit.order.service.domain;

import br.com.moraesit.commons.domain.valueobject.ProductId;
import br.com.moraesit.order.service.domain.entity.Order;
import br.com.moraesit.order.service.domain.entity.Product;
import br.com.moraesit.order.service.domain.entity.Restaurant;
import br.com.moraesit.order.service.domain.event.OrderCancelledEvent;
import br.com.moraesit.order.service.domain.event.OrderCreatedEvent;
import br.com.moraesit.order.service.domain.event.OrderPaidEvent;
import br.com.moraesit.order.service.domain.exception.OrderDomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDomainServiceImpl implements OrderDomainService {
    private static final String UTC = "UTC";
    private final Logger logger = LoggerFactory.getLogger(OrderDomainServiceImpl.class);

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        logger.info("Order with id: {} is initiated!", order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        logger.info("Order with id: {} is paid!", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        logger.info("Order with id: {} is approved!", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        logger.info("Order payment is cancelling for order  id: {}", order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        logger.info("Order with id: {} is cancelled!", order.getId().getValue());
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id: " + restaurant.getId().getValue() +
                    " is currently not active!");
        }
    }

    // TODO: atualmente n^2, utilizar HashMap para 0(n)
    private void setOrderProductInformationOld(Order order, Restaurant restaurant) {
        order.getItems().forEach(orderItem -> restaurant.getProducts().forEach(restaurantProduct -> {
            Product currentProduct = orderItem.getProduct();
            if (currentProduct.equals(restaurantProduct)) {
                currentProduct.updatedWithConfirmedNameAndPrice(
                        restaurantProduct.getName(),
                        restaurantProduct.getPrice()
                );
            }
        }));
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        Map<ProductId, Product> restaurantProductsMap = new HashMap<>();
        restaurant.getProducts().forEach(product -> restaurantProductsMap.put(product.getId(), product));

        order.getItems().forEach(orderItem -> {
            Product currentProduct = orderItem.getProduct();
            Product restaurantProduct = restaurantProductsMap.get(currentProduct.getId());
            if (restaurantProduct != null) {
                currentProduct.updatedWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
            }
        });
    }
}
