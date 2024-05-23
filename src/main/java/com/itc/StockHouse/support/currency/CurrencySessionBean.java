package com.itc.StockHouse.support.currency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@SessionScope
@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencySessionBean {
    private String currencyName;
}
