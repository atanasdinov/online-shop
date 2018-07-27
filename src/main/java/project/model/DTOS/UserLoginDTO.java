package project.model.DTOS;

import javax.validation.constraints.NotEmpty;

public class UserLoginDTO {

    private static final String EMPTY_FIELD_MESSAGE = "Field must not be empty";

    @NotEmpty(message = EMPTY_FIELD_MESSAGE)
    private String username;

    @NotEmpty(message = EMPTY_FIELD_MESSAGE)
    private String password;

    public UserLoginDTO() {
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
}
