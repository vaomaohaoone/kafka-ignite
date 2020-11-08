package org.mephi.homework.timofeev.kafkaignite.properties;

import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Getter
public class KafkaConsumerProperties {

    private final Properties properties;

    public KafkaConsumerProperties(@Value("${app.kafka.bootstrap-servers}") String kafkaBootstrapServers, @Value("${app.kafka.consumer.group-id}") String kafkaConsumerGroupId) {
        this.properties = new Properties();
        this.properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        this.properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        this.properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        this.properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerGroupId);
    }
}
