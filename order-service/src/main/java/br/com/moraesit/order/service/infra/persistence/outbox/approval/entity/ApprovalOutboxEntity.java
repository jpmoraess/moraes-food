package br.com.moraesit.order.service.infra.persistence.outbox.approval.entity;

import br.com.moraesit.commons.domain.valueobject.OrderStatus;
import br.com.moraesit.commons.outbox.OutboxStatus;
import br.com.moraesit.commons.saga.SagaStatus;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "restaurant_approval_outbox")
public class ApprovalOutboxEntity {

    @Id
    private UUID id;
    private UUID sagaId;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    @Enumerated(EnumType.STRING)
    private SagaStatus sagaStatus;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    private OutboxStatus outboxStatus;
    @Version
    private int version;

    public ApprovalOutboxEntity() {
    }

    private ApprovalOutboxEntity(Builder builder) {
        id = builder.id;
        sagaId = builder.sagaId;
        createdAt = builder.createdAt;
        processedAt = builder.processedAt;
        type = builder.type;
        payload = builder.payload;
        sagaStatus = builder.sagaStatus;
        orderStatus = builder.orderStatus;
        outboxStatus = builder.outboxStatus;
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

    public SagaStatus getSagaStatus() {
        return sagaStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OutboxStatus getOutboxStatus() {
        return outboxStatus;
    }

    public int getVersion() {
        return version;
    }

    public static final class Builder {
        private UUID id;
        private UUID sagaId;
        private ZonedDateTime createdAt;
        private ZonedDateTime processedAt;
        private String type;
        private String payload;
        private SagaStatus sagaStatus;
        private OrderStatus orderStatus;
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

        public Builder sagaStatus(SagaStatus val) {
            sagaStatus = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
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

        public ApprovalOutboxEntity build() {
            return new ApprovalOutboxEntity(this);
        }
    }
}
