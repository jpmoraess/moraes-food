package br.com.moraesit.order.service.infra.persistence.order.adapter;

import br.com.moraesit.order.service.application.ports.output.repository.OrderRepository;
import br.com.moraesit.order.service.domain.entity.Order;
import br.com.moraesit.order.service.domain.valueobject.TrackingId;
import br.com.moraesit.order.service.infra.persistence.order.mapper.OrderPersistenceMapper;
import br.com.moraesit.order.service.infra.persistence.order.repository.OrderJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public Order save(Order order) {
        return OrderPersistenceMapper.orderEntityToOrder(orderJpaRepository
                .save(OrderPersistenceMapper.orderToOrderEntity(order)));
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJpaRepository.findByTrackingId(trackingId.getValue())
                .map(OrderPersistenceMapper::orderEntityToOrder);
    }
}
