package br.com.moraesit.payment.service.application.ports.output.repository;

import br.com.moraesit.payment.service.domain.entity.CreditHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreditHistoryRepository {

    CreditHistory save(CreditHistory creditHistory);

    Optional<List<CreditHistory>> findByCustomerId(UUID customerId);
}
