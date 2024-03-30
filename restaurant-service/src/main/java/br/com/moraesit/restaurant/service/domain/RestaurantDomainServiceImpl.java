package br.com.moraesit.restaurant.service.domain;

import br.com.moraesit.commons.domain.valueobject.OrderApprovalStatus;
import br.com.moraesit.restaurant.service.domain.entity.Restaurant;
import br.com.moraesit.restaurant.service.domain.event.OrderApprovalEvent;
import br.com.moraesit.restaurant.service.domain.event.OrderApprovedEvent;
import br.com.moraesit.restaurant.service.domain.event.OrderRejectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static br.com.moraesit.commons.domain.DomainConstants.UTC;

@Component // remover dependência de framework do domain core (expor bean em outra camada)
public class RestaurantDomainServiceImpl implements RestaurantDomainService {

    private final Logger logger = LoggerFactory.getLogger(RestaurantDomainServiceImpl.class);

    @Override
    public OrderApprovalEvent validateOrder(Restaurant restaurant, List<String> failureMessages) {
        restaurant.validateOrder(failureMessages);
        logger.info("Validating order with id: {}", restaurant.getOrderDetail().getId().getValue());
        if (failureMessages.isEmpty()) {
            logger.info("Order is approved for order id: {}", restaurant.getOrderDetail().getId().getValue());
            restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);
            return new OrderApprovedEvent(restaurant.getOrderApproval(), restaurant.getId(), failureMessages, ZonedDateTime.now(ZoneId.of(UTC)));
        } else {
            logger.info("Order is rejected for order id: {}", restaurant.getOrderDetail().getId().getValue());
            restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);
            return new OrderRejectedEvent(restaurant.getOrderApproval(), restaurant.getId(), failureMessages, ZonedDateTime.now(ZoneId.of(UTC)));
        }
    }
}
