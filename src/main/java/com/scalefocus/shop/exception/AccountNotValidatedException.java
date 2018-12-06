package com.scalefocus.shop.exception;


/**
 * <b>This exception is used to check account validation </b>
 */
public class AccountNotValidatedException extends RuntimeException {
    public AccountNotValidatedException(String message) {
        super(message);
    }
}
