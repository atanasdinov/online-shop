package project.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.model.DTOS.UserLoginDTO;
import project.model.DTOS.UserRegisterDTO;
import project.model.entities.Role;
import project.model.entities.User;
import project.repositories.UserRepository;
import project.service.RoleService;
import project.service.UserService;
import project.utils.ModelParser;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImp implements UserService, UserDetailsService {

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
    public UserLoginDTO login(UserLoginDTO userLoginDTO) {
        User existingUser;
        UserLoginDTO loginDTO;

        try {
            existingUser = userRepository.getExistingUser(userLoginDTO.getUsername(), userLoginDTO.getPassword());  // == null if wrong password / non-existing user
            loginDTO = this.modelParser.convert(existingUser, UserLoginDTO.class);
        } catch (NullPointerException e) {
            return null;
        }
        return loginDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                roles
        );
        return userDetails;
    }
}