package org.mephi.homework.timofeev.kafkaignite.repository;

import org.apache.ignite.springdata22.repository.IgniteRepository;
import org.apache.ignite.springdata22.repository.config.Query;
import org.apache.ignite.springdata22.repository.config.RepositoryConfig;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowAbroadTrips;

import java.util.UUID;

/**
 * Просто чтобы было для примера на будущее*/
@RepositoryConfig(cacheName = "app-cache")
public interface CitizenRowAbroadTripsRepository extends IgniteRepository<CitizenRowAbroadTrips, UUID> {
    @Query("passportId = ?")
    CitizenRowAbroadTrips getCitizenRowAbroadTripsByPassportId(UUID passportId);
}
