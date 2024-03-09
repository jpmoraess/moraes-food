package br.com.moraesit.payment.service.application.ports.output.repository;

import br.com.moraesit.payment.service.domain.entity.CreditEntry;

import java.util.Optional;
import java.util.UUID;

public interface CreditEntryRepository {

    CreditEntry save(CreditEntry creditEntry);

    Optional<CreditEntry> findByCustomerId(UUID customerId);
}
