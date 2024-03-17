package br.com.moraesit.commons.domain.events.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovalEventPayload {

    @JsonProperty
    private String orderId;

    @JsonProperty
    private String restaurantId;

    @JsonProperty
    private BigDecimal price;

    @JsonProperty
    private ZonedDateTime createdAt;

    @JsonProperty
    private String restaurantOrderStatus;

    @JsonProperty
    private List<OrderApprovalEventProduct> products;

    private OrderApprovalEventPayload(Builder builder) {
        orderId = builder.orderId;
        restaurantId = builder.restaurantId;
        price = builder.price;
        createdAt = builder.createdAt;
        restaurantOrderStatus = builder.restaurantOrderStatus;
        products = builder.products;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getOrderId() {
        return orderId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public String getRestaurantOrderStatus() {
        return restaurantOrderStatus;
    }

    public List<OrderApprovalEventProduct> getProducts() {
        return products;
    }

    public static final class Builder {
        private String orderId;
        private String restaurantId;
        private BigDecimal price;
        private ZonedDateTime createdAt;
        private String restaurantOrderStatus;
        private List<OrderApprovalEventProduct> products;

        private Builder() {
        }

        public Builder orderId(String val) {
            orderId = val;
            return this;
        }

        public Builder restaurantId(String val) {
            restaurantId = val;
            return this;
        }

        public Builder price(BigDecimal val) {
            price = val;
            return this;
        }

        public Builder createdAt(ZonedDateTime val) {
            createdAt = val;
            return this;
        }

        public Builder restaurantOrderStatus(String val) {
            restaurantOrderStatus = val;
            return this;
        }

        public Builder products(List<OrderApprovalEventProduct> val) {
            products = val;
            return this;
        }

        public OrderApprovalEventPayload build() {
            return new OrderApprovalEventPayload(this);
        }
    }
}
