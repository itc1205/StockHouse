package com.itc.StockHouse.support;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Аспект для измерения времени выполнения метода
 * <p>
 *     Вешается на аннотации {@link LogMethodExecutionTime}
 * </p>
 */
@Aspect
@Component
@Slf4j
public class LogMethodExecutionAspect {
    @Around("@annotation(LogMethodExecutionTime)")
    public Object logMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.nanoTime() - start;
            log.info("Method {} executed in {} ms", joinPoint.getSignature(), executionTime / 1_000_000);
        }
    }
}
