package org.mephi.homework.timofeev.kafkaignite.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * POJO класс выводимой статистики о гражданах
 */
@Data
@AllArgsConstructor
public class StatisticRow {
    /**
     * Возраст
     */
    private Integer age;
    /**
     * Среднее количество заграничных поездок
     */
    private Integer averageNumberTrips;
    /**
     * Средняя заработная плата
     */
    private Long averageSalary;
}
