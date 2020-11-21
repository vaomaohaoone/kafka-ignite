package org.mephi.homework.timofeev.kafkaignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.events.CacheEvent;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.ignite.stream.kafka.KafkaStreamer;

import static org.apache.ignite.events.EventType.EVT_CACHE_OBJECT_PUT;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mephi.homework.timofeev.kafkaignite.config.*;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowAbroadTrips;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowSalary;
import org.mephi.homework.timofeev.kafkaignite.data.JoinedRow;
import org.mephi.homework.timofeev.kafkaignite.data.StatisticRow;
import org.mephi.homework.timofeev.kafkaignite.properties.IgniteAppProperties;
import org.mephi.homework.timofeev.kafkaignite.properties.KafkaAppProperties;
import org.mephi.homework.timofeev.kafkaignite.service.ComputeService;
import org.mephi.homework.timofeev.kafkaignite.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mephi.homework.timofeev.kafkaignite.utils.AppRandomUtils.*;


/**
 * End-to-end тестирование приложения
 * Архитектура:
 * Kafka - embedded kafka
 * Ignite - In-memory cache
 * */
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:3333",
                "port=3333"
        },
        topics = {"trips-topic", "salary-topic"}
)
@SpringBootTest(
        classes = {
                IgniteTestConfig.class,
                KafkaTestConfig.class,
                KafkaStreamerConfig.class,
                ObjectMapperConfig.class,
                IgniteAppProperties.class,
                KafkaAppProperties.class,
                ProducerService.class,
                ComputeService.class
        }
)
@DirtiesContext
public class TestApp {

    @Autowired
    private ProducerService producerService;
    @Autowired
    private ComputeService computeService;
    @Autowired
    private KafkaStreamer<UUID, CitizenRowSalary> kafkaStreamerSalary;
    @Autowired
    private KafkaStreamer<UUID, CitizenRowAbroadTrips> kafkaStreamerTrips;
    @Autowired
    private Ignite igniteInstance;

    // Количество граждан
    @Value("${batch.num}")
    private Integer batchNum;
    // Время работы kafka - стримера
    @Value("${listener.work-time}")
    private Integer listenerWorkTime;
    @Value("${ignite.cache-name}")
    private String cacheName;

    private List<CitizenRowSalary> citizenRowSalariesList;
    private List<CitizenRowAbroadTrips> citizenRowAbroadTripsList;

    /**
     * Отпрака данных в embedded Kafka
     * */
    @BeforeEach
    public void before() {
        citizenRowSalariesList = new ArrayList<>();
        citizenRowAbroadTripsList = new ArrayList<>();
        for (int i = 0; i < batchNum; i++) {
            {
                UUID passportId = generateRandomPassportId();
                Set<Integer> monthList = generateRandomMonthList();
                monthList.forEach(month -> {
                    CitizenRowSalary citizenRowSalary = generateCitizenRowSalary(passportId, month);
                    producerService.sendCitizenRowSalary(citizenRowSalary);
                    citizenRowSalariesList.add(citizenRowSalary);
                });
                if (abroadTripsNeeded()) {
                    CitizenRowAbroadTrips citizenRowAbroadTrips = generateCitizenRowAbroadTrips(passportId);
                    producerService.sendCitizenRowRowAbroadTrips(citizenRowAbroadTrips);
                    citizenRowAbroadTripsList.add(citizenRowAbroadTrips);
                }
            }
        }
    }

    @Test
    public void testCompute() throws InterruptedException {
        //
        kafkaStreamerSalary.start();
        kafkaStreamerTrips.start();

        //старт ожидания данных в ignite cache
        waitKafkaListener();
        //конец ожидания

        //подсчёт ожидаемых данных по средней зарплате
        Map<Integer, Long> expectedMapAverageSalary = getExpectedMapAverageSalary();

        //подсчёт ожидаемых данных по среднему количеству поездок
        Map<Integer, Long> expectedMapAverageTrips = getExpectedMapAverageTrips();

        //получение актуальных данных
        List<StatisticRow> actualStatistic = computeService.computeStatistic();

        //форматирование актуальных данных для сравнения с ожидаемыми
        Map<Integer, Long> resultStatisticByTrips = actualStatistic.stream().collect(
                Collectors.toMap(StatisticRow::getAge, statisticRow -> Long.valueOf(statisticRow.getAverageNumberTrips()))
        );

        Map<Integer, Long> resultStatisticBySalary = actualStatistic.stream().collect(
                Collectors.toMap(StatisticRow::getAge, StatisticRow::getAverageSalary)
        );

        //сравнение
        assertEquals(expectedMapAverageTrips.keySet(), resultStatisticByTrips.keySet());
        assertEquals(expectedMapAverageSalary.keySet(), resultStatisticBySalary.keySet());
        assertTrue(areEqualsAverageValues(expectedMapAverageSalary, resultStatisticBySalary));
        assertTrue(areEqualsAverageValues(expectedMapAverageTrips, resultStatisticByTrips));
    }

    /**
     * Получение ожидаемого результата по подсчёту среднего количества поездок
     * */
    private Map<Integer, Long> getExpectedMapAverageTrips() {
        return citizenRowAbroadTripsList.stream()
                .collect(Collectors.groupingBy(CitizenRowAbroadTrips::getAge,
                        Collectors.collectingAndThen(Collectors.averagingInt(CitizenRowAbroadTrips::getAbroadTripCount), Double::longValue)));
    }

    /**
     * Получение ожидаемого результата по подсчёту средней зарплаты
     * */
    private Map<Integer, Long> getExpectedMapAverageSalary() {
        Map<UUID, Integer> passportAgeMap = citizenRowAbroadTripsList.stream()
                .collect(Collectors.toMap(CitizenRowAbroadTrips::getPassportId, CitizenRowAbroadTrips::getAge));

        return citizenRowSalariesList.stream()
                .map(citizenRowSalary -> new JoinedRow(citizenRowSalary.getPassportId(), passportAgeMap.get(citizenRowSalary.getPassportId()), citizenRowSalary.getSalary()))
                .filter(joinedRow -> joinedRow.getAge() != null)
                .collect(Collectors.groupingBy(JoinedRow::getAge,
                        Collectors.collectingAndThen(Collectors.averagingLong(JoinedRow::getSalary), Double::longValue)));
    }

    /**
     * Метод ожидания работы kafka-listener*/
    private void waitKafkaListener() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        IgniteBiPredicate<UUID, CacheEvent> listener = new IgniteBiPredicate<UUID, CacheEvent>() {
            @Override
            public boolean apply(UUID uuid, CacheEvent evt) {
                latch.countDown();

                return true;
            }
        };

        igniteInstance.events(igniteInstance.cluster().forCacheNodes(cacheName)).remoteListen(listener, null, EVT_CACHE_OBJECT_PUT);
        latch.await(listenerWorkTime, TimeUnit.SECONDS);
    }

    /**
     * Метод нужен для сравнения средних зарплат и количества поездок с погрешностью +-1
     */
    private boolean areEqualsAverageValues(Map<Integer, Long> first, Map<Integer, Long> second) {
        return first.entrySet().stream()
                .allMatch(
                        entry ->
                                entry.getValue().equals(second.get(entry.getKey())) ||
                                        entry.getValue().equals(second.get(entry.getKey()) + 1) ||
                                        entry.getValue().equals(second.get(entry.getKey()) - 1)

                );
    }

}
