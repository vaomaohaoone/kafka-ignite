package org.mephi.homework.timofeev.kafkaignite.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.ignite.cache.affinity.AffinityKeyMapped;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CitizenRowAbroadTrips implements Serializable {

    @QuerySqlField(index = true)
    private UUID passportId;

    @Max(100)
    @Min(18)
    @QuerySqlField(index = true)
    @AffinityKeyMapped
    private Integer age;

    @Max(100)
    @Min(0)
    @QuerySqlField(index = true)
    private Integer abroadTripCount;
}
