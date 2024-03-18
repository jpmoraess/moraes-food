package br.com.moraesit.order.service.application.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderItemInput {

    @NotNull
    private UUID productId;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal price;

    @NotNull
    private BigDecimal subTotal;
}
