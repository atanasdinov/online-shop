package com.scalefocus.shop.exception;


/**
 * <b>This exception is used to check if a cart is empty when proceeding with checkout.</b>
 */
public class CheckoutEmptyCartException extends RuntimeException {
    public CheckoutEmptyCartException(String message) {
        super(message);
    }
}
