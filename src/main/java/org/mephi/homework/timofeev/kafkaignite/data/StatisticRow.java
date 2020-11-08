package org.mephi.homework.timofeev.kafkaignite.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticRow {
    private Integer age;
    private Integer averageNumberTrips;
    private Long averageSalary;
}
