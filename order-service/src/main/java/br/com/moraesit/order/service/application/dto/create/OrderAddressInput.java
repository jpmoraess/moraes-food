package br.com.moraesit.order.service.application.dto.create;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderAddressInput {
    @NotNull
    @Max(value = 50)
    private String street;
    @NotNull
    @Max(value = 10)
    private String postalCode;
    @NotNull
    @Max(value = 50)
    private String city;

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }
}
