package com.itc.StockHouse.configurations.property;

import lombok.Data;

import java.util.HashMap;

@Data
public class ServiceProperties {
    private String host;
    private HashMap<String, String> methods;
}
