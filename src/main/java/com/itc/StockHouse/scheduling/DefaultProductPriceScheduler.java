package com.itc.StockHouse.scheduling;

import com.itc.StockHouse.model.StockEntity;
import com.itc.StockHouse.repository.StockRepository;
import com.itc.StockHouse.support.LogMethodExecutionTime;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        log.atInfo().log("Обновлено %d товаров".formatted(list.size()));
    }
}
