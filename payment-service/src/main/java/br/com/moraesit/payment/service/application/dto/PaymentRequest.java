package br.com.moraesit.payment.service.application.dto;

import br.com.moraesit.commons.domain.valueobject.PaymentOrderStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class PaymentRequest {

    private final String id;
    private final String sagaId;
    private final String orderId;
    private final String customerId;
    private final BigDecimal price;
    private final Instant createdAt;
    private PaymentOrderStatus paymentOrderStatus;

    private PaymentRequest(Builder builder) {
        id = builder.id;
        sagaId = builder.sagaId;
        orderId = builder.orderId;
        customerId = builder.customerId;
        price = builder.price;
        createdAt = builder.createdAt;
        setPaymentOrderStatus(builder.paymentOrderStatus);
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public PaymentOrderStatus getPaymentOrderStatus() {
        return paymentOrderStatus;
    }

    public void setPaymentOrderStatus(PaymentOrderStatus paymentOrderStatus) {
        this.paymentOrderStatus = paymentOrderStatus;
    }

    public static final class Builder {
        private String id;
        private String sagaId;
        private String orderId;
        private String customerId;
        private BigDecimal price;
        private Instant createdAt;
        private PaymentOrderStatus paymentOrderStatus;

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

        public Builder createdAt(Instant val) {
            createdAt = val;
            return this;
        }

        public Builder paymentOrderStatus(PaymentOrderStatus val) {
            paymentOrderStatus = val;
            return this;
        }

        public PaymentRequest build() {
            return new PaymentRequest(this);
        }
    }
}
