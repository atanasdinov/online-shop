package com.scalefocus.shop.exception;


/**
 * <b>This method is used to check whether the password from input form is correct.</b>
 */
public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String message) {
        super(message);
    }
}
