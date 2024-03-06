package br.com.moraesit.order.service.infra.persistence.customer.adapter;

import br.com.moraesit.order.service.application.ports.output.repository.CustomerRepository;
import br.com.moraesit.order.service.domain.entity.Customer;
import br.com.moraesit.order.service.infra.persistence.customer.mapper.CustomerPersistenceMapper;
import br.com.moraesit.order.service.infra.persistence.customer.repository.CustomerJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    public CustomerRepositoryImpl(CustomerJpaRepository customerJpaRepository) {
        this.customerJpaRepository = customerJpaRepository;
    }

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return customerJpaRepository.findById(customerId)
                .map(CustomerPersistenceMapper::customerEntityToCustomer);
    }
}
