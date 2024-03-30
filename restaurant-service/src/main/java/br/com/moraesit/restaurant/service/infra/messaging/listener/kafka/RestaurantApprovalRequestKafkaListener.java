package br.com.moraesit.restaurant.service.infra.messaging.listener.kafka;

import br.com.moraesit.commons.domain.events.payload.OrderApprovalEventPayload;
import br.com.moraesit.commons.kafka.consumer.KafkaConsumer;
import br.com.moraesit.commons.messaging.DebeziumOp;
import br.com.moraesit.restaurant.service.application.exception.RestaurantApplicationServiceException;
import br.com.moraesit.restaurant.service.application.ports.input.message.listener.RestaurantApprovalRequestMessageListener;
import br.com.moraesit.restaurant.service.domain.exception.RestaurantNotFoundException;
import br.com.moraesit.restaurant.service.infra.messaging.mapper.RestaurantMessagingMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import debezium.order.restaurant_approval_outbox.Envelope;
import debezium.order.restaurant_approval_outbox.Value;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class RestaurantApprovalRequestKafkaListener implements KafkaConsumer<Envelope> {

    private final ObjectMapper objectMapper;
    private final RestaurantApprovalRequestMessageListener restaurantApprovalRequestMessageListener;

    public RestaurantApprovalRequestKafkaListener(ObjectMapper objectMapper, RestaurantApprovalRequestMessageListener restaurantApprovalRequestMessageListener) {
        this.objectMapper = objectMapper;
        this.restaurantApprovalRequestMessageListener = restaurantApprovalRequestMessageListener;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}", topics = "${restaurant-service.restaurant-approval-request-topic-name}")
    public void receive(@Payload List<Envelope> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of restaurant approval requests received!",
                messages.stream().filter(m -> m.getBefore() == null && DebeziumOp.CREATE.getValue().equals(m.getOp())).toList().size());
        messages.forEach(avroModel -> {
            if (avroModel.getBefore() == null && DebeziumOp.CREATE.getValue().equals(avroModel.getOp())) {
                Value restaurantApprovalRequestAvroModel = avroModel.getAfter();
                OrderApprovalEventPayload orderApprovalEventPayload = getOrderEventPayload(restaurantApprovalRequestAvroModel.getPayload(), OrderApprovalEventPayload.class);
                try {
                    log.info("Processing order approval for order id: {}", orderApprovalEventPayload.getOrderId());
                    restaurantApprovalRequestMessageListener.approveOrder(RestaurantMessagingMapper
                            .restaurantApprovalRequestAvroModelToRestaurantApprovalRequest(orderApprovalEventPayload, restaurantApprovalRequestAvroModel));
                } catch (DataAccessException e) {
                    SQLException sqlException = (SQLException) e.getRootCause();
                    if (sqlException != null && sqlException.getSQLState() != null && PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {
                        // NO-OP for unique constraint exception
                        log.error("Caught unique constraint violation exception with sql state: {} in " +
                                "RestaurantApprovalRequestKafkaListener for order id: {}", sqlException.getSQLState(), orderApprovalEventPayload.getOrderId());
                    } else {
                        throw new RestaurantApplicationServiceException("Throwing DataAccessException in RestaurantApprovalRequestKafkaListener: " + e.getMessage(), e);
                    }
                } catch (RestaurantNotFoundException e) {
                    // NO-OP for RestaurantNotFoundException
                    log.error("No restaurant found for restaurant id: {} and order id: {}", orderApprovalEventPayload.getRestaurantId(), orderApprovalEventPayload.getOrderId());
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
