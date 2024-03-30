package br.com.moraesit.restaurant.service.application;

import br.com.moraesit.restaurant.service.application.dto.RestaurantApprovalRequest;
import br.com.moraesit.restaurant.service.application.ports.input.message.listener.RestaurantApprovalRequestMessageListener;
import org.springframework.stereotype.Component;

@Component
public class RestaurantApprovalRequestMessageListenerImpl implements RestaurantApprovalRequestMessageListener {

    private final RestaurantApprovalRequestHelper restaurantApprovalRequestHelper;

    public RestaurantApprovalRequestMessageListenerImpl(RestaurantApprovalRequestHelper restaurantApprovalRequestHelper) {
        this.restaurantApprovalRequestHelper = restaurantApprovalRequestHelper;
    }

    @Override
    public void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest) {
        restaurantApprovalRequestHelper.persistOrderApproval(restaurantApprovalRequest);
    }
}
