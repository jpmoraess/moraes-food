package br.com.moraesit.order.service.domain;

import br.com.moraesit.order.service.domain.entity.Order;
import br.com.moraesit.order.service.domain.entity.Restaurant;
import br.com.moraesit.order.service.domain.event.OrderCancelledEvent;
import br.com.moraesit.order.service.domain.event.OrderCreatedEvent;
import br.com.moraesit.order.service.domain.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {
    OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant);

    OrderPaidEvent payOrder(Order order);

    void approveOrder(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);

    void cancelOrder(Order order, List<String> failureMessages);
}
