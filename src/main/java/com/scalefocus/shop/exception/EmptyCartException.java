package com.scalefocus.shop.exception;


/**
 * <b>This exception is used to indicate when a user is trying to empty a cart which is already empty.</b>
 */
public class EmptyCartException extends RuntimeException {
    public EmptyCartException(String message) {
        super(message);
    }
}
