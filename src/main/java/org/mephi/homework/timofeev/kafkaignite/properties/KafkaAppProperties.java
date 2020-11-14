package org.mephi.homework.timofeev.kafkaignite.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties приложения для работы с Kafka
 */
@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaAppProperties {
    /**
     * Имя Kafka топика с первым типом входных данных
     */
    private String tripsTopic;
    /**
     * Имя Kafka топика со вторым типом входных данных
     */
    private String salaryTopic;
    /**
     * Id Kafka продьюсера с первым типом входных данных
     */
    private String producerTripsId;
    /**
     * Id Kafka продьюсера со вторым типом входных данных
     */
    private String producerSalaryId;
    /**
     * Id Kafka консьюмер группы
     */
    private String consumerGroupId;
    /**
     * Список Kafka брокеров
     */
    private String bootstrapServers;
}
