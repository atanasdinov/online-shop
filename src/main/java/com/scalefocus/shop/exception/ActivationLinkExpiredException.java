package com.scalefocus.shop.exception;


/**
 * <b>This exception is used to indicate that a user is trying to activate his account through expired validation link.</b>
 */
public class ActivationLinkExpiredException extends RuntimeException {
    private String username;

    public ActivationLinkExpiredException(String message, String username) {
        super(message);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
