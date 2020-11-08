package org.mephi.homework.timofeev.kafkaignite.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.ignite.cache.affinity.AffinityKeyMapped;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CitizenRowSalary implements Serializable {

    @QuerySqlField(index = true)
    @AffinityKeyMapped
    private UUID passportId;

    @Max(12)
    @Min(1)
    @QuerySqlField(index = true)
    private Integer month;

    @Max(1000000)
    @Min(0)
    @QuerySqlField(index = true)
    private Long salary;
}
