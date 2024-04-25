package com.itc.StockHouse.support;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;


/**
 * Аспект для измерения времени выполнения транзакции
 *
 * <p>
 *     Вешается на {@link org.springframework.transaction.annotation.Transactional}
 * </p>
 *
 * <p>
 *     Результат времени выполнения выведется в stdout
 * </p>
 */
@Aspect
@Component
@Slf4j
public class LogTransactionExecutionAspect implements TransactionSynchronization {
    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void logExecutionTime() {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization(){

            final long startTime = System.nanoTime();


            @Override
            public void afterCompletion(int status) {
                String transactionStatus;
                switch (status) {
                    case STATUS_COMMITTED -> transactionStatus = "Commited";
                    case STATUS_ROLLED_BACK -> transactionStatus = "Rolled back";
                    default -> transactionStatus = "Unknown";
                }
                log.info("Finished transaction");
                log.info("Transaction status: {}", transactionStatus);
                log.info("Transaction time: {}ms", (System.nanoTime() - startTime) / 1_000_000);
            }
        });

    }
}