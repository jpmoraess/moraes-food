package br.com.moraesit.order.service.infra.rest;

import br.com.moraesit.order.service.application.dto.create.CreateOrderInput;
import br.com.moraesit.order.service.application.dto.create.CreateOrderOutput;
import br.com.moraesit.order.service.application.dto.track.TrackOrderInput;
import br.com.moraesit.order.service.application.dto.track.TrackOrderOutput;
import br.com.moraesit.order.service.application.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderOutput> createOrder(@RequestBody CreateOrderInput input) {
        log.info("Creating order for customer: {} at restaurant: {}", input.getCustomerId(), input.getRestaurantId());
        CreateOrderOutput createOrderOutput = orderApplicationService.createOrder(input);
        log.info("Order created with tracking id: {}", createOrderOutput.getOrderTrackingId());
        return ResponseEntity.ok(createOrderOutput);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderOutput> getOrderByTrackingId(@PathVariable UUID trackingId) {
        TrackOrderOutput trackOrderOutput = orderApplicationService
                .trackOrder(TrackOrderInput.builder().orderTrackingId(trackingId).build());
        log.info("Returning order status with tracking id: {}", trackOrderOutput.getOrderTrackingId());
        return ResponseEntity.ok(trackOrderOutput);
    }
}
