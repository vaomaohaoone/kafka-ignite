package org.mephi.homework.timofeev.kafkaignite.config;

import lombok.RequiredArgsConstructor;
import org.apache.ignite.stream.kafka.KafkaStreamer;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowAbroadTrips;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowSalary;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StartEventListener {

    private final KafkaStreamer<UUID, CitizenRowSalary> kafkaStreamerSalary;
    private final KafkaStreamer<UUID, CitizenRowAbroadTrips> kafkaStreamerTrips;

    @EventListener(ApplicationStartedEvent.class)
    public void startKafkaStreamers() {
        kafkaStreamerSalary.start();
        kafkaStreamerTrips.start();
    }
}
