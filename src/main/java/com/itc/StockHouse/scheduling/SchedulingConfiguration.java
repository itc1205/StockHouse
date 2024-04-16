package com.itc.StockHouse.scheduling;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Конфигурационный класс для бинов шедулинга
 */
@Configuration
@EnableScheduling
public class SchedulingConfiguration {
    @Bean
    @Profile("prod")
    @ConditionalOnExpression("${app.scheduling.enabled:false} and ${app.scheduling.optimization:false} == false")
    public DefaultProductPriceScheduler defaultSchedule() {
        return new DefaultProductPriceScheduler();
    }

    @Bean
    @Profile("prod")
    @ConditionalOnExpression("${app.scheduling.enabled:false} and ${app.scheduling.optimization:false}")
    public OptimisedProductPriceBatchScheduler optimisedSchedule() {
        return new OptimisedProductPriceBatchScheduler();
    }
}
