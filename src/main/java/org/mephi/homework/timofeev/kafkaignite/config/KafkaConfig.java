package org.mephi.homework.timofeev.kafkaignite.config;

import lombok.RequiredArgsConstructor;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowAbroadTrips;
import org.mephi.homework.timofeev.kafkaignite.properties.AppProperties;
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

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfig {

    private final AppProperties appProperties;

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, appProperties.getKafkaBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topicSalary() {
        return TopicBuilder.name(appProperties.getKafkaSalaryTopic())
                .build();
    }

    @Bean
    public NewTopic topicTrips() {
        return TopicBuilder.name(appProperties.getKafkaTripsTopic())
                .build();
    }

    @Bean
    public ProducerFactory<Long, CitizenRowSalary> producerFactoryCitizenRowSalary() {
        return new DefaultKafkaProducerFactory<>(producerConfigs(appProperties.getKafkaProducerSalaryId()));
    }

    @Bean
    public ProducerFactory<Long, CitizenRowAbroadTrips> producerFactoryCitizenRowAbroadTrips() {
        return new DefaultKafkaProducerFactory<>(producerConfigs(appProperties.getKafkaProducerTripsId()));
    }

    @Bean
    public KafkaTemplate<Long, CitizenRowSalary> kafkaTemplateCitizenRowSalary() {
        return new KafkaTemplate<Long, CitizenRowSalary>(producerFactoryCitizenRowSalary());
    }

    @Bean
    public KafkaTemplate<Long, CitizenRowAbroadTrips> kafkaTemplateCitizenRowAbroadTrips() {
        return new KafkaTemplate<Long, CitizenRowAbroadTrips>(producerFactoryCitizenRowAbroadTrips());
    }

    private Map<String, Object> producerConfigs(String clientId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appProperties.getKafkaBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        return props;
    }
}
