package br.com.moraesit.restaurant.service.application;

import br.com.moraesit.commons.domain.valueobject.OrderId;
import br.com.moraesit.commons.outbox.OutboxStatus;
import br.com.moraesit.restaurant.service.application.dto.RestaurantApprovalRequest;
import br.com.moraesit.restaurant.service.application.mapper.RestaurantDataMapper;
import br.com.moraesit.restaurant.service.application.outbox.helper.OrderOutboxHelper;
import br.com.moraesit.restaurant.service.application.outbox.model.OrderOutboxMessage;
import br.com.moraesit.restaurant.service.application.ports.output.repository.OrderApprovalRepository;
import br.com.moraesit.restaurant.service.application.ports.output.repository.RestaurantRepository;
import br.com.moraesit.restaurant.service.domain.RestaurantDomainService;
import br.com.moraesit.restaurant.service.domain.entity.Restaurant;
import br.com.moraesit.restaurant.service.domain.event.OrderApprovalEvent;
import br.com.moraesit.restaurant.service.domain.exception.RestaurantDomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class RestaurantApprovalRequestHelper {

    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantRepository restaurantRepository;
    private final OrderApprovalRepository orderApprovalRepository;
    private final OrderOutboxHelper orderOutboxHelper;

    public RestaurantApprovalRequestHelper(RestaurantDomainService restaurantDomainService,
                                           RestaurantRepository restaurantRepository,
                                           OrderApprovalRepository orderApprovalRepository,
                                           OrderOutboxHelper orderOutboxHelper) {
        this.restaurantDomainService = restaurantDomainService;
        this.restaurantRepository = restaurantRepository;
        this.orderApprovalRepository = orderApprovalRepository;
        this.orderOutboxHelper = orderOutboxHelper;
    }

    @Transactional
    public void persistOrderApproval(RestaurantApprovalRequest restaurantApprovalRequest) {
        if (isOutboxMessageProcessed(restaurantApprovalRequest)) {
            log.info("An outbox message with saga id: {} already saved to database!", restaurantApprovalRequest.getSagaId());
            return;
        }
        log.info("Processing restaurant approval for order id: {}", restaurantApprovalRequest.getOrderId());
        List<String> failureMessages = new ArrayList<>();
        Restaurant restaurant = findRestaurant(restaurantApprovalRequest);
        OrderApprovalEvent orderApprovalEvent = restaurantDomainService.validateOrder(restaurant, failureMessages);
        orderApprovalRepository.save(restaurant.getOrderApproval());
        orderOutboxHelper.saveOrderOutboxMessage(
                RestaurantDataMapper.orderApprovalEventToOrderEventPayload(orderApprovalEvent),
                orderApprovalEvent.getOrderApproval().getApprovalStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(restaurantApprovalRequest.getSagaId()));
    }

    private Restaurant findRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
        Restaurant restaurant = RestaurantDataMapper.restaurantApprovalRequestToRestaurant(restaurantApprovalRequest);
        Optional<Restaurant> restaurantOptional = restaurantRepository.findRestaurantInformation(restaurant);
        if (restaurantOptional.isEmpty()) {
            log.error("Restaurant with id: {} not found!", restaurantApprovalRequest.getRestaurantId());
            throw new RestaurantDomainException("Restaurant with id: " + restaurantApprovalRequest.getRestaurantId() + " not found!");
        }
        Restaurant restaurantEntity = restaurantOptional.get();
        restaurant.setActive(restaurantEntity.isActive());
        restaurant.getOrderDetail().getProducts().forEach(product -> restaurantEntity.getOrderDetail().getProducts()
                .forEach(p -> {
                    if (p.getId().equals(product.getId()))
                        product.updateWithConfirmedNamePriceAndAvailability(p.getName(), p.getPrice(), p.isAvailable());
                }));
        restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())));
        return restaurant;
    }

    private boolean isOutboxMessageProcessed(RestaurantApprovalRequest restaurantApprovalRequest) {
        Optional<OrderOutboxMessage> orderOutboxMessage = orderOutboxHelper
                .getCompletedOrderOutboxMessageBySagaIdAndOutboxStatus(UUID
                        .fromString(restaurantApprovalRequest.getSagaId()), OutboxStatus.COMPLETED);
        return orderOutboxMessage.isPresent();
    }
}
