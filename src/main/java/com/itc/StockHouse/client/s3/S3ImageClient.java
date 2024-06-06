package com.itc.StockHouse.client.s3;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

public interface S3ImageClient {
    UUID uploadImage(MultipartFile file);

    ByteArrayOutputStream getImagesAsZip(List<UUID> uuidList);
}
