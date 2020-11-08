package org.mephi.homework.timofeev.kafkaignite.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.stream.kafka.KafkaStreamer;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowAbroadTrips;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowSalary;
import org.mephi.homework.timofeev.kafkaignite.properties.AppProperties;
import org.mephi.homework.timofeev.kafkaignite.properties.KafkaConsumerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@RequiredArgsConstructor
public class KafkaStreamerConfig {

    private final AppProperties appProperties;

    private final ObjectMapper objectMapper;

    private final KafkaConsumerProperties kafkaConsumerProperties;

    @Bean(destroyMethod = "stop")
    public KafkaStreamer<UUID, CitizenRowAbroadTrips> kafkaStreamerTrips(IgniteDataStreamer<UUID, CitizenRowAbroadTrips> igniteDataStreamerTrips, Ignite igniteInstance) {
        KafkaStreamer<UUID, CitizenRowAbroadTrips> kafkaStreamer = new KafkaStreamer<>();
        kafkaStreamer.setIgnite(igniteInstance);
        kafkaStreamer.setStreamer(igniteDataStreamerTrips);
        kafkaStreamer.setTopic(Collections.singletonList(appProperties.getKafkaTripsTopic()));
        kafkaStreamer.setThreads(1);
        kafkaStreamer.setConsumerConfig(kafkaConsumerProperties.getProperties());
        kafkaStreamer.setSingleTupleExtractor(msg -> {
            try {
                CitizenRowAbroadTrips value = objectMapper.readValue(msg.value().toString(), CitizenRowAbroadTrips.class);
                return new AbstractMap.SimpleEntry<>(value.getPassportId(), value);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException(e.toString());
            }
        });
        return kafkaStreamer;
    }

    @Bean(destroyMethod = "stop")
    public KafkaStreamer<UUID, CitizenRowSalary> kafkaStreamerSalary(IgniteDataStreamer<UUID, CitizenRowSalary> igniteDataStreamerSalary, Ignite igniteInstance) {
        KafkaStreamer<UUID, CitizenRowSalary> kafkaStreamer = new KafkaStreamer<>();
        kafkaStreamer.setIgnite(igniteInstance);
        kafkaStreamer.setStreamer(igniteDataStreamerSalary);
        kafkaStreamer.setTopic(Collections.singletonList(appProperties.getKafkaSalaryTopic()));
        kafkaStreamer.setThreads(1);
        kafkaStreamer.setConsumerConfig(kafkaConsumerProperties.getProperties());
        kafkaStreamer.setSingleTupleExtractor(msg -> {
            try {
                CitizenRowSalary value = objectMapper.readValue(msg.value().toString(), CitizenRowSalary.class);
                return new AbstractMap.SimpleEntry<>(UUID.randomUUID(), value);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException(e.toString());
            }
        });
        return kafkaStreamer;
    }

}
