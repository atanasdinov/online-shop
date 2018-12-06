package com.scalefocus.shop.exception;


/**
 * <b>This exception is used to indicate whether a product already exists.</b>
 */
public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String message) {
        super(message);
    }
}
