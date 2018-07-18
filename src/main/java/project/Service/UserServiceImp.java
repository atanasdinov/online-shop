package project.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.DTOS.UserLoginDTO;
import project.model.DTOS.UserRegisterDTO;
import project.model.entities.User;
import project.repositories.UserRepository;
import project.utils.ModelParser;

@Service
public class UserServiceImp implements UserService {

    private UserRepository userRepository;
    private ModelParser modelParser;

    @Autowired
    public UserServiceImp(UserRepository userRepository, ModelParser modelParser) {
        this.userRepository = userRepository;
        this.modelParser = modelParser;
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        User user = modelParser.convert(userRegisterDTO, User.class);
        this.userRepository.add(user);
    }

    @Override
    public UserLoginDTO login(UserLoginDTO userLoginDTO) {
        User existingUser;
        UserLoginDTO loginDTO;
        try {
            existingUser = userRepository.getExistingUser(userLoginDTO.getUsername(), userLoginDTO.getPassword());
            loginDTO = this.modelParser.convert(existingUser, UserLoginDTO.class);
        } catch (NullPointerException e) {
            return null;
        }
        return loginDTO;
    }

}