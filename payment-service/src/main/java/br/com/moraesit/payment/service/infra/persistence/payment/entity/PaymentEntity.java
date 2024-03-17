package br.com.moraesit.payment.service.infra.persistence.payment.entity;

import br.com.moraesit.commons.domain.valueobject.PaymentStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    private UUID id;
    private UUID customerId;
    private UUID orderId;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private ZonedDateTime createdAt;

    public PaymentEntity() {
    }

    private PaymentEntity(Builder builder) {
        id = builder.id;
        customerId = builder.customerId;
        orderId = builder.orderId;
        price = builder.price;
        status = builder.status;
        createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentEntity that = (PaymentEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static final class Builder {
        private UUID id;
        private UUID customerId;
        private UUID orderId;
        private BigDecimal price;
        private PaymentStatus status;
        private ZonedDateTime createdAt;

        private Builder() {
        }

        public Builder id(UUID val) {
            id = val;
            return this;
        }

        public Builder customerId(UUID val) {
            customerId = val;
            return this;
        }

        public Builder orderId(UUID val) {
            orderId = val;
            return this;
        }

        public Builder price(BigDecimal val) {
            price = val;
            return this;
        }

        public Builder status(PaymentStatus val) {
            status = val;
            return this;
        }

        public Builder createdAt(ZonedDateTime val) {
            createdAt = val;
            return this;
        }

        public PaymentEntity build() {
            return new PaymentEntity(this);
        }
    }
}
