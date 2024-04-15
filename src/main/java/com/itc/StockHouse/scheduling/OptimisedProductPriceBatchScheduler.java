package com.itc.StockHouse.scheduling;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.UUID;

public class OptimisedProductPriceBatchScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job priceUpdateJob;

    @Scheduled(fixedDelayString = "${app.scheduling.rate}")
    public void runScheduledTask() throws Exception {
        JobExecution jobExecution = jobLauncher.run(priceUpdateJob,
                new JobParametersBuilder()
                        .addLocalDateTime("startDateTime", LocalDateTime.now())
                        .addString("uuid", UUID.randomUUID().toString(), true)
                        .toJobParameters());
    }
}
