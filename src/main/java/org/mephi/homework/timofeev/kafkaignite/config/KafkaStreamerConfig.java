package org.mephi.homework.timofeev.kafkaignite.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.stream.kafka.KafkaStreamer;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowAbroadTrips;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowSalary;
import org.mephi.homework.timofeev.kafkaignite.properties.KafkaAppProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * Класс конфигурации стриминга данных из Kafka в Ignite
 */
@Configuration
@RequiredArgsConstructor
public class KafkaStreamerConfig {

    private final KafkaAppProperties kafkaAppProperties;
    private final ObjectMapper objectMapper;
    @Qualifier("consumerProperties")
    private final Properties consumerProperties;

    /**
     * Бин создания KafkaStreamer объекта для CitizenRowAbroadTrips данных
     * @param igniteDataStreamerTrips - ignite data streamer для стриминга CitizenRowAbroadTrips из Kafka
     * @param igniteInstance - запущенный инстанс игнайта
     * @return объект KafkaStreamer
     */
    @Bean(destroyMethod = "stop")
    public KafkaStreamer<UUID, CitizenRowAbroadTrips> kafkaStreamerTrips(IgniteDataStreamer<UUID, CitizenRowAbroadTrips> igniteDataStreamerTrips, Ignite igniteInstance) {
        KafkaStreamer<UUID, CitizenRowAbroadTrips> kafkaStreamer = new KafkaStreamer<>();
        kafkaStreamer.setIgnite(igniteInstance);
        kafkaStreamer.setStreamer(igniteDataStreamerTrips);
        kafkaStreamer.setTopic(Collections.singletonList(kafkaAppProperties.getTripsTopic()));
        kafkaStreamer.setThreads(1);
        kafkaStreamer.setConsumerConfig(consumerProperties);
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

    /**
     * Бин создания KafkaStreamer объекта для CitizenRowSalary данных
     * @param igniteDataStreamerSalary - ignite data streamer для стриминга CitizenRowSalary из Kafka
     * @param igniteInstance - запущенный инстанс игнайта
     * @return объект KafkaStreamer
     */
    @Bean(destroyMethod = "stop")
    public KafkaStreamer<UUID, CitizenRowSalary> kafkaStreamerSalary(IgniteDataStreamer<UUID, CitizenRowSalary> igniteDataStreamerSalary, Ignite igniteInstance) {
        KafkaStreamer<UUID, CitizenRowSalary> kafkaStreamer = new KafkaStreamer<>();
        kafkaStreamer.setIgnite(igniteInstance);
        kafkaStreamer.setStreamer(igniteDataStreamerSalary);
        kafkaStreamer.setTopic(Collections.singletonList(kafkaAppProperties.getSalaryTopic()));
        kafkaStreamer.setThreads(1);
        kafkaStreamer.setConsumerConfig(consumerProperties);
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
