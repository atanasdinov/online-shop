package com.scalefocus.shop.exception;

public class ProductNotFoundException extends RuntimeException {

    static final long serialVersionUID = 1;

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNotFoundException(Throwable cause) {
        super(cause);
    }
}
