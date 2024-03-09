package br.com.moraesit.payment.service.domain.valueobject;

import br.com.moraesit.commons.domain.valueobject.BaseId;

import java.util.UUID;

public class CreditHistoryId extends BaseId<UUID> {

    public CreditHistoryId(UUID value) {
        super(value);
    }
}
