package org.mephi.homework.timofeev.kafkaignite.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@Getter
public class AppProperties {
    @Value("${app.kafka.trips-topic}")
    private String kafkaTripsTopic;
    @Value("${app.kafka.salary-topic}")
    private String kafkaSalaryTopic;
    @Value("${app.kafka.producer.trips-id}")
    private String kafkaProducerTripsId;
    @Value("${app.kafka.producer.salary-id}")
    private String kafkaProducerSalaryId;
    @Value("${app.kafka.consumer.group-id}")
    private String kafkaConsumerGroupId;
    @Value("${app.kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;
    @Value("${app.ignite.cache-name}")
    private String igniteCacheName;
    @Value("${app.ignite.streamer.flush-frequency}")
    private Integer igniteStreamerFlushFrequency;
}
