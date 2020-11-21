package org.mephi.homework.timofeev.kafkaignite.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ignite.cache.affinity.AffinityKeyMapped;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * POJO класс информации о гражданах второго типа
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitizenRowSalary implements Serializable {

    /**
     * Номер паспорта
     */
    @QuerySqlField(index = true)
    @AffinityKeyMapped
    private UUID passportId;

    /**
     * Месяц
     */
    @Max(12)
    @Min(1)
    @QuerySqlField(index = true)
    private Integer month;

    /**
     * Зарплата
     */
    @Max(1000000)
    @Min(0)
    @QuerySqlField(index = true)
    private Long salary;
}
