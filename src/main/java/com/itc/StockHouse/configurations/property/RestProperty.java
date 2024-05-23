package com.itc.StockHouse.configurations.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rest")
public class RestProperty {
    ClientProperty currencyService;
}
