package org.mephi.homework.timofeev.kafkaignite.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties приложения для работы с Ignite
 */
@ConfigurationProperties(prefix = "ignite")
@Data
public class IgniteAppProperties {
    /**
     * Имя кеша
     */
    private String cacheName;
    /**
     * Путь persistence хранилища
     */
    private String storagePath;
    /**
     * Интервал стриминга из Kafka в Ignite
     */
    private Long streamerFlushFrequency;
}
