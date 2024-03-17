package br.com.moraesit.payment.service.application.outbox.model;

import br.com.moraesit.commons.domain.valueobject.PaymentStatus;
import br.com.moraesit.commons.outbox.OutboxStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

public class OrderOutboxMessage {
    private UUID id;
    private UUID sagaId;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    private PaymentStatus paymentStatus;
    private OutboxStatus outboxStatus;
    private int version;

    public OrderOutboxMessage() {
    }

    private OrderOutboxMessage(Builder builder) {
        id = builder.id;
        sagaId = builder.sagaId;
        createdAt = builder.createdAt;
        processedAt = builder.processedAt;
        type = builder.type;
        payload = builder.payload;
        paymentStatus = builder.paymentStatus;
        setOutboxStatus(builder.outboxStatus);
        version = builder.version;
    }

    public static Builder builder() {
        return new Builder();
    }


    public UUID getId() {
        return id;
    }

    public UUID getSagaId() {
        return sagaId;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getProcessedAt() {
        return processedAt;
    }

    public String getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public OutboxStatus getOutboxStatus() {
        return outboxStatus;
    }

    public int getVersion() {
        return version;
    }

    public void setOutboxStatus(OutboxStatus outboxStatus) {
        this.outboxStatus = outboxStatus;
    }

    public static final class Builder {
        private UUID id;
        private UUID sagaId;
        private ZonedDateTime createdAt;
        private ZonedDateTime processedAt;
        private String type;
        private String payload;
        private PaymentStatus paymentStatus;
        private OutboxStatus outboxStatus;
        private int version;

        private Builder() {
        }

        public Builder id(UUID val) {
            id = val;
            return this;
        }

        public Builder sagaId(UUID val) {
            sagaId = val;
            return this;
        }

        public Builder createdAt(ZonedDateTime val) {
            createdAt = val;
            return this;
        }

        public Builder processedAt(ZonedDateTime val) {
            processedAt = val;
            return this;
        }

        public Builder type(String val) {
            type = val;
            return this;
        }

        public Builder payload(String val) {
            payload = val;
            return this;
        }

        public Builder paymentStatus(PaymentStatus val) {
            paymentStatus = val;
            return this;
        }

        public Builder outboxStatus(OutboxStatus val) {
            outboxStatus = val;
            return this;
        }

        public Builder version(int val) {
            version = val;
            return this;
        }

        public OrderOutboxMessage build() {
            return new OrderOutboxMessage(this);
        }
    }
}
