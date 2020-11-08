package org.mephi.homework.timofeev.kafkaignite.utils;

import org.apache.commons.lang3.RandomUtils;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowAbroadTrips;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowSalary;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public final class AppRandomUtils {

    public static Set<Integer> generateRandomMonthList() {
        Set<Integer> monthList = new LinkedHashSet<>();
        int rowsCount = RandomUtils.nextInt(0, 13);
        while (monthList.size() < rowsCount) {
            monthList.add(RandomUtils.nextInt(0, 12) + 1);
        }
        return monthList;
    }

    public static UUID generateRandomPassportId() {
        return UUID.randomUUID();
    }

    public static Boolean abroadTripsNeeded() {
        return RandomUtils.nextBoolean();
    }

    public static CitizenRowAbroadTrips generateCitizenRowAbroadTrips(UUID passportId) {
        return new CitizenRowAbroadTrips(passportId, RandomUtils.nextInt(18, 101), RandomUtils.nextInt(0, 101));
    }

    public static CitizenRowSalary generateCitizenRowSalary(UUID passportId, int month) {
        return new CitizenRowSalary(passportId, month, RandomUtils.nextLong(0, 1000001));
    }

}
