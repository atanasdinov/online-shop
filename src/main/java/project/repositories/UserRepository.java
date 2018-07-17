package project.repositories;

import project.model.entities.User;

import java.util.List;

public interface UserRepository {
    void add(User user);
    User get(String username);
    List<User> all();
}
