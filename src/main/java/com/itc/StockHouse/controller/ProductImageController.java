package com.itc.StockHouse.controller;

import com.itc.StockHouse.service.image.ProductImageService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/productImage")
@RequiredArgsConstructor
public class ProductImageController {
    private final ProductImageService productImageService;

    @PostMapping(value = "/{productId}")
    public void uploadImage(@PathVariable("productId") UUID productId, @RequestParam("file") MultipartFile file) {
        productImageService.uploadImage(productId, file);
    }

    @GetMapping(value = "/{productId}", produces = "application/zip")
    public byte[] getImagesAsZip(@PathVariable("productId") UUID productId, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"images-%s.zip\"".formatted(productId));
        return productImageService.getImages(productId).toByteArray();
    }
}
