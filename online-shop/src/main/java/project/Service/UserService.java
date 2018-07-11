package project.Service;

import project.Entity.User;

import java.util.List;

public interface UserService {
    public boolean Login(String userName, String password);

    void add(User user);
    List<User> listUsers();
}
