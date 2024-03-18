package br.com.moraesit.order.service.application.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderInput {

    @NotNull
    private UUID customerId;

    @NotNull
    private UUID restaurantId;

    @NotNull
    private BigDecimal price;

    @NotNull
    private List<OrderItemInput> items;

    @NotNull
    private OrderAddressInput orderAddress;
}
