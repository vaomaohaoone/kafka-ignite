package org.mephi.homework.timofeev.kafkaignite.config;

import lombok.RequiredArgsConstructor;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowAbroadTrips;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowSalary;
import org.mephi.homework.timofeev.kafkaignite.properties.IgniteAppProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.UUID;

/**
 * Тестовый класс конфигурации Ignite
 * */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = IgniteAppProperties.class)
public class IgniteTestConfig {

    private final IgniteAppProperties igniteAppProperties;

    @Bean
    @Primary
    public Ignite igniteInstance() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        CacheConfiguration cacheConfiguration = new CacheConfiguration<>(igniteAppProperties.getCacheName());
        cacheConfiguration.setIndexedTypes(UUID.class, CitizenRowAbroadTrips.class);
        cacheConfiguration.setIndexedTypes(UUID.class, CitizenRowSalary.class);
        cfg.setCacheConfiguration(cacheConfiguration);
        Ignite ignite = Ignition.start(cfg);
        return ignite;
    }

    /**
     * Бин создания IgniteDataStreamer для CitizenRowAbroadTrips входных данных
     * @param igniteInstance - запущенный инстанс игнайта
     * @return IgniteDataStreamer<UUID, CitizenRowAbroadTrips>
     */
    @Bean(destroyMethod = "close")
    public IgniteDataStreamer<UUID, CitizenRowAbroadTrips> igniteDataStreamerTrips(Ignite igniteInstance) {
        IgniteDataStreamer<UUID, CitizenRowAbroadTrips> streamer = igniteInstance.dataStreamer(igniteAppProperties.getCacheName());
        streamer.allowOverwrite(true);
        streamer.autoFlushFrequency(igniteAppProperties.getStreamerFlushFrequency());
        return streamer;
    }

    /**
     * Бин создания IgniteDataStreamer для CitizenRowSalary входных данных
     * @param igniteInstance - запущенный инстанс игнайта
     * @return IgniteDataStreamer<UUID, CitizenRowSalary>
     */
    @Bean(destroyMethod = "close")
    public IgniteDataStreamer<UUID, CitizenRowSalary> igniteDataStreamerSalary(Ignite igniteInstance) {
        IgniteDataStreamer<UUID, CitizenRowSalary> streamer = igniteInstance.dataStreamer(igniteAppProperties.getCacheName());
        streamer.allowOverwrite(true);
        streamer.autoFlushFrequency(igniteAppProperties.getStreamerFlushFrequency());
        return streamer;
    }
}
