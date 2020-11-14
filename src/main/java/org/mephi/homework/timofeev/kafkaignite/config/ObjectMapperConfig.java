package org.mephi.homework.timofeev.kafkaignite.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Класс конфигурации ObjectMapper
 */
@Configuration
public class ObjectMapperConfig {

    /**
     * Бин создания ObjectMapper
     * @return ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules();
    }
}
