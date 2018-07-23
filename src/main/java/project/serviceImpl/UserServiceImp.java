package project.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.DTOS.UserDTO;
import project.model.entities.User;
import project.repositories.UserRepository;
import project.service.RoleService;
import project.service.UserService;
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
    public void register(UserDTO userDTO) {
        User user = modelParser.convert(userDTO, User.class);
        this.userRepository.add(user);

    }

    @Override
    public UserDTO get(String username) {
        User user = userRepository.get(username);
        return modelParser.convert(user, UserDTO.class);
    }
}