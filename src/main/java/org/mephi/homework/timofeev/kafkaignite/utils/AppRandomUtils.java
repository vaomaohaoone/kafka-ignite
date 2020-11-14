package org.mephi.homework.timofeev.kafkaignite.utils;

import org.apache.commons.lang3.RandomUtils;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowAbroadTrips;
import org.mephi.homework.timofeev.kafkaignite.data.CitizenRowSalary;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Утилита генерации данных
 */
public final class AppRandomUtils {

    /**
     * @return сгенерированный набор месяцев, в которые будут данные по зарплате гражданина
     */
    public static Set<Integer> generateRandomMonthList() {
        Set<Integer> monthList = new LinkedHashSet<>();
        int rowsCount = RandomUtils.nextInt(0, 13);
        while (monthList.size() < rowsCount) {
            monthList.add(RandomUtils.nextInt(0, 12) + 1);
        }
        return monthList;
    }

    /**
     * @return сгенерированный номер паспорта
     */
    public static UUID generateRandomPassportId() {
        return UUID.randomUUID();
    }

    /**
     * @return признак наличия записи о возрасте и количестве заграничных поездок гражданина
     */
    public static Boolean abroadTripsNeeded() {
        return RandomUtils.nextBoolean();
    }

    /**
     * @param passportId - номер паспорта
     * @return CitizenRowAbroadTrips запись
     */
    public static CitizenRowAbroadTrips generateCitizenRowAbroadTrips(UUID passportId) {
        return new CitizenRowAbroadTrips(passportId, RandomUtils.nextInt(18, 101), RandomUtils.nextInt(0, 101));
    }

    /**
     * @param passportId - номер паспорта
     * @param month - месяц
     * @return CitizenRowSalary запись
     */
    public static CitizenRowSalary generateCitizenRowSalary(UUID passportId, int month) {
        return new CitizenRowSalary(passportId, month, RandomUtils.nextLong(0, 1000001));
    }

}
