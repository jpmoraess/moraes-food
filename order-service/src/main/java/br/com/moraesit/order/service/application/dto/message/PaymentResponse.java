package br.com.moraesit.order.service.application.dto.message;

import br.com.moraesit.commons.domain.valueobject.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PaymentResponse {
    private String id;
    private String sagaId;
    private String orderId;
    private String paymentId;
    private String customerId;
    private BigDecimal price;
    private Instant createdAt;
    private PaymentStatus paymentStatus;
    private List<String> failureMessages;

    public PaymentResponse() {
    }

    private PaymentResponse(Builder builder) {
        id = builder.id;
        sagaId = builder.sagaId;
        orderId = builder.orderId;
        paymentId = builder.paymentId;
        customerId = builder.customerId;
        price = builder.price;
        createdAt = builder.createdAt;
        paymentStatus = builder.paymentStatus;
        failureMessages = builder.failureMessages;
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

    public String getPaymentId() {
        return paymentId;
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

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private String id;
        private String sagaId;
        private String orderId;
        private String paymentId;
        private String customerId;
        private BigDecimal price;
        private Instant createdAt;
        private PaymentStatus paymentStatus;
        private List<String> failureMessages;

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

        public Builder paymentId(String val) {
            paymentId = val;
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

        public Builder paymentStatus(PaymentStatus val) {
            paymentStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public PaymentResponse build() {
            return new PaymentResponse(this);
        }
    }
}
