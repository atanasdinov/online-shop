package project.model.DTOS;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRegisterDTO {

    private static final int USERNAME_MIN_LENGTH = 6;
    private static final int USERNAME_MAX_LENGTH = 15;
    private static final String USERNAME_LENGTH_ERROR_MESSAGE = "Username must be between 6 and 15 symbols";
    private static final String EMPTY_FIELD_MESSAGE = "Field must not be empty";
    private static final String USERNAME_PATTERN_MISMATCH_ERROR = "Username must contains: letter,digit,'-' or '_' and ends with digit or letter";
    private static final String NAME_PATTERN_MISMATCH_ERROR = "Name must contains only letters";
    private static final String PASSWORD_LENGTH_ERROR_MESSAGE = "Password length must be between 7 and 20";
    private static final int PASSWORD_MIN_LENGTH = 7;
    private static final int PASSWORD_MAX_LENGTH = 20;
    public static final String EMAIL_ADDRESS_ERROR_MESSAGE = "Invalid email";


    @NotEmpty(message = EMPTY_FIELD_MESSAGE)
    @Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH, message = USERNAME_LENGTH_ERROR_MESSAGE)
    @Pattern(regexp = "^([a-zA-Z][a-zA-Z0-9_-]+)[a-zA-Z0-9]$", message = USERNAME_PATTERN_MISMATCH_ERROR)
    private String username;

    @NotEmpty(message = EMPTY_FIELD_MESSAGE)
    @Size(min = PASSWORD_MIN_LENGTH,max = PASSWORD_MAX_LENGTH, message = PASSWORD_LENGTH_ERROR_MESSAGE)
    private String password;

    @NotEmpty(message = EMPTY_FIELD_MESSAGE)
    @Pattern(regexp = "^[a-zA-Z]+$", message = NAME_PATTERN_MISMATCH_ERROR)
    private String firstName;

    @NotEmpty(message = EMPTY_FIELD_MESSAGE)
    @Pattern(regexp = "^[a-zA-Z]+$", message = NAME_PATTERN_MISMATCH_ERROR)
    private String lastName;

    @NotEmpty(message = EMPTY_FIELD_MESSAGE)
    @Pattern(regexp = "^[a-z]+@[a-z]+.[a-z]{2,6}$", message = EMAIL_ADDRESS_ERROR_MESSAGE)
    private String email;

    private String address;

    public UserRegisterDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
