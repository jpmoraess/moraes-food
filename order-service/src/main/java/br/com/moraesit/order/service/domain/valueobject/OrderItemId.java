package br.com.moraesit.order.service.domain.valueobject;

import br.com.moraesit.commons.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> {
    public OrderItemId(Long value) {
        super(value);
    }
}
