package br.com.moraesit.order.service.infra.rest;

import br.com.moraesit.order.service.application.dto.create.CreateOrderInput;
import br.com.moraesit.order.service.application.dto.create.CreateOrderOutput;
import br.com.moraesit.order.service.application.dto.track.TrackOrderInput;
import br.com.moraesit.order.service.application.dto.track.TrackOrderOutput;
import br.com.moraesit.order.service.application.ports.input.service.OrderApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderOutput> createOrder(@RequestBody CreateOrderInput input) {
        logger.info("Creating order for customer: {} at restaurant: {}", input.getCustomerId(), input.getRestaurantId());
        CreateOrderOutput createOrderOutput = orderApplicationService.createOrder(input);
        logger.info("Order created with tracking id: {}", createOrderOutput.getOrderTrackingId());
        return ResponseEntity.ok(createOrderOutput);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderOutput> getOrderByTrackingId(@PathVariable UUID trackingId) {
        TrackOrderOutput trackOrderOutput = orderApplicationService
                .trackOrder(TrackOrderInput.builder().orderTrackingId(trackingId).build());
        logger.info("Returning order status with tracking id: {}", trackOrderOutput.getOrderTrackingId());
        return ResponseEntity.ok(trackOrderOutput);
    }
}
