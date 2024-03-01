package br.com.moraesit.order.service.application.ports.input.service;

import br.com.moraesit.order.service.application.dto.create.CreateOrderInput;
import br.com.moraesit.order.service.application.dto.create.CreateOrderOutput;
import br.com.moraesit.order.service.application.dto.track.TrackOrderInput;
import br.com.moraesit.order.service.application.dto.track.TrackOrderOutput;
import jakarta.validation.Valid;

public interface OrderApplicationService {
    CreateOrderOutput createOrder(@Valid CreateOrderInput input);

    TrackOrderOutput trackOrder(@Valid TrackOrderInput input);
}
