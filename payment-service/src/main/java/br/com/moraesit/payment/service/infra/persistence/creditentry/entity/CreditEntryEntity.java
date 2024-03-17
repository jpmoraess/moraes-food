package br.com.moraesit.payment.service.infra.persistence.creditentry.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "credit_entry")
public class CreditEntryEntity {

    @Id
    private UUID id;
    private UUID customerId;
    private BigDecimal totalCreditAmount;
    @Version
    private int version;

    public CreditEntryEntity() {
    }

    private CreditEntryEntity(Builder builder) {
        id = builder.id;
        customerId = builder.customerId;
        totalCreditAmount = builder.totalCreditAmount;
        version = builder.version;
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

    public BigDecimal getTotalCreditAmount() {
        return totalCreditAmount;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditEntryEntity that = (CreditEntryEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static final class Builder {
        private UUID id;
        private UUID customerId;
        private BigDecimal totalCreditAmount;
        private int version;

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

        public Builder totalCreditAmount(BigDecimal val) {
            totalCreditAmount = val;
            return this;
        }

        public Builder version(int val) {
            version = val;
            return this;
        }

        public CreditEntryEntity build() {
            return new CreditEntryEntity(this);
        }
    }
}
