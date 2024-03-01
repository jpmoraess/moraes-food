package br.com.moraesit.order.service.application;

import br.com.moraesit.order.service.application.dto.create.CreateOrderInput;
import br.com.moraesit.order.service.application.dto.create.CreateOrderOutput;
import br.com.moraesit.order.service.application.dto.track.TrackOrderInput;
import br.com.moraesit.order.service.application.dto.track.TrackOrderOutput;
import br.com.moraesit.order.service.application.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final TrackOrderHandler trackOrderHandler;
    private final CreateOrderHandler createOrderHandler;

    public OrderApplicationServiceImpl(TrackOrderHandler trackOrderHandler, CreateOrderHandler createOrderHandler) {
        this.trackOrderHandler = trackOrderHandler;
        this.createOrderHandler = createOrderHandler;
    }

    @Override
    public CreateOrderOutput createOrder(CreateOrderInput input) {
        return createOrderHandler.createOrder(input);
    }

    @Override
    public TrackOrderOutput trackOrder(TrackOrderInput input) {
        return trackOrderHandler.trackOrder(input);
    }
}
