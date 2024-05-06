package com.itc.StockHouse.exceptions;

/**
 * Исключение, выбрасываемое при конфликте артикулов
 */
public class ProductVendorCodeAlreadyExistsException extends RuntimeException {
    public ProductVendorCodeAlreadyExistsException(String message) {
        super(message);
    }
}
