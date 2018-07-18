package project.repositories;

import project.model.entities.User;

import java.util.List;

public interface UserRepository {
    void add(User user);
    List<User> all();
    User getExistingUser(String username, String password);
}
