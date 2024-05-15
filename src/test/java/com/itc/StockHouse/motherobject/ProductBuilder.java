package com.itc.StockHouse.motherobject;

import com.itc.StockHouse.model.ProductEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class ProductBuilder {
    private static final String DEFAULT_NAME = "Default stock name";
    private static final BigDecimal DEFAULT_PRICE = BigDecimal.TEN;
    private static final Integer DEFAULT_AMOUNT = 15;
    private static final String DEFAULT_CATEGORY = "Default category";
    private static final String DEFAULT_VENDOR_CODE = "VX121321RU";
    private static final String DEFAULT_DESCRIPTION = "LOREM IPSUM";
    private static final OffsetDateTime DEFAULT_UPDATE_TIME = OffsetDateTime.now();
    private static final OffsetDateTime DEFAULT_CREATE_TIME = OffsetDateTime.now();
    private static final Boolean DEFAULT_IS_AVAILABLE = true;


    private String name = DEFAULT_NAME;
    private BigDecimal price = DEFAULT_PRICE;
    private Integer amount = DEFAULT_AMOUNT;
    private String category = DEFAULT_CATEGORY;
    private String vendorCode = DEFAULT_VENDOR_CODE;
    private String description = DEFAULT_DESCRIPTION;
    private OffsetDateTime updateDate = DEFAULT_UPDATE_TIME;
    private OffsetDateTime creationDate = DEFAULT_CREATE_TIME;
    private Boolean isAvailable = DEFAULT_IS_AVAILABLE;


    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ProductBuilder withAmount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public ProductBuilder withCategory(String category) {
        this.category = category;
        return this;
    }

    public ProductBuilder withVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
        return this;
    }

    public ProductBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ProductBuilder withCreationDate(OffsetDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public ProductBuilder withUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public ProductBuilder withIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
        return this;
    }

    public ProductEntity build() {
        return ProductEntity.builder()
                .name(name)
                .price(price)
                .amount(amount)
                .category(category)
                .vendorCode(vendorCode)
                .description(description)
                .updateDate(updateDate)
                .creationDate(creationDate)
                .isAvailable(isAvailable)
                .build();
    }

}
