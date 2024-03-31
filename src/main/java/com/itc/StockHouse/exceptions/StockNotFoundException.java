package com.itc.StockHouse.exceptions;

/**
 * Исключение, выбрасываемое при ненахождении товара
 */
public class StockNotFoundException extends RuntimeException {
     public StockNotFoundException(String message) {
         super(message);
     }
}
