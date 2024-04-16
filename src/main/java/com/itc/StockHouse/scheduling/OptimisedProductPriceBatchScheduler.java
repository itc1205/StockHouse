package com.itc.StockHouse.scheduling;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Оптимизированный шедулер обновления цены
 *
 * <p>
 *     При работе также записывает все измененные записи в файл app.output_file указанный в application.yaml
 * </p>
 *
 * <p>
 *     Под капотом постоянно перезапускает задачу из {@link com.itc.StockHouse.batching.BatchConfiguration}
 *     через время app.scheduling.rate указанное в application.yaml
 * </p>
 * <p>
 *     За счет батчинга потребляет меньше и отрабатывает быстрее чем
 *     {@link com.itc.StockHouse.scheduling.DefaultProductPriceScheduler}
 * </p>
 */
public class OptimisedProductPriceBatchScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job priceUpdateJob;

    /**
     * Метод для запуска задачи каждые app.scheduling.rate миллисекунд указанные в application.yaml
     */
    @Scheduled(fixedDelayString = "${app.scheduling.rate}")
    public void runScheduledTask() throws Exception {
        JobExecution jobExecution = jobLauncher.run(priceUpdateJob,
                new JobParametersBuilder()
                        .addLocalDateTime("startDateTime", LocalDateTime.now())
                        .addString("uuid", UUID.randomUUID().toString(), true)
                        .toJobParameters());
    }
}
