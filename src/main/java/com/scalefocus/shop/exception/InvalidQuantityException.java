package com.scalefocus.shop.exception;


/**
 * <b>This exception is used to indicate invalid data entered in "product quantity" form.</b>
 */
public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException(String message) {
        super(message);
    }
}
