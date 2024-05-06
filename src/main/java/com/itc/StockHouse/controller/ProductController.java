package com.itc.StockHouse.controller;

import com.itc.StockHouse.dto.CreateProductDto;
import com.itc.StockHouse.dto.PatchAmountProductDTO;
import com.itc.StockHouse.dto.ProductDto;
import com.itc.StockHouse.dto.UpdateProductDto;
import com.itc.StockHouse.exceptions.ProductVendorCodeAlreadyExistsException;
import com.itc.StockHouse.exceptions.ProductNotFoundException;
import com.itc.StockHouse.service.ProductService;
import com.itc.StockHouse.utils.ProductMappingUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Rest-контроллер для сервиса по управлению товарами на складе
 *
 * <p> Основная задача контроллера - маппинг сервиса {@link ProductService} к соответствующим конечным точкам приложения</p>
 */
@RestController
@Validated
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    private final ProductMappingUtils utils;

    @Operation(summary = "Операция создания товара на складе")
    @PostMapping("/")
    public ProductDto createProduct(@RequestBody CreateProductDto createProductDto) throws ProductVendorCodeAlreadyExistsException {
        return utils.mapToProductDto(
                productService.createProduct(
                        utils.mapToProductEntity(createProductDto)
                )
        );
    }

    @Operation(summary = "Операция получения информации о товаре на складе")
    @GetMapping("/{id}")
    public ProductDto getProductByID(@PathVariable("id") UUID id) throws ProductNotFoundException {
        return utils.mapToProductDto(
                productService.getProductByUUID(id)
        );
    }

    @Operation(summary = "Операция получения всех товаров на складе")
    @GetMapping("/")
    public List<ProductDto> getAllProducts(Pageable pageable) {
        return productService.getAll(pageable)
                .stream()
                .map(utils::mapToProductDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Операция обновления товара на складе")
    @PutMapping("/")
    public ProductDto updateProduct(@RequestBody UpdateProductDto updateProductDto) throws ProductVendorCodeAlreadyExistsException, ProductNotFoundException {
        return utils.mapToProductDto(
                productService.updateProduct(
                        utils.mapToProductEntity(updateProductDto)
                )
        );
    }

    @Operation(summary = "Операция обновления количества товара на складе")
    @PatchMapping("/{id}")
    public ProductDto updateProductAmount(@PathVariable("id") UUID id,
                                        @RequestBody PatchAmountProductDTO patchAmountProductDTO) {
        return utils.mapToProductDto(
                productService.updateAmountOfProduct(id, patchAmountProductDTO.getAmount())
        );
    }

    @Operation(summary = "Операция удаления товара на складе")
    @DeleteMapping("/{id}")
    public void deleteProductByUUID(@PathVariable("id") UUID id) throws ProductNotFoundException {
        productService.deleteProductByUUID(id);
    }
}
