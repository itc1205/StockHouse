package com.itc.StockHouse.configurations;

import com.itc.StockHouse.configurations.property.S3Property;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final S3Property s3Property;

    @Bean
    public S3Client s3Client() {
        // Empty credentials
        AwsCredentials credentials = AwsBasicCredentials.builder()
                .accessKeyId(s3Property.getAccessKeyId())
                .secretAccessKey(s3Property.getSecretAccessKey())
                .build();

        Region region = Region.EU_CENTRAL_1;

        return S3Client.builder()
                .endpointOverride(URI.create(s3Property.getUrl()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        credentials
                ))
                .region(region)
                .build();
    }
}
