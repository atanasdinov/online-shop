package project.Service;

import project.model.entities.User;


public interface UserService {
    void add(String firstName, String lastName, String username, String password, String address);
    User get(String username);

}
