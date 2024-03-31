package com.itc.StockHouse.exceptions;

/**
 * Исключение, выбрасываемое при конфликте артикулов
 */
public class StockVendorCodeAlreadyExistsException extends RuntimeException {
    public StockVendorCodeAlreadyExistsException(String message) {
        super(message);
    }
}
