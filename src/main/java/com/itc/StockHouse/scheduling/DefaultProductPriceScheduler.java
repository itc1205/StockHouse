package com.itc.StockHouse.scheduling;

import com.itc.StockHouse.model.StockEntity;
import com.itc.StockHouse.repository.StockRepository;
import com.itc.StockHouse.support.LogMethodExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
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
@Component
@Profile("prod")
@ConditionalOnExpression(value = "#{'${app.scheduling.mode:none}'.equals('simple')}")
public class DefaultProductPriceScheduler {

    private final StockRepository repository;

    DefaultProductPriceScheduler(StockRepository repository) {
        this.repository = repository;
    }


    @Value("#{new java.math.BigDecimal(\"${app.priceIncreasePercentage:10}\")}")
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
                        .divide(BigDecimal.valueOf(100), MathContext.DECIMAL64)
                        .add(BigDecimal.ONE)
        );

        stockEntity.setPrice(newPrice);
        return stockEntity;
    }
}
