package project.repository.specification;

import project.model.entities.Role;
import project.model.entities.User;

import java.util.List;


public interface UserRepository {

    void addUser(User user, Role role);

    List<User> getAllUsers();

    User getExistingUser(String username, String password);

    User getExistingUserByUsername(String username);

    User checkToken(String token);

    void setToken(String token, User user);
}
