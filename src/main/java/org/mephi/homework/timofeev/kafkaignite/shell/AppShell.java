package org.mephi.homework.timofeev.kafkaignite.shell;

import lombok.RequiredArgsConstructor;
import org.mephi.homework.timofeev.kafkaignite.data.StatisticRow;
import org.mephi.homework.timofeev.kafkaignite.service.ComputeService;
import org.mephi.homework.timofeev.kafkaignite.service.ProducerService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * Spring консоль сервис
 */
@ShellComponent
@RequiredArgsConstructor
public class AppShell {

    private final ProducerService producerService;
    private final ComputeService computeService;

    @ShellMethod(value = "send information about citizens, provide num citizens as argument", key = "send-batch")
    public void sendBatch(@ShellOption Integer num) {
        producerService.sendBatch(num);
    }

    @ShellMethod(value = "compute", key = "compute")
    public void compute() {
        for (StatisticRow statisticRow : computeService.computeStatistic()) {
            System.out.println(String.format("For age: %d, average salary is: %d and average trips count is: %d",
                    statisticRow.getAge(), statisticRow.getAverageSalary(), statisticRow.getAverageNumberTrips()));
        }
    }
}
