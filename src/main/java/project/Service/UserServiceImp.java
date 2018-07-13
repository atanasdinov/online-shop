package project.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.User;
import project.repositories.UserRepository;

@Service
public class UserServiceImp implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(String firstName, String lastName, String username, String password, String address) {
        this.userRepository.add(new User (firstName, lastName, username, password, address));
    }

    @Override
    public User get(String username) {
        return userRepository.get(username);
    }
}