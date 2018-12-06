package com.scalefocus.shop.exception;


/**
 * <b>This exception is used to indicate whether the quantity (from an input form) is available.</b>
 */
public class QuantityNotAvailableException extends RuntimeException {
    public QuantityNotAvailableException(String message) {
        super(message);
    }
}
