package br.com.moraesit.commons.domain.events.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class OrderPaymentEventPayload {

    @JsonProperty
    private String id;

    @JsonProperty
    private String sagaId;

    @JsonProperty
    private String orderId;

    @JsonProperty
    private String customerId;

    @JsonProperty
    private BigDecimal price;

    @JsonProperty
    private ZonedDateTime createdAt;

    @JsonProperty
    private String paymentOrderStatus;

    public OrderPaymentEventPayload() {
    }

    private OrderPaymentEventPayload(Builder builder) {
        id = builder.id;
        sagaId = builder.sagaId;
        orderId = builder.orderId;
        customerId = builder.customerId;
        price = builder.price;
        createdAt = builder.createdAt;
        paymentOrderStatus = builder.paymentOrderStatus;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public String getSagaId() {
        return sagaId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public String getPaymentOrderStatus() {
        return paymentOrderStatus;
    }

    public static final class Builder {
        private String id;
        private String sagaId;
        private String orderId;
        private String customerId;
        private BigDecimal price;
        private ZonedDateTime createdAt;
        private String paymentOrderStatus;

        private Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder sagaId(String val) {
            sagaId = val;
            return this;
        }

        public Builder orderId(String val) {
            orderId = val;
            return this;
        }

        public Builder customerId(String val) {
            customerId = val;
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

        public Builder paymentOrderStatus(String val) {
            paymentOrderStatus = val;
            return this;
        }

        public OrderPaymentEventPayload build() {
            return new OrderPaymentEventPayload(this);
        }
    }
}
