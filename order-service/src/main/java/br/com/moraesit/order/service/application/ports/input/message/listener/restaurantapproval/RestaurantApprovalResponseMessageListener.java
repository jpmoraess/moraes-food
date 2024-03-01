package br.com.moraesit.order.service.application.ports.input.message.listener.restaurantapproval;

import br.com.moraesit.order.service.application.dto.message.RestaurantApprovalResponse;

public interface RestaurantApprovalResponseMessageListener {
    void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse);

    void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}
