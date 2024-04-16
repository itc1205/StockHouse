package com.itc.StockHouse.processors;

import com.itc.StockHouse.model.StockEntity;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;


/**
 * Обработчик который занимается повышением цены у StockEntity на priceIncreasePercentage
 */
public class StockItemPriceProcessor implements ItemProcessor<StockEntity, StockEntity> {

    /**
     * @param priceIncreasePercentage Процент увеличения цены
     */
    public StockItemPriceProcessor(Double priceIncreasePercentage) {
        this.priceIncreasePercentage = priceIncreasePercentage;
    }

    /**
     * Процент увеличения цены
     */
    private final Double priceIncreasePercentage;

    @Override
    public StockEntity process(final StockEntity stockEntity) {
        BigDecimal newPrice = stockEntity
                .getPrice()
                .multiply(BigDecimal.valueOf(priceIncreasePercentage / 100))
                .add(stockEntity.getPrice());
        stockEntity.setPrice(newPrice);
        return stockEntity;
    }
}
