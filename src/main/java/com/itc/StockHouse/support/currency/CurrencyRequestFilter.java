package com.itc.StockHouse.support.currency;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CurrencyRequestFilter extends OncePerRequestFilter {

    private final CurrencyProvider currencyProvider;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {


        if (request.getHeader("currency") != null) {
            currencyProvider.setCurrency(Currency.valueOf(request.getHeader("currency")));
        }

        filterChain.doFilter(request, response);
    }
}