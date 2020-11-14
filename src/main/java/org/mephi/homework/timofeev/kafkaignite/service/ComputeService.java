package org.mephi.homework.timofeev.kafkaignite.service;

import lombok.RequiredArgsConstructor;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.mephi.homework.timofeev.kafkaignite.data.StatisticRow;
import org.mephi.homework.timofeev.kafkaignite.properties.IgniteAppProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Сервис подсчёта статистики о гражданах в зависимости от возраста
 */
@Service
@RequiredArgsConstructor
public class ComputeService {
    private final Ignite igniteInstance;
    private final IgniteAppProperties igniteAppProperties;

    /**
     * Метод подсчёта статистики с помощью Ignite compute
     * @return список данных по возрастам граждан
     */
    public List<StatisticRow> computeStatistic() {
        ArrayList<FieldsQueryCursor<List<?>>> currentResult = new ArrayList<>(igniteInstance.compute().broadcast(
                () -> {
                    IgniteCache appCache = Ignition.localIgnite().cache(igniteAppProperties.getCacheName());
                    String sql = "SELECT t1.AGE, AVERAGENUMBERTRIPS, AVERAGESALARY FROM (SELECT AGE, AVG(ABROADTRIPCOUNT) " +
                            "AS AVERAGENUMBERTRIPS FROM CITIZENROWABROADTRIPS GROUP BY AGE) AS t1 INNER JOIN\n" +
                            "(SELECT AGE, AVG(SALARY) AS AVERAGESALARY FROM CITIZENROWABROADTRIPS AS a INNER JOIN CITIZENROWSALARY " +
                            "AS b ON a.PASSPORTID = b.PASSPORTID GROUP BY AGE) AS t2 \n" +
                            "ON t1.AGE = t2.AGE;";
                    FieldsQueryCursor<List<?>> fieldsQueryCursor = appCache.query(new SqlFieldsQuery(sql).setLocal(true));
                    return fieldsQueryCursor;
                }
        ));
        //get(0) потому что нода одна
        return currentResult.get(0).getAll().stream().map(
                row ->
                        new StatisticRow(
                                (Integer) row.get(0),
                                (Integer) row.get(1),
                                (Long) row.get(2)
                        )
        ).collect(Collectors.toList());
    }

}
