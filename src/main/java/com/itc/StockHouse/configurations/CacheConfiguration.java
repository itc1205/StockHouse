package com.itc.StockHouse.configurations;

import com.google.common.cache.CacheBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


@Configuration
@EnableCaching
public class CacheConfiguration {

    public static final String currencyCacheName = "currency";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(currencyCacheName) {
            // https://stackoverflow.com/a/12174860
            @Override
            protected @NotNull Cache createConcurrentMapCache(@NotNull String name) {
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder()
                                .expireAfterWrite(1, TimeUnit.MINUTES)
                                .build()
                                .asMap(),
                        false
                );
            }
        };
    }
}