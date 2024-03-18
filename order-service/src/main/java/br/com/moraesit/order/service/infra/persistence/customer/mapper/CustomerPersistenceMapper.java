package br.com.moraesit.order.service.infra.persistence.customer.mapper;

import br.com.moraesit.commons.domain.valueobject.CustomerId;
import br.com.moraesit.order.service.domain.entity.Customer;
import br.com.moraesit.order.service.infra.persistence.customer.entity.CustomerEntity;

public class CustomerPersistenceMapper {

    public static Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return new Customer(new CustomerId(customerEntity.getId()));
    }

    public static CustomerEntity customerToCustomerEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId().getValue())
                .username(customer.getUsername())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
    }
}
