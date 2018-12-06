package com.scalefocus.shop.exception;


/**
 * <b>This exception is used to indicate that a given category is not found.</b>
 */
public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
