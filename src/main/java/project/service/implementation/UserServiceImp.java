package project.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.model.DTOS.UserLoginDTO;
import project.model.DTOS.UserRegisterDTO;
import project.model.entities.Cart;
import project.model.entities.Role;
import project.model.entities.User;
import project.repository.specification.CartRepository;
import project.repository.specification.UserRepository;
import project.service.specification.RoleService;
import project.service.specification.UserService;
import project.utility.ModelParser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;


@Service
public class UserServiceImp implements UserService {

    private UserRepository userRepository;
    private ModelParser modelParser;
    private RoleService roleService;
    private BCryptPasswordEncoder passwordEncoder;
    private CartRepository cartRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository, ModelParser modelParser, RoleService roleService, BCryptPasswordEncoder passwordEncoder, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.modelParser = modelParser;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        Role role;
        User user = new User(userRegisterDTO.getFirstName(),
                userRegisterDTO.getLastName(),
                userRegisterDTO.getEmail(),
                userRegisterDTO.getUsername(),
                passwordEncoder.encode(userRegisterDTO.getPassword()),
                userRegisterDTO.getAddress());

        if (userRepository.getAllUsers().isEmpty()) {
            roleService.insert(new Role("ADMIN"));
            roleService.insert(new Role("USER"));
            role = roleService.getRoleByName("ADMIN");
        } else
            role = roleService.getRoleByName("USER");

        cartRepository.persist();

        Cart cart = cartRepository.getCart(cartRepository.getAllCarts().size());
        user.setCart(cart);
        cart.setUser(user);
        role.getUsers().add(user);
        user.setRole(role);

        userRepository.addUser(user, role);
    }

    @Override
    public boolean login(UserLoginDTO userLoginDTO, HttpServletResponse response) {
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
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        String token = bytes.toString();
        user.setToken(token);
        userRepository.setToken(token, user);

        return token;
    }

    @Override
    public User getUser(String name) {
        return userRepository.getExistingUserByUsername(name);
    }

    @Override
    public boolean doesExist(String username) {
        return userRepository.doesExist(username);
    }
}
