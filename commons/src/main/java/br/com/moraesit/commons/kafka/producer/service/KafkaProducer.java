package br.com.moraesit.commons.kafka.producer.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.support.SendResult;

import java.io.Serializable;
import java.util.function.BiConsumer;

public interface KafkaProducer<K extends Serializable, V extends SpecificRecordBase> {

    void send(String topic, K key, V message, BiConsumer<SendResult<K, V>, Throwable> callback);
}
