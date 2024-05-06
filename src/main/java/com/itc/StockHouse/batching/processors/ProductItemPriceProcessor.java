package com.itc.StockHouse.batching.processors;

import com.itc.StockHouse.model.ProductEntity;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.math.MathContext;


/**
 * Обработчик который занимается повышением цены у ProductEntity на priceIncreasePercentage
 */
public class ProductItemPriceProcessor implements ItemProcessor<ProductEntity, ProductEntity> {

    /**
     * @param priceIncreasePercentage Процент увеличения цены
     */
    public ProductItemPriceProcessor(BigDecimal priceIncreasePercentage) {
        this.priceIncreasePercentage = priceIncreasePercentage;
    }

    /**
     * Процент увеличения цены
     */
    private final BigDecimal priceIncreasePercentage;

    @Override
    public ProductEntity process(final ProductEntity productEntity) {
        BigDecimal newPrice = calculateNewPrice(productEntity.getPrice());
        productEntity.setPrice(newPrice);
        return productEntity;
    }

    private BigDecimal calculateNewPrice(BigDecimal oldPrice) {
        return oldPrice.multiply(
                priceIncreasePercentage
                        .divide(BigDecimal.valueOf(100), MathContext.DECIMAL64)
                        .add(BigDecimal.ONE)
        );
    }
}
