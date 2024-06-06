package com.itc.StockHouse.client.s3;

import com.itc.StockHouse.configurations.property.S3Property;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Slf4j
@Component
@RequiredArgsConstructor
public class S3ImageClientImpl implements S3ImageClient {

    private static final String fileNameMetadata = "filename";

    private final S3Property s3Property;
    private final S3Client s3Client;



    @Override
    public UUID uploadImage(MultipartFile file) {
        UUID imageId = UUID.randomUUID();

        Map<String, String> metadata = new HashMap<>() {{
           put("x-amz-meta-" + fileNameMetadata, file.getOriginalFilename());
        }};

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Property.getBucketName())
                .key(imageId.toString())
                .metadata(metadata)
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException | S3Exception e) {
            log.error("Could not upload file: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return imageId;
    }

    @Override
    public ByteArrayOutputStream getImagesAsZip(List<UUID> uuidList) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        try {
            for (UUID uuid : uuidList) {

                GetObjectRequest request = GetObjectRequest.builder()
                        .key(uuid.toString())
                        .bucket(s3Property.getBucketName())
                        .build();
                ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(request);

                ZipEntry zipEntry = new ZipEntry(responseBytes.response().metadata().get(fileNameMetadata));
                zipOutputStream.putNextEntry(zipEntry);

                zipOutputStream.write(responseBytes.asByteArray(), 0, responseBytes.asByteArray().length);

                // Close the ZipEntry.
                zipOutputStream.closeEntry();

            }
            zipOutputStream.close();

        } catch (IOException | S3Exception e) {
            log.error("Could not archive/download files: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return outputStream;
    }
}
