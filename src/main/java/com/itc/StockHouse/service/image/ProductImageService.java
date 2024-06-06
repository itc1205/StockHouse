package com.itc.StockHouse.service.image;

import com.itc.StockHouse.client.s3.S3ImageClient;
import com.itc.StockHouse.exceptions.ProductNotFoundException;
import com.itc.StockHouse.model.ProductImageEntity;
import com.itc.StockHouse.repository.ProductImageRepository;
import com.itc.StockHouse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductImageService {
    private final S3ImageClient s3ImageClient;
    private final ProductImageRepository imageRepository;
    private final ProductRepository productRepository;


    @Transactional
    public void uploadImage(UUID productId, MultipartFile image) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with uuid: %s not found".formatted(productId)));

        UUID imageId = s3ImageClient.uploadImage(image);
        imageRepository.save(new ProductImageEntity(imageId, productId));
    }

    public ByteArrayOutputStream getImages(UUID productId) {
        List<UUID> idList = imageRepository.findByProductId(productId).stream()
                .map(ProductImageEntity::getS3id)
                .toList();
        return s3ImageClient.getImagesAsZip(idList);
    }

}
