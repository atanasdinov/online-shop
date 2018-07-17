package project.Dao;

import project.model.entities.User;

import java.util.List;

public interface UserDao {
    void add(User user);
    List<User> listUsers();
}
