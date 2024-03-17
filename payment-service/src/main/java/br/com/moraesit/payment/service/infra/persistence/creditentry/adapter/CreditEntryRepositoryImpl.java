package br.com.moraesit.payment.service.infra.persistence.creditentry.adapter;

import br.com.moraesit.payment.service.application.ports.output.repository.CreditEntryRepository;
import br.com.moraesit.payment.service.domain.entity.CreditEntry;
import br.com.moraesit.payment.service.infra.persistence.creditentry.mapper.CreditEntryPersistenceMapper;
import br.com.moraesit.payment.service.infra.persistence.creditentry.repository.CreditEntryJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CreditEntryRepositoryImpl implements CreditEntryRepository {

    private final CreditEntryJpaRepository creditEntryJpaRepository;

    public CreditEntryRepositoryImpl(CreditEntryJpaRepository creditEntryJpaRepository) {
        this.creditEntryJpaRepository = creditEntryJpaRepository;
    }

    @Override
    public CreditEntry save(CreditEntry creditEntry) {
        return CreditEntryPersistenceMapper.creditEntryEntityToCreditEntry(creditEntryJpaRepository
                .save(CreditEntryPersistenceMapper.creditEntryToCreditEntryEntity(creditEntry)));
    }

    @Override
    public Optional<CreditEntry> findByCustomerId(UUID customerId) {
        return creditEntryJpaRepository
                .findByCustomerId(customerId)
                .map(CreditEntryPersistenceMapper::creditEntryEntityToCreditEntry);
    }
}
