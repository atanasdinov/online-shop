package com.scalefocus.shop.exception;

import com.scalefocus.shop.model.entity.User;
import org.springframework.validation.BindingResult;


/**
 * <b>This exception is used to indicate invalid entered data upon user registration.</b>
 */
public class InvalidUserDataException extends RuntimeException {
    private User userData;
    private BindingResult bindingResult;

    public InvalidUserDataException(String message, User userData, BindingResult bindingResult) {
        super(message);
        this.userData = userData;
        this.bindingResult = bindingResult;
    }

    public User getUserData() {
        return userData;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}