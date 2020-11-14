package org.mephi.homework.timofeev.kafkaignite.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowAbroadTrips;
import org.mephi.homework.timofeev.kafkaignite.properties.KafkaAppProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowSalary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Класс конфигурации Kafka
 */
@Configuration
@EnableKafka
@RequiredArgsConstructor
@EnableConfigurationProperties(value = KafkaAppProperties.class)
public class KafkaAppConfig {

    private final KafkaAppProperties kafkaAppProperties;

    /**
     * Бин создания KafkaAdmin клиента
     * @return KafkaAdmin объект
     */
    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAppProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    /**
     * Бин создания (если не существует) топика для CitizenRowSalary входных данных.
     * Количество партиций будет устновлено по умолчанию в 1, количество реплик 1 (since kafka 2.6)
     * @return NewTopic объект
     */
    @Bean
    public NewTopic topicSalary() {
        return TopicBuilder.name(kafkaAppProperties.getSalaryTopic())
                .build();
    }

    /**
     * Бин создания (если не существует) топика для CitizenRowAbroadTrips входных данных.
     * Количество партиций будет устновлено по умолчанию в 1, количество реплик 1 (since kafka 2.6)
     * @return NewTopic объект
     */
    @Bean
    public NewTopic topicTrips() {
        return TopicBuilder.name(kafkaAppProperties.getTripsTopic())
                .build();
    }

    /**
     * Бин создания ProducerFactory для конфигурации записи данных CitizenRowSalary в Kafka
     * @return ProducerFactory<Long, CitizenRowSalary> объект
     */
    @Bean
    public ProducerFactory<Long, CitizenRowSalary> producerFactoryCitizenRowSalary() {
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
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAppProperties.getBootstrapServers());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaAppProperties.getConsumerGroupId());
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
