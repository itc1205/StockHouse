package com.itc.StockHouse.scheduling;

import com.itc.StockHouse.model.StockEntity;
import com.itc.StockHouse.repository.StockRepository;
import com.itc.StockHouse.support.LogMethodExecutionTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Дефолтный шедулер изменения цены
 *
 * <p>
 *     Не рекомендуется к включению, так как на большом количестве записей будет потреблять большое кол-во ОЗУ
 * </p>
 *
 * <p>
 *     Под капотом использует StreamAPI и фетчит !все! записи из базы данных
 * </p>
 *
 * @deprecated use {@link OptimisedProductPriceBatchScheduler} instead
 */
@Slf4j
@AllArgsConstructor
public class DefaultProductPriceScheduler {

    private StockRepository repository;

    @Value("${app.priceIncreasePercentage}")
    private BigDecimal priceIncreasePercentage;

    @Transactional
    @LogMethodExecutionTime
    @Scheduled(fixedDelayString = "${app.scheduling.rate}")
    public void runScheduledTask() {
        List<StockEntity> list = repository
                .findAll()
                .stream()
                .map(this::updateStockPrice)
                .toList();

        repository.saveAll(list);
        log.atInfo().log("Обновлено %d товаров".formatted(list.size()));
    }

    private StockEntity updateStockPrice(StockEntity stockEntity) {
        BigDecimal oldPrice = stockEntity.getPrice();

        BigDecimal newPrice = oldPrice.multiply(
                priceIncreasePercentage
                        .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                        .add(BigDecimal.ONE)
        );

        stockEntity.setPrice(newPrice);
        return stockEntity;
    }
}
