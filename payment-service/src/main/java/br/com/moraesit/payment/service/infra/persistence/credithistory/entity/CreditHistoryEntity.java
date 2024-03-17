package br.com.moraesit.payment.service.infra.persistence.credithistory.entity;

import br.com.moraesit.payment.service.domain.valueobject.TransactionType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "credit_history")
public class CreditHistoryEntity {

    @Id
    private UUID id;
    private UUID customerId;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    public CreditHistoryEntity() {
    }

    private CreditHistoryEntity(Builder builder) {
        id = builder.id;
        customerId = builder.customerId;
        amount = builder.amount;
        type = builder.type;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditHistoryEntity that = (CreditHistoryEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static final class Builder {
        private UUID id;
        private UUID customerId;
        private BigDecimal amount;
        private TransactionType type;

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

        public Builder amount(BigDecimal val) {
            amount = val;
            return this;
        }

        public Builder type(TransactionType val) {
            type = val;
            return this;
        }

        public CreditHistoryEntity build() {
            return new CreditHistoryEntity(this);
        }
    }
}
