package br.com.moraesit.order.service.domain.valueobject;

import br.com.moraesit.commons.domain.valueobject.BaseId;

import java.util.UUID;

public class TrackingId extends BaseId<UUID> {
    public TrackingId(UUID value) {
        super(value);
    }
}
