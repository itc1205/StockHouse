package com.itc.StockHouse.exceptions;

/**
 * Исключение, выбрасываемое при ненахождении товара
 */
public class ProductNotFoundException extends RuntimeException {
     public ProductNotFoundException(String message) {
         super(message);
     }
}
