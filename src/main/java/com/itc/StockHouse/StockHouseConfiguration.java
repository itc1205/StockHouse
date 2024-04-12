package com.itc.StockHouse;

import com.itc.StockHouse.scheduling.DefaultScheduler;
import com.itc.StockHouse.scheduling.OptimisedScheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StockHouseConfiguration {
    @Bean
    @Profile("dev")
    @ConditionalOnExpression("${app.scheduling.enabled:false} and ${app.scheduling.optimization:false} == false")
    public DefaultScheduler defaultSchedule() {
        return new DefaultScheduler();
    }

    @Bean
    @Profile("dev")
    @ConditionalOnExpression("${app.scheduling.enabled:false} and ${app.scheduling.optimization:false}")
    public OptimisedScheduler optimisedSchedule() {
        return new OptimisedScheduler();
    }
}
