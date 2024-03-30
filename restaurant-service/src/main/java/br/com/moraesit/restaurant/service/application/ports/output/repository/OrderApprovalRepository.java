package br.com.moraesit.restaurant.service.application.ports.output.repository;

import br.com.moraesit.restaurant.service.domain.entity.OrderApproval;

public interface OrderApprovalRepository {

    OrderApproval save(OrderApproval orderApproval);
}
