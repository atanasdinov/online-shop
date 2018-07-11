package project.Dao;

import project.Entity.User;

import java.util.List;

public interface UserDao {
    void add(User user);
    List<User> listUsers();
}
