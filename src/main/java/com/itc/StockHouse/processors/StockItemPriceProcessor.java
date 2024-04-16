package com.itc.StockHouse.processors;

import com.itc.StockHouse.model.StockEntity;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.math.RoundingMode;


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
        BigDecimal newPrice = stockEntity
                .getPrice()
                .multiply(priceIncreasePercentage.divide(BigDecimal.valueOf(100), RoundingMode.UNNECESSARY))
                .add(stockEntity.getPrice());
        stockEntity.setPrice(newPrice);
        return stockEntity;
    }
}
