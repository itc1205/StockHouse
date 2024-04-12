package com.itc.StockHouse.scheduling;

import org.springframework.scheduling.annotation.Scheduled;

public class DefaultScheduler {
    @Scheduled(fixedRateString = "${app.scheduling.rate}")
    public void runScheduledTask() {
        System.out.println("Default scheduled task is running");
    }
}
