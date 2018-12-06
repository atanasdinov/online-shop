package com.scalefocus.shop.exception;

import com.scalefocus.shop.model.entity.User;


/**
 * <b>This exception indicates that an e-mail is already in use upon registration.</b>
 */
public class EmailAlreadyExistsException extends RuntimeException {
    private User userData;

    public EmailAlreadyExistsException(String message, User userData) {
        super(message);
        this.userData = userData;
    }

    public User getUserData() {
        return userData;
    }
}
