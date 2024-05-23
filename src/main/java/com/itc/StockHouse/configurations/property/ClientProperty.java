package com.itc.StockHouse.configurations.property;

import lombok.Data;

import java.util.HashMap;

@Data
public class ClientProperty {
    String host;
    HashMap<String, String> methods;
}