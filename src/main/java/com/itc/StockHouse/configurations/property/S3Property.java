package com.itc.StockHouse.configurations.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "app.s3")
@Component
public class S3Property {
    private String url;
    private String bucketName;
    private String accessKeyId;
    private String secretAccessKey;
}
