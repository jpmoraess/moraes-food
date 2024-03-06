package br.com.moraesit.order.service.application;

import br.com.moraesit.order.service.application.dto.create.CreateOrderInput;
import br.com.moraesit.order.service.application.dto.create.CreateOrderOutput;
import br.com.moraesit.order.service.application.mapper.OrderDataMapper;
import br.com.moraesit.order.service.application.ports.output.repository.CustomerRepository;
import br.com.moraesit.order.service.application.ports.output.repository.OrderRepository;
import br.com.moraesit.order.service.application.ports.output.repository.RestaurantRepository;
import br.com.moraesit.order.service.domain.OrderDomainService;
import br.com.moraesit.order.service.domain.entity.Customer;
import br.com.moraesit.order.service.domain.entity.Order;
import br.com.moraesit.order.service.domain.entity.Restaurant;
import br.com.moraesit.order.service.domain.event.OrderCreatedEvent;
import br.com.moraesit.order.service.domain.exception.OrderDomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
public class CreateOrderHandler {
    private final Logger logger = LoggerFactory.getLogger(CreateOrderHandler.class);

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;

    public CreateOrderHandler(OrderDomainService orderDomainService, OrderRepository orderRepository,
                              CustomerRepository customerRepository, RestaurantRepository restaurantRepository) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public CreateOrderOutput createOrder(CreateOrderInput input) {
        checkCustomer(input.getCustomerId());
        Restaurant restaurant = checkRestaurant(input);
        Order order = OrderDataMapper.createOrderInputToOrder(input);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        Order saved = save(order);
        logger.info("Order is created with id: {}", saved.getId().getValue());
        return OrderDataMapper.orderToCreateOrderOutput(saved, "Order created successfully!");
    }

    private Order save(Order order) {
        Order orderResult = orderRepository.save(order);
        if (orderResult == null) {
            logger.error("Could not saved order!");
            throw new OrderDomainException("Could not save order!");
        }
        logger.info("Order is saved with id: {}", orderResult.getId().getValue());
        return orderResult;
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customerOptional = customerRepository.findCustomer(customerId);
        if (customerOptional.isEmpty()) {
            logger.warn("Could not find customer with id: {}", customerId);
            throw new OrderDomainException("Could not find customer with id: " + customerId);
        }
    }

    private Restaurant checkRestaurant(CreateOrderInput input) {
        Restaurant restaurant = OrderDataMapper.createOrderInputToRestaurant(input);
        Optional<Restaurant> restaurantOptional = restaurantRepository.findRestaurantInformation(restaurant);
        if (restaurantOptional.isEmpty()) {
            logger.warn("Could not find restaurant with id: {}", input.getRestaurantId());
            throw new OrderDomainException("Could not find restaurant with id: " + input.getRestaurantId());
        }
        return restaurantOptional.get();
    }
}
