package br.com.moraesit.restaurant.service.domain.valueobject;

import br.com.moraesit.commons.domain.valueobject.BaseId;

import java.util.UUID;

public class OrderApprovalId extends BaseId<UUID> {

    public OrderApprovalId(UUID value) {
        super(value);
    }
}
