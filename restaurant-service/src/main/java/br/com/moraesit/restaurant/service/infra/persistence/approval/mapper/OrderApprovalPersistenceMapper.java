package br.com.moraesit.restaurant.service.infra.persistence.approval.mapper;

import br.com.moraesit.commons.domain.valueobject.OrderId;
import br.com.moraesit.commons.domain.valueobject.RestaurantId;
import br.com.moraesit.restaurant.service.domain.entity.OrderApproval;
import br.com.moraesit.restaurant.service.domain.valueobject.OrderApprovalId;
import br.com.moraesit.restaurant.service.infra.persistence.approval.entity.OrderApprovalEntity;

public class OrderApprovalPersistenceMapper {

    public static OrderApprovalEntity orderApprovalToOrderApprovalEntity(OrderApproval orderApproval) {
        return OrderApprovalEntity.builder()
                .id(orderApproval.getId().getValue())
                .restaurantId(orderApproval.getRestaurantId().getValue())
                .orderId(orderApproval.getOrderId().getValue())
                .status(orderApproval.getApprovalStatus())
                .build();
    }

    public static OrderApproval orderApprovalEntityToOrderApproval(OrderApprovalEntity orderApprovalEntity) {
        return OrderApproval.builder()
                .orderApprovalId(new OrderApprovalId(orderApprovalEntity.getId()))
                .restaurantId(new RestaurantId(orderApprovalEntity.getRestaurantId()))
                .orderId(new OrderId(orderApprovalEntity.getOrderId()))
                .approvalStatus(orderApprovalEntity.getStatus())
                .build();
    }
}
