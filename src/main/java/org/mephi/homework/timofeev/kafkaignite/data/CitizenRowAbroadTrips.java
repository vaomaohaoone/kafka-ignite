package org.mephi.homework.timofeev.kafkaignite.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ignite.cache.affinity.AffinityKeyMapped;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.UUID;

/**
 * POJO класс информации о гражданах первого типа
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitizenRowAbroadTrips implements Serializable {

    /**
     * Номер паспорта
     */
    @QuerySqlField(index = true)
    private UUID passportId;

    /**
     * Возраст
     */
    @Max(100)
    @Min(18)
    @QuerySqlField(index = true)
    @AffinityKeyMapped
    private Integer age;

    /**
     * Количество заграничных поездок
     */
    @Max(100)
    @Min(0)
    @QuerySqlField(index = true)
    private Integer abroadTripCount;
}
