package br.com.moraesit.order.service.application.ports.output.repository;

import br.com.moraesit.order.service.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Optional<Customer> findCustomer(UUID customerId);
}
