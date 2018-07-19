package project.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.model.DTOS.UserLoginDTO;
import project.model.DTOS.UserRegisterDTO;
import project.model.entities.Role;
import project.model.entities.User;
import project.repositories.UserRepository;
import project.utils.ModelParser;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImp implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private ModelParser modelParser;
    private RoleService roleService;

    @Autowired
    public UserServiceImp(UserRepository userRepository, ModelParser modelParser, RoleService roleService) {
        this.userRepository = userRepository;
        this.modelParser = modelParser;
        this.roleService = roleService;
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        Role role;

        User user = new User(userRegisterDTO.getFirstName(), userRegisterDTO.getLastName(), userRegisterDTO.getEmail(),
                userRegisterDTO.getUsername(), userRegisterDTO.getPassword(), userRegisterDTO.getAddress());

        if(this.userRepository.all().size() == 0) {
            this.roleService.insert(new Role("ADMIN"));
            this.roleService.insert(new Role("USER"));

            role = this.roleService.get("ADMIN");
        }

        else {
            role = this.roleService.get("USER");
        }

        role.getUsers().add(user);
        user.setRole(role);

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