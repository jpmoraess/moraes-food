package br.com.moraesit.payment.service.infra.messaging.listener.kafka;

import br.com.moraesit.commons.domain.events.payload.OrderPaymentEventPayload;
import br.com.moraesit.commons.domain.valueobject.PaymentOrderStatus;
import br.com.moraesit.commons.kafka.consumer.KafkaSingleItemConsumer;
import br.com.moraesit.commons.messaging.DebeziumOp;
import br.com.moraesit.payment.service.application.ports.input.message.listener.PaymentRequestMessageListener;
import br.com.moraesit.payment.service.domain.exception.PaymentNotFoundException;
import br.com.moraesit.payment.service.infra.messaging.mapper.PaymentMessagingMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import debezium.order.payment_outbox.Envelope;
import debezium.order.payment_outbox.Value;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Slf4j
@Component
public class PaymentRequestKafkaListener implements KafkaSingleItemConsumer<Envelope> {

    private final ObjectMapper objectMapper;
    private final PaymentRequestMessageListener paymentRequestMessageListener;

    public PaymentRequestKafkaListener(ObjectMapper objectMapper, PaymentRequestMessageListener paymentRequestMessageListener) {
        this.objectMapper = objectMapper;
        this.paymentRequestMessageListener = paymentRequestMessageListener;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${payment-service.payment-request-topic-name}")
    public void receive(@Payload Envelope message,
                        @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset) {

        if (message.getBefore() == null && DebeziumOp.CREATE.getValue().equals(message.getOp())) {
            Value paymentRequest = message.getAfter();
            OrderPaymentEventPayload orderPaymentEventPayload =
                    getOrderEventPayload(paymentRequest.getPayload(), OrderPaymentEventPayload.class);
            try {
                if (PaymentOrderStatus.PENDING.name().equals(orderPaymentEventPayload.getPaymentOrderStatus())) {
                    log.info("Processing payment for order id: {}", orderPaymentEventPayload.getOrderId());
                    paymentRequestMessageListener.completePayment(PaymentMessagingMapper
                            .paymentRequestAvroModelToPaymentRequest(orderPaymentEventPayload, paymentRequest));
                } else if (PaymentOrderStatus.CANCELLED.name().equals(orderPaymentEventPayload.getPaymentOrderStatus())) {
                    log.info("Cancelling payment for order id: {}", orderPaymentEventPayload.getOrderId());
                    paymentRequestMessageListener.cancelPayment(PaymentMessagingMapper
                            .paymentRequestAvroModelToPaymentRequest(orderPaymentEventPayload, paymentRequest));
                }
            } catch (DataAccessException e) {
                SQLException exception = (SQLException) e.getRootCause();
                if (exception != null && exception.getSQLState() != null && PSQLState.UNIQUE_VIOLATION.getState().equals(exception.getSQLState())) {
                    // NO-OP for unique constraint exception
                    log.error("Caught unique constraint exception with sql state: {} in PaymentRequestKafkaListener for order id: {}", exception.getSQLState(), orderPaymentEventPayload.getOrderId());
                }
            } catch (PaymentNotFoundException e) {

            } catch (Exception e) {

            }
        }
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
