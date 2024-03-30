package br.com.moraesit.restaurant.service.application.ports.input.message.listener;

import br.com.moraesit.restaurant.service.application.dto.RestaurantApprovalRequest;

public interface RestaurantApprovalRequestMessageListener {

    void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest);
}
