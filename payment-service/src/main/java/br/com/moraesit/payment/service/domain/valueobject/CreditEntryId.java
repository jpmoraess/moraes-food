package br.com.moraesit.payment.service.domain.valueobject;

import br.com.moraesit.commons.domain.valueobject.BaseId;

import java.util.UUID;

public class CreditEntryId extends BaseId<UUID> {

    public CreditEntryId(UUID value) {
        super(value);
    }
}
