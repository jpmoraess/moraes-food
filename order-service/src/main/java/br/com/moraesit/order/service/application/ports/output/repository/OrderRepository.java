package br.com.moraesit.order.service.application.ports.output.repository;

import br.com.moraesit.commons.domain.valueobject.OrderId;
import br.com.moraesit.order.service.domain.entity.Order;
import br.com.moraesit.order.service.domain.valueobject.TrackingId;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(OrderId orderId);

    Optional<Order> findByTrackingId(TrackingId trackingId);
}
