package com.itc.StockHouse.controller;

import com.itc.StockHouse.service.image.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/productImage")
@RequiredArgsConstructor
public class ProductImageController {
    private final ProductImageService productImageService;

    @PostMapping("/{productId}")
    public void uploadImage(UUID productId, @RequestBody MultipartFile file) {
        productImageService.uploadImage(productId, file);
    }

    @GetMapping("/{productId}")
    public byte[] getImagesAsZip(UUID productId) {
        return productImageService.getImages(productId).toByteArray();
    }
}
