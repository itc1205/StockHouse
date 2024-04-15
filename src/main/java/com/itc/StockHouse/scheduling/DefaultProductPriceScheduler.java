package com.itc.StockHouse.scheduling;

import com.itc.StockHouse.model.StockEntity;
import com.itc.StockHouse.repository.StockRepository;
import com.itc.StockHouse.support.LogMethodExecutionTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public class DefaultProductPriceScheduler {

    @Autowired
    private StockRepository repository;

    @Value("${app.priceIncreasePercentage}")
    private Double priceIncreasePercentage;

    @Transactional
    @LogMethodExecutionTime
    @Scheduled(fixedDelayString = "${app.scheduling.rate}")
    public void runScheduledTask() {
        List<StockEntity> list = repository
                .findAll()
                .stream()
                .peek(stockEntity -> {
                    BigDecimal newPrice = stockEntity
                            .getPrice()
                            .multiply(BigDecimal.valueOf(priceIncreasePercentage / 100))
                            .add(stockEntity.getPrice());
                    stockEntity.setPrice(newPrice);
                })
                .toList();
        repository.saveAll(list);
        System.out.printf("Обновлено %d товаров%n", list.size());
    }
}
