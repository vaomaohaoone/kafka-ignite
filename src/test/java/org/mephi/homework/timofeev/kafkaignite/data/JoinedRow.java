package org.mephi.homework.timofeev.kafkaignite.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * Класс для промежуточных данных (используется для получения ожидаемых данных)
 * */
@Data
@AllArgsConstructor
public class JoinedRow {
    private UUID passportId;
    private Integer age;
    private Long salary;
}
