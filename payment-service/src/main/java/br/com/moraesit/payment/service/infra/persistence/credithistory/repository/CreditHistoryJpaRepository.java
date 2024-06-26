package br.com.moraesit.payment.service.infra.persistence.credithistory.repository;

import br.com.moraesit.payment.service.infra.persistence.credithistory.entity.CreditHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CreditHistoryJpaRepository extends JpaRepository<CreditHistoryEntity, UUID> {

    Optional<List<CreditHistoryEntity>> findByCustomerId(UUID customerId);
}
