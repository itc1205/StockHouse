package com.itc.StockHouse.support;

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
public class LogTransactionExecutionAspect implements TransactionSynchronization {
    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void logExecutionTime() {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization(){

            long startTime;

            @Override
            public void beforeCompletion() {
                startTime = System.nanoTime();
            }

            @Override
            public void afterCompletion(int status) {
                switch (status) {
                    case STATUS_COMMITTED -> System.out.print("Транзакция завершена.");
                    case STATUS_ROLLED_BACK -> System.out.print("Транзакция откатилась.");
                    case STATUS_UNKNOWN -> System.out.print("Статус транзакции неизвестен...");
                }
                System.out.printf(" Время занятое транзацией: %d ms%n", (System.nanoTime() - startTime) / 1_000_000);
            }
        });

    }
}