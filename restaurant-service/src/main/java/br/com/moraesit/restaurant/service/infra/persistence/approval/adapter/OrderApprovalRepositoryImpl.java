package br.com.moraesit.restaurant.service.infra.persistence.approval.adapter;

import br.com.moraesit.restaurant.service.application.ports.output.repository.OrderApprovalRepository;
import br.com.moraesit.restaurant.service.domain.entity.OrderApproval;
import br.com.moraesit.restaurant.service.infra.persistence.approval.mapper.OrderApprovalPersistenceMapper;
import br.com.moraesit.restaurant.service.infra.persistence.approval.repository.OrderApprovalJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderApprovalRepositoryImpl implements OrderApprovalRepository {

    private final OrderApprovalJpaRepository orderApprovalJpaRepository;

    public OrderApprovalRepositoryImpl(OrderApprovalJpaRepository orderApprovalJpaRepository) {
        this.orderApprovalJpaRepository = orderApprovalJpaRepository;
    }

    @Override
    public OrderApproval save(OrderApproval orderApproval) {
        return OrderApprovalPersistenceMapper
                .orderApprovalEntityToOrderApproval(orderApprovalJpaRepository
                        .save(OrderApprovalPersistenceMapper.orderApprovalToOrderApprovalEntity(orderApproval)));
    }
}
