package project.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.model.DTOS.UserLoginDTO;
import project.model.DTOS.UserRegisterDTO;
import project.model.entities.Role;
import project.model.entities.User;
import project.repositories.UserRepository;
import project.utils.ModelParser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;


@Service
public class UserServiceImp implements UserService {

    private UserRepository userRepository;
    private ModelParser modelParser;
    private RoleService roleService;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(UserRepository userRepository, ModelParser modelParser, RoleService roleService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelParser = modelParser;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        Role role;

        User user = new User(userRegisterDTO.getFirstName(),
                userRegisterDTO.getLastName(),
                userRegisterDTO.getEmail(),
                userRegisterDTO.getUsername(),
                passwordEncoder.encode(userRegisterDTO.getPassword()), // Storing the encoded password
                userRegisterDTO.getAddress());

        if (this.userRepository.all().isEmpty()) {
            this.roleService.insert(new Role("ADMIN"));
            this.roleService.insert(new Role("USER"));
            role = this.roleService.get("ADMIN");
        } else {
            role = this.roleService.get("USER");
        }

        role.getUsers().add(user);
        user.setRole(role);

        this.userRepository.add(user, role);
    }

    @Override
    public boolean login(UserLoginDTO userLoginDTO,
                         HttpServletResponse response) {

        User user = userRepository.getExistingUser(userLoginDTO.getUsername(), userLoginDTO.getPassword());

        final int COOKIE_EXPIRATION_30_MIN = 60 * 30;

        if (user == null)
            return false;
        else {
            Cookie cookie = new Cookie("token", this.addToken(user));
            cookie.setPath("/");
            cookie.setMaxAge(COOKIE_EXPIRATION_30_MIN);
            response.addCookie(cookie);

            return true;
        }
    }

    @Override
    public User checkToken(String token) {
        if (token == null)
            return null;
        return userRepository.checkToken(token);
    }

    @Override
    public String addToken(User user) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String token = bytes.toString();
        user.setToken(token);
        userRepository.setToken(token, user);
        return token;
    }

}
