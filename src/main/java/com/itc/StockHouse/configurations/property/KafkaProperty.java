package com.itc.StockHouse.configurations.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "app.kafka")
@Component
public class KafkaProperty {
    private String bootstrapAddress;
    private String topicName;
    private Integer partitions;
    private String groupId;
}
