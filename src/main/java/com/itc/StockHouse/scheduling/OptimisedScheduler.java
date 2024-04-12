package com.itc.StockHouse.scheduling;

import org.springframework.scheduling.annotation.Scheduled;

public class OptimisedScheduler {
    @Scheduled(fixedRateString = "${app.scheduling.rate}")
    public void runScheduledTask() {
        System.out.println("Optimised scheduled task is running");
    }
}
