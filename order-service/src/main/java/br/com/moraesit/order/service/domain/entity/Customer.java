package br.com.moraesit.order.service.domain.entity;

import br.com.moraesit.commons.domain.entity.AggregateRoot;
import br.com.moraesit.commons.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {

    public Customer() {
    }

    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }
}
