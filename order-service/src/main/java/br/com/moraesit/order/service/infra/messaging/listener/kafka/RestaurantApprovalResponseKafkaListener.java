package br.com.moraesit.order.service.infra.messaging.listener.kafka;

import br.com.moraesit.commons.domain.events.payload.RestaurantOrderEventPayload;
import br.com.moraesit.commons.domain.valueobject.OrderApprovalStatus;
import br.com.moraesit.commons.kafka.consumer.KafkaConsumer;
import br.com.moraesit.commons.messaging.DebeziumOp;
import br.com.moraesit.order.service.application.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import br.com.moraesit.order.service.domain.exception.OrderNotFoundException;
import br.com.moraesit.order.service.infra.messaging.mapper.OrderMessagingMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import debezium.restaurant.order_outbox.Envelope;
import debezium.restaurant.order_outbox.Value;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<Envelope> {

    private final ObjectMapper objectMapper;
    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;

    public RestaurantApprovalResponseKafkaListener(ObjectMapper objectMapper, RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener) {
        this.objectMapper = objectMapper;
        this.restaurantApprovalResponseMessageListener = restaurantApprovalResponseMessageListener;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}", topics = "${order-service.restaurant-approval-response-topic-name}")
    public void receive(@Payload List<Envelope> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of restaurant approval responses received!",
                messages.stream().filter(message -> message.getBefore() == null &&
                        DebeziumOp.CREATE.getValue().equals(message.getOp())).toList().size());

        messages.forEach(avroModel -> {
            if (avroModel.getBefore() == null && DebeziumOp.CREATE.getValue().equals(avroModel.getOp())) {
                log.info("Incoming message in RestaurantApprovalResponseKafkaListener: {}", avroModel);
                Value restaurantApprovalResponseAvroModel = avroModel.getAfter();
                RestaurantOrderEventPayload restaurantOrderEventPayload =
                        getOrderEventPayload(restaurantApprovalResponseAvroModel.getPayload(), RestaurantOrderEventPayload.class);
                try {
                    if (OrderApprovalStatus.APPROVED.name().equals(restaurantOrderEventPayload.getOrderApprovalStatus())) {
                        log.info("Processing approved order for order id: {}",
                                restaurantOrderEventPayload.getOrderId());
                        restaurantApprovalResponseMessageListener.orderApproved(OrderMessagingMapper
                                .approvalResponseAvroModelToApprovalResponse(restaurantOrderEventPayload, restaurantApprovalResponseAvroModel));
                    } else if (OrderApprovalStatus.REJECTED.name().equals(restaurantOrderEventPayload.getOrderApprovalStatus())) {
                        log.info("Processing rejected order for order id: {}, with failure messages: {}",
                                restaurantOrderEventPayload.getOrderId(),
                                String.join(",", restaurantOrderEventPayload.getFailureMessages()));
                        restaurantApprovalResponseMessageListener.orderRejected(OrderMessagingMapper
                                .approvalResponseAvroModelToApprovalResponse(restaurantOrderEventPayload, restaurantApprovalResponseAvroModel));
                    }
                } catch (OptimisticLockingFailureException e) {
                    //NO-OP for optimistic lock. This means another thread finished the work, do not throw error to prevent reading the data from kafka again!
                    log.error("Caught optimistic locking exception in RestaurantApprovalResponseKafkaListener for order id: {}",
                            restaurantOrderEventPayload.getOrderId());
                } catch (OrderNotFoundException e) {
                    //NO-OP for OrderNotFoundException
                    log.error("No order found for order id: {}", restaurantOrderEventPayload.getOrderId());
                } catch (DataAccessException e) {
                    SQLException sqlException = (SQLException) e.getRootCause();
                    if (sqlException != null && sqlException.getSQLState() != null &&
                            PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {
                        //NO-OP for unique constraint exception
                        log.error("Caught unique constraint exception with sql state: {} " +
                                        "in RestaurantApprovalResponseKafkaListener for order id: {}",
                                sqlException.getSQLState(), restaurantOrderEventPayload.getOrderId());
                    }
                }
            }
        });
    }

    public <T> T getOrderEventPayload(String payload, Class<T> outputType) {
        try {
            return objectMapper.readValue(payload, outputType);
        } catch (JsonProcessingException e) {
            log.error("Could not read {} object!", outputType.getName(), e);
            throw new RuntimeException("Could not read " + outputType.getName() + " object!", e);
        }
    }
}
