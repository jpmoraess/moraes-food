package br.com.moraesit.commons.kafka.producer.service.impl;

import br.com.moraesit.commons.kafka.producer.exception.KafkaProducerException;
import br.com.moraesit.commons.kafka.producer.service.KafkaProducer;
import jakarta.annotation.PreDestroy;
import org.apache.avro.specific.SpecificRecordBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Component
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {

    private final Logger logger = LoggerFactory.getLogger(KafkaProducerImpl.class);

    private final KafkaTemplate<K, V> kafkaTemplate;

    public KafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topic, K key, V message, BiConsumer<SendResult<K, V>, Throwable> callback) {
        logger.info("Sending message={} to topic={}", message, topic);
        try {
            CompletableFuture<SendResult<K, V>> kafkaSendResultFuture = kafkaTemplate.send(topic, key, message);
            kafkaSendResultFuture.whenComplete(callback);
        } catch (KafkaException e) {
            logger.error("Error on kafka producer with key: {}, message: {} and exception: {}", key, message, e.getMessage());
            throw new KafkaProducerException("Error on kafka producer with key: " + key + ", message: " + message + " and exception: " + e.getMessage());
        }
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            logger.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }
}
