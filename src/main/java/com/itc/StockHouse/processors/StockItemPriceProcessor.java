package com.itc.StockHouse.processors;

import com.itc.StockHouse.model.StockEntity;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;


public class StockItemPriceProcessor implements ItemProcessor<StockEntity, StockEntity> {

    public StockItemPriceProcessor(Double priceIncreasePercentage) {
        this.priceIncreasePercentage = priceIncreasePercentage;
    }

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
