package br.com.moraesit.payment.service.infra.persistence.credithistory.adapter;

import br.com.moraesit.payment.service.application.ports.output.repository.CreditHistoryRepository;
import br.com.moraesit.payment.service.domain.entity.CreditHistory;
import br.com.moraesit.payment.service.infra.persistence.credithistory.entity.CreditHistoryEntity;
import br.com.moraesit.payment.service.infra.persistence.credithistory.mapper.CreditHistoryPersistenceMapper;
import br.com.moraesit.payment.service.infra.persistence.credithistory.repository.CreditHistoryJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CreditHistoryRepositoryImpl implements CreditHistoryRepository {

    private final CreditHistoryJpaRepository creditHistoryJpaRepository;

    public CreditHistoryRepositoryImpl(CreditHistoryJpaRepository creditHistoryJpaRepository) {
        this.creditHistoryJpaRepository = creditHistoryJpaRepository;
    }

    @Override
    public CreditHistory save(CreditHistory creditHistory) {
        return CreditHistoryPersistenceMapper.creditHistoryEntityToCreditHistory(creditHistoryJpaRepository
                .save(CreditHistoryPersistenceMapper.creditHistoryToCreditHistoryEntity(creditHistory)));
    }

    @Override
    public Optional<List<CreditHistory>> findByCustomerId(UUID customerId) {
        Optional<List<CreditHistoryEntity>> creditHistory =
                creditHistoryJpaRepository.findByCustomerId(customerId);
        return creditHistory
                .map(creditHistoryList ->
                        creditHistoryList.stream()
                                .map(CreditHistoryPersistenceMapper::creditHistoryEntityToCreditHistory)
                                .collect(Collectors.toList()));
    }
}
