package br.com.moraesit.order.service.application;

import br.com.moraesit.commons.outbox.OutboxStatus;
import br.com.moraesit.order.service.application.dto.create.CreateOrderInput;
import br.com.moraesit.order.service.application.dto.create.CreateOrderOutput;
import br.com.moraesit.order.service.application.mapper.OrderDataMapper;
import br.com.moraesit.order.service.application.outbox.helper.payment.PaymentOutboxHelper;
import br.com.moraesit.order.service.domain.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class CreateOrderHandler {
    private final Logger logger = LoggerFactory.getLogger(CreateOrderHandler.class);

    private final OrderSagaHelper orderSagaHelper;
    private final CreateOrderHelper createOrderHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;

    public CreateOrderHandler(OrderSagaHelper orderSagaHelper, CreateOrderHelper createOrderHelper, PaymentOutboxHelper paymentOutboxHelper) {
        this.orderSagaHelper = orderSagaHelper;
        this.createOrderHelper = createOrderHelper;
        this.paymentOutboxHelper = paymentOutboxHelper;
    }

    @Transactional
    public CreateOrderOutput createOrder(CreateOrderInput input) {
        OrderCreatedEvent orderCreatedEvent = createOrderHelper.persistOrder(input);
        logger.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        CreateOrderOutput createOrderOutput = OrderDataMapper
                .orderToCreateOrderOutput(orderCreatedEvent.getOrder(), "Order created successfully!");

        paymentOutboxHelper.saveOrderPaymentOutboxMessage(
                OrderDataMapper.orderCreatedEventToOrderPaymentEventPayload(orderCreatedEvent),
                orderCreatedEvent.getOrder().getOrderStatus(),
                orderSagaHelper.orderStatusToSagaStatus(orderCreatedEvent.getOrder().getOrderStatus()),
                OutboxStatus.STARTED,
                UUID.randomUUID()
        );

        logger.info("Returning CreateOrderOutput with order id: {}", orderCreatedEvent.getOrder().getId().getValue());
        return createOrderOutput;
    }
}
