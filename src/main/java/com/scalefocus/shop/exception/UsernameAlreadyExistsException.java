package com.scalefocus.shop.exception;

import com.scalefocus.shop.model.entity.User;


/**
 * <b>This exception is used to indicate if a given username upon registration is already taken.</b>
 */
public class UsernameAlreadyExistsException extends RuntimeException {
    private User userData;

    public UsernameAlreadyExistsException(String message, User userData) {
        super(message);
        this.userData = userData;
    }

    public User getUserData() {
        return userData;
    }
}
