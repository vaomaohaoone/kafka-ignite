package org.mephi.homework.timofeev.kafkaignite.service.listener;

import lombok.RequiredArgsConstructor;
import org.apache.ignite.stream.kafka.KafkaStreamer;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowAbroadTrips;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowSalary;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Сервис старта поллинга данных из kafka в ignite
 */
@Component
@RequiredArgsConstructor
public class KafkaListener {

    private final KafkaStreamer<UUID, CitizenRowSalary> kafkaStreamerSalary;
    private final KafkaStreamer<UUID, CitizenRowAbroadTrips> kafkaStreamerTrips;

    /**
     * Метод старта поллинга топиков с данными, начало операций при старте приложения
     */
    @EventListener(ApplicationStartedEvent.class)
    public void startKafkaStreamers() {
        kafkaStreamerSalary.start();
        kafkaStreamerTrips.start();
    }
}
