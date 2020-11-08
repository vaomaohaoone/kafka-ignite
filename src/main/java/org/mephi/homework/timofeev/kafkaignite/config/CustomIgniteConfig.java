package org.mephi.homework.timofeev.kafkaignite.config;

import lombok.RequiredArgsConstructor;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.springdata22.repository.config.EnableIgniteRepositories;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowAbroadTrips;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowSalary;
import org.mephi.homework.timofeev.kafkaignite.properties.AppProperties;
import org.mephi.homework.timofeev.kafkaignite.repository.CitizenRowAbroadTripsRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@EnableIgniteRepositories(basePackageClasses = {CitizenRowAbroadTripsRepository.class})
@RequiredArgsConstructor
public class CustomIgniteConfig {

    private final AppProperties appProperties;

    @Bean
    public Ignite igniteInstance() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        CacheConfiguration cacheConfiguration = new CacheConfiguration<>(appProperties.getIgniteCacheName());
        cacheConfiguration.setIndexedTypes(UUID.class, CitizenRowAbroadTrips.class);
        cacheConfiguration.setIndexedTypes(UUID.class, CitizenRowSalary.class);
        cfg.setCacheConfiguration(cacheConfiguration);
        return Ignition.start(cfg);
    }

    @Bean(destroyMethod = "close")
    public IgniteDataStreamer<UUID, CitizenRowAbroadTrips> igniteDataStreamerTrips(Ignite igniteInstance) {
        IgniteDataStreamer<UUID, CitizenRowAbroadTrips> streamer = igniteInstance.dataStreamer(appProperties.getIgniteCacheName());
        streamer.allowOverwrite(true);
        streamer.autoFlushFrequency(appProperties.getIgniteStreamerFlushFrequency());
        return streamer;
    }

    @Bean(destroyMethod = "close")
    public IgniteDataStreamer<UUID, CitizenRowSalary> igniteDataStreamerSalary(Ignite igniteInstance) {
        IgniteDataStreamer<UUID, CitizenRowSalary> streamer = igniteInstance.dataStreamer(appProperties.getIgniteCacheName());
        streamer.allowOverwrite(true);
        streamer.autoFlushFrequency(appProperties.getIgniteStreamerFlushFrequency());
        return streamer;
    }

}
