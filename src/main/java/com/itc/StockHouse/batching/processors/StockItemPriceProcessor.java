package com.itc.StockHouse.batching.processors;

import com.itc.StockHouse.model.StockEntity;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.math.MathContext;


/**
 * Обработчик который занимается повышением цены у StockEntity на priceIncreasePercentage
 */
public class StockItemPriceProcessor implements ItemProcessor<StockEntity, StockEntity> {

    /**
     * @param priceIncreasePercentage Процент увеличения цены
     */
    public StockItemPriceProcessor(BigDecimal priceIncreasePercentage) {
        this.priceIncreasePercentage = priceIncreasePercentage;
    }

    /**
     * Процент увеличения цены
     */
    private final BigDecimal priceIncreasePercentage;

    @Override
    public StockEntity process(final StockEntity stockEntity) {
        BigDecimal newPrice = calculateNewPrice(stockEntity.getPrice());
        stockEntity.setPrice(newPrice);
        return stockEntity;
    }

    private BigDecimal calculateNewPrice(BigDecimal oldPrice) {
        return oldPrice.multiply(
                priceIncreasePercentage
                        .divide(BigDecimal.valueOf(100), MathContext.DECIMAL64)
                        .add(BigDecimal.ONE)
        );
    }
}
