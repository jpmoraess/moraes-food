package br.com.moraesit.restaurant.service.infra.persistence.approval.repository;

import br.com.moraesit.restaurant.service.infra.persistence.approval.entity.OrderApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderApprovalJpaRepository extends JpaRepository<OrderApprovalEntity, UUID> {
}
