package com.itc.StockHouse.configurations.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "rest")
@Data
public class RestProperties {
    ServiceProperties currencyService;
}
