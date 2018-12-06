package com.scalefocus.shop.model.constant;

public class ValidationConstants {

    public static final int USERNAME_MIN_LENGTH = 6;
    public static final int USERNAME_MAX_LENGTH = 15;
    public static final String USERNAME_LENGTH_ERROR_MESSAGE = "Username must be between 6 and 15 symbols";
    public static final String USERNAME_PATTERN_MISMATCH_ERROR = "Username must contains: letter,digit,'-' or '_' and ends with digit or letter";

    public static final int PASSWORD_MIN_LENGTH = 7;
    public static final String PASSWORD_LENGTH_ERROR_MESSAGE = "Password length must be at least 7 symbols";

    public static final String QUANTITY_PATTERN_MISMATCH_ERROR = "Invalid quantity";
    public static final String PRICE_PATTERN_MISMATCH_ERROR = "Invalid price";
    public static final String EMPTY_FIELD_MESSAGE = "Field must not be empty";
    public static final String EMAIL_ADDRESS_ERROR_MESSAGE = "Invalid email";

    public static final String NAME_PATTERN_MISMATCH_ERROR = "Name must contains only letters";
    public static final String EMAIL_REGEX = "^[a-z0-9._]+@[a-z]+.[a-z]{2,6}$";
    public static final String USERNAME_REGEX = "^([a-zA-Z][a-zA-Z0-9_-]+)[a-zA-Z0-9]$";
    public static final String FIRST_NAME_REGEX = "^[a-zA-Z]+$";
    public static final String LAST_NAME_REGEX = "^[a-zA-Z]+$";

}
