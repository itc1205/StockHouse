package com.itc.StockHouse.support;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Аспект для измерения времени выполнения метода
 * <p>
 *     Вешается на аннотации {@link LogMethodExecutionTime}
 * </p>
 */
@Aspect
public class LogMethodExecutionAspect {
    @Around("@annotation(LogMethodExecutionTime)")
    public Object logMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();

        Object proceed = joinPoint.proceed();

        long executionTime = System.nanoTime() - start;

        System.out.printf("Метод %s выполнился за %d ms %n", joinPoint.getSignature(), executionTime / 1_000_000);
        return proceed;
    }
}
