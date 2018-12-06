package com.scalefocus.shop.exception;


/**
 * <b>This exception is used to check if a given category already exists.</b>
 */
public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}
