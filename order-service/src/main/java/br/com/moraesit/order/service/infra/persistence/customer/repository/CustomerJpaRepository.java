package br.com.moraesit.order.service.infra.persistence.customer.repository;

import br.com.moraesit.order.service.infra.persistence.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {
}
