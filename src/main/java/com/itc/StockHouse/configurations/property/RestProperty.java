package com.itc.StockHouse.configurations.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "rest")
@Component
public class RestProperty {
    ClientProperty accountService;
    ClientProperty crmService;
}