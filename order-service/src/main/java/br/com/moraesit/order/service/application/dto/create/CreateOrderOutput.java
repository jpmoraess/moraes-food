package br.com.moraesit.order.service.application.dto.create;

import br.com.moraesit.commons.domain.valueobject.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderOutput {
    @NotNull
    private UUID orderTrackingId;
    @NotNull
    private OrderStatus orderStatus;
    @NotNull
    private String message;

    public CreateOrderOutput(UUID orderTrackingId, OrderStatus orderStatus) {
        this.orderTrackingId = orderTrackingId;
        this.orderStatus = orderStatus;
    }

    public CreateOrderOutput(UUID orderTrackingId, OrderStatus orderStatus, String message) {
        this.orderTrackingId = orderTrackingId;
        this.orderStatus = orderStatus;
        this.message = message;
    }

    public UUID getOrderTrackingId() {
        return orderTrackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public String getMessage() {
        return message;
    }
}
