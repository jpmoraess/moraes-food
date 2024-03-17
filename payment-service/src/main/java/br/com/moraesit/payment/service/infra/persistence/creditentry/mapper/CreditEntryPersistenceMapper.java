package br.com.moraesit.payment.service.infra.persistence.creditentry.mapper;

import br.com.moraesit.commons.domain.valueobject.CustomerId;
import br.com.moraesit.commons.domain.valueobject.Money;
import br.com.moraesit.payment.service.domain.entity.CreditEntry;
import br.com.moraesit.payment.service.domain.valueobject.CreditEntryId;
import br.com.moraesit.payment.service.infra.persistence.creditentry.entity.CreditEntryEntity;

public class CreditEntryPersistenceMapper {

    public static CreditEntry creditEntryEntityToCreditEntry(CreditEntryEntity creditEntryEntity) {
        return CreditEntry.builder()
                .creditEntryId(new CreditEntryId(creditEntryEntity.getId()))
                .customerId(new CustomerId(creditEntryEntity.getCustomerId()))
                .totalCreditAmount(new Money(creditEntryEntity.getTotalCreditAmount()))
                .version(creditEntryEntity.getVersion())
                .build();
    }

    public static CreditEntryEntity creditEntryToCreditEntryEntity(CreditEntry creditEntry) {
        return CreditEntryEntity.builder()
                .id(creditEntry.getId().getValue())
                .customerId(creditEntry.getCustomerId().getValue())
                .totalCreditAmount(creditEntry.getTotalCreditAmount().getAmount())
                .version(creditEntry.getVersion())
                .build();
    }

}
