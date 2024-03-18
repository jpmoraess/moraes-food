package br.com.moraesit.payment.service.infra.persistence.outbox.entity;

import br.com.moraesit.commons.domain.valueobject.PaymentStatus;
import br.com.moraesit.commons.outbox.OutboxStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_outbox")
@Entity
public class OrderOutboxEntity {

    @Id
    private UUID id;
    private UUID sagaId;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    @Enumerated(EnumType.STRING)
    private OutboxStatus outboxStatus;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Version
    private int version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderOutboxEntity that = (OrderOutboxEntity) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
