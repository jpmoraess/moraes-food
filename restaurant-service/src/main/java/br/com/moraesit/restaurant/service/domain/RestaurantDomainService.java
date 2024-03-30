package br.com.moraesit.restaurant.service.domain;

import br.com.moraesit.restaurant.service.domain.entity.Restaurant;
import br.com.moraesit.restaurant.service.domain.event.OrderApprovalEvent;

import java.util.List;

public interface RestaurantDomainService {

    OrderApprovalEvent validateOrder(Restaurant restaurant, List<String> failureMessages);
}
