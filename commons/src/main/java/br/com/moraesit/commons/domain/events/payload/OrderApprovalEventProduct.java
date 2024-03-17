package br.com.moraesit.commons.domain.events.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderApprovalEventProduct {

    @JsonProperty
    private String id;

    @JsonProperty
    private Integer quantity;

    public OrderApprovalEventProduct() {
    }

    private OrderApprovalEventProduct(Builder builder) {
        id = builder.id;
        quantity = builder.quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public static final class Builder {
        private String id;
        private Integer quantity;

        private Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder quantity(Integer val) {
            quantity = val;
            return this;
        }

        public OrderApprovalEventProduct build() {
            return new OrderApprovalEventProduct(this);
        }
    }
}
