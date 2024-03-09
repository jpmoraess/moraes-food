package br.com.moraesit.payment.service.domain.valueobject;

import br.com.moraesit.commons.domain.valueobject.BaseId;

import java.util.UUID;

public class PaymentId extends BaseId<UUID> {

    public PaymentId(UUID value) {
        super(value);
    }
}
