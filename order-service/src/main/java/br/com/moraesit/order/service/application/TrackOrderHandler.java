package br.com.moraesit.order.service.application;

import br.com.moraesit.order.service.application.dto.track.TrackOrderInput;
import br.com.moraesit.order.service.application.dto.track.TrackOrderOutput;
import br.com.moraesit.order.service.application.mapper.OrderDataMapper;
import br.com.moraesit.order.service.application.ports.output.repository.OrderRepository;
import br.com.moraesit.order.service.domain.entity.Order;
import br.com.moraesit.order.service.domain.exception.OrderNotFoundException;
import br.com.moraesit.order.service.domain.valueobject.TrackingId;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class TrackOrderHandler {
    private final Logger logger = LoggerFactory.getLogger(TrackOrderHandler.class);

    private final OrderRepository orderRepository;

    public TrackOrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public TrackOrderOutput trackOrder(TrackOrderInput input) {
        Optional<Order> orderResult = orderRepository.findByTrackingId(new TrackingId(input.getOrderTrackingId()));
        if (orderResult.isEmpty()) {
            logger.warn("Could not find order with tracking id: {}", input.getOrderTrackingId());
            throw new OrderNotFoundException("Could not find order with tracking id: " + input.getOrderTrackingId());
        }
        return OrderDataMapper.orderToTrackOrderOutput(orderResult.get());
    }
}
