package org.mephi.homework.timofeev.kafkaignite.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowAbroadTrips;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowSalary;
import org.mephi.homework.timofeev.kafkaignite.properties.AppProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.mephi.homework.timofeev.kafkaignite.utils.AppRandomUtils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerService {
    private final KafkaTemplate<Long, CitizenRowSalary> kafkaTemplateCitizenRowSalary;
    private final KafkaTemplate<Long, CitizenRowAbroadTrips> kafkaTemplateCitizenRowAbroadTrips;
    private final ObjectMapper objectMapper;
    private final AppProperties appProperties;

    public void sendBatch(Integer num) {
        for (int i = 0; i < num; i ++)
            sendRows();
    }

    private void sendCitizenRowSalary(CitizenRowSalary citizenRowSalary) {
        log.info("<= sending CitizenRowSalary {}", writeValueAsString(citizenRowSalary));
        kafkaTemplateCitizenRowSalary.send(appProperties.getKafkaSalaryTopic(), citizenRowSalary);
    }

    private void sendCitizenRowRowAbroadTrips(CitizenRowAbroadTrips citizenRowAbroadTrips) {
        log.info("<= sending CitizenRowAbroadTrips {}", writeValueAsString(citizenRowAbroadTrips));
        kafkaTemplateCitizenRowAbroadTrips.send(appProperties.getKafkaTripsTopic(), citizenRowAbroadTrips);
    }

    private void sendRows() {
        UUID passportId = generateRandomPassportId();
        Set<Integer> monthList = generateRandomMonthList();
        monthList.forEach(month -> {
            sendCitizenRowSalary(generateCitizenRowSalary(passportId, month));
        });
        if (abroadTripsNeeded()) {
            sendCitizenRowRowAbroadTrips(generateCitizenRowAbroadTrips(passportId));
        }
    }

    private String writeValueAsString(Object row) {
        try {
            return objectMapper.writeValueAsString(row);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
