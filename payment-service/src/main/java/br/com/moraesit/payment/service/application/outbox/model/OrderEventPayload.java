package br.com.moraesit.payment.service.application.outbox.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class OrderEventPayload {

    @JsonProperty
    private String paymentId;

    @JsonProperty
    private String customerId;

    @JsonProperty
    private String orderId;

    @JsonProperty
    private BigDecimal price;

    @JsonProperty
    private ZonedDateTime createdAt;

    @JsonProperty
    private String paymentStatus;

    @JsonProperty
    private List<String> failureMessages;

    public OrderEventPayload() {
    }

    private OrderEventPayload(Builder builder) {
        paymentId = builder.paymentId;
        customerId = builder.customerId;
        orderId = builder.orderId;
        price = builder.price;
        createdAt = builder.createdAt;
        paymentStatus = builder.paymentStatus;
        failureMessages = builder.failureMessages;
    }

    public static Builder builder() {
        return new Builder();
    }


    public String getPaymentId() {
        return paymentId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private String paymentId;
        private String customerId;
        private String orderId;
        private BigDecimal price;
        private ZonedDateTime createdAt;
        private String paymentStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public Builder paymentId(String val) {
            paymentId = val;
            return this;
        }

        public Builder customerId(String val) {
            customerId = val;
            return this;
        }

        public Builder orderId(String val) {
            orderId = val;
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

        public Builder paymentStatus(String val) {
            paymentStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public OrderEventPayload build() {
            return new OrderEventPayload(this);
        }
    }
}
