package org.mephi.homework.timofeev.kafkaignite.config;

import lombok.RequiredArgsConstructor;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowAbroadTrips;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowSalary;
import org.mephi.homework.timofeev.kafkaignite.properties.IgniteAppProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * Класс конфигурации Ignite
 */
@Configuration
@EnableConfigurationProperties(value = IgniteAppProperties.class)
@RequiredArgsConstructor
public class IgniteAppConfig {

    private final IgniteAppProperties igniteAppProperties;

    /**
     * Бин создания Ignite инстанса
     * @return Ignite
     */
    @Bean
    public Ignite igniteInstance() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        DataStorageConfiguration dataStorageConfiguration = new DataStorageConfiguration();
        dataStorageConfiguration.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
        dataStorageConfiguration.setStoragePath(igniteAppProperties.getStoragePath());
        cfg.setDataStorageConfiguration(dataStorageConfiguration);
        CacheConfiguration cacheConfiguration = new CacheConfiguration<>(igniteAppProperties.getCacheName());
        cacheConfiguration.setIndexedTypes(UUID.class, CitizenRowAbroadTrips.class);
        cacheConfiguration.setIndexedTypes(UUID.class, CitizenRowSalary.class);
        cfg.setCacheConfiguration(cacheConfiguration);
        Ignite ignite = Ignition.start(cfg);
        ignite.active(true);
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
