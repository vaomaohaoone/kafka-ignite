package org.mephi.homework.timofeev.kafkaignite.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowAbroadTrips;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowSalary;
import org.mephi.homework.timofeev.kafkaignite.properties.KafkaAppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Тестовый класс конфигурации Kafka
 * */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = KafkaAppProperties.class)
public class KafkaTestConfig {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    // Autowire the kafka broker registered via @EmbeddedKafka
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    private final KafkaAppProperties kafkaAppProperties;


    @Bean
    public ProducerFactory<Long, org.mephi.homework.timofeev.kafkaignite.data.CitizenRowSalary> producerFactoryCitizenRowSalary() {
        return new DefaultKafkaProducerFactory<>(getProducerConfig(kafkaAppProperties.getProducerSalaryId()));
    }

    /**
     * Бин создания ProducerFactory для конфигурации записи данных CitizenRowAbroadTrips в Kafka
     * @return ProducerFactory<Long, CitizenRowAbroadTrips> объект
     */
    @Bean
    public ProducerFactory<Long, CitizenRowAbroadTrips> producerFactoryCitizenRowAbroadTrips() {
        return new DefaultKafkaProducerFactory<>(getProducerConfig(kafkaAppProperties.getProducerTripsId()));
    }

    /**
     * Бин создания KafkaTemplate для записи данных CitizenRowSalary в Kafka
     * @return KafkaTemplate<Long, CitizenRowSalary> объект
     */
    @Bean
    public KafkaTemplate<Long, CitizenRowSalary> kafkaTemplateCitizenRowSalary() {
        return new KafkaTemplate<Long, CitizenRowSalary>(producerFactoryCitizenRowSalary());
    }

    /**
     * Бин создания KafkaTemplate для записи данных CitizenRowAbroadTrips в Kafka
     * @return KafkaTemplate<Long, CitizenRowAbroadTrips> объект
     */
    @Bean
    public KafkaTemplate<Long, CitizenRowAbroadTrips> kafkaTemplateCitizenRowAbroadTrips() {
        return new KafkaTemplate<Long, CitizenRowAbroadTrips>(producerFactoryCitizenRowAbroadTrips());
    }

    /**
     * Бин создания конфигурации Kafka consumer
     * @return Properties объект
     */
    @Bean
    @Qualifier("consumerProperties")
    public Properties consumerProperties() {
        final Map<String, Object> consumerProps = KafkaTestUtils
                .consumerProps(kafkaAppProperties.getConsumerGroupId(), "true", embeddedKafka);
        // Since we're pre-sending the messages to test for, we need to read from start of topic
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // We need to match the ser/deser used in expected application config
        consumerProps
                .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps
                .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        Properties properties = new Properties();
        properties.putAll(consumerProps);
        return properties;
    }

    /**
     * Метод создания конфигурации Kafka producer
     * @param clientId - producer kafka id
     * @return - map с конфигурацией Kafka producer
     */
    private Map<String, Object> getProducerConfig(String clientId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAppProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        return props;
    }
}
