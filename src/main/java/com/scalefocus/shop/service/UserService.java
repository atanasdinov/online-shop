package com.scalefocus.shop.service;

import com.scalefocus.shop.enumeration.RoleType;
import com.scalefocus.shop.exception.*;
import com.scalefocus.shop.model.entity.Cart;
import com.scalefocus.shop.model.entity.Role;
import com.scalefocus.shop.model.entity.User;
import com.scalefocus.shop.model.entity.VerificationToken;
import com.scalefocus.shop.repository.specification.CartRepository;
import com.scalefocus.shop.repository.specification.RoleRepository;
import com.scalefocus.shop.repository.specification.UserRepository;
import com.scalefocus.shop.repository.specification.VerificationTokenRepository;
import com.scalefocus.shop.service.email.EmailSenderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * <b>This service declares all {@link User}-related manipulations and actions.</b>
 */
@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private CartRepository cartRepository;
    private VerificationTokenRepository verificationTokenRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, CartRepository cartRepository,
                       VerificationTokenRepository verificationTokenRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cartRepository = cartRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    @DependsOn(value = "tableCreation")
    public Void createRoles() {
        if (!roleRepository.rolesExist()) {
            roleRepository.createRole(new Role(RoleType.ADMIN.toString()));
            roleRepository.createRole(new Role(RoleType.MODERATOR.toString()));
            roleRepository.createRole(new Role(RoleType.USER.toString()));

            logger.info("ADMIN, MODERATOR and USER roles created.");
        }
        return null;
    }

    /**
     * This method is used for {@link User} registration.
     *
     * @param userData      contains the data from the register form.
     * @param bindingResult contains contains errors from form validation (if any).
     * @param request       the web request.
     */
    public void register(User userData, BindingResult bindingResult, WebRequest request) {
        if (bindingResult.hasErrors())
            throw new InvalidUserDataException("Invalid user data!", userData, bindingResult);

        if (userRepository.userExists(userData.getUsername()))
            throw new UsernameAlreadyExistsException("The username already exists!", userData);

        if (userRepository.emailExists(userData.getEmail()))
            throw new EmailAlreadyExistsException("There is already a registered user with this email!", userData);

        User user = new User(
                userData.getFirstName(),
                userData.getLastName(),
                userData.getEmail(),
                userData.getUsername(),
                passwordEncoder.encode(userData.getPassword()),
                userData.getAddress());
        Role role;

        if (userRepository.usersCount() == 0)
            role = roleRepository.getRole(RoleType.ADMIN.toString());
        else
            role = roleRepository.getRole(RoleType.USER.toString());

        Cart cart = cartRepository.createCart();
        logger.info("Cart created.");

        user.setCart(cart);
        cart.setUser(user);
        role.getUsers().add(user);
        user.setRole(role);

        userRepository.addUser(user);
        logger.info("User created.");

        sendActivationEmail(userRepository.getUser(user.getUsername()), request);
    }

    /**
     * This method sends email to the user.
     *
     * @param user    who receives the email.
     * @param request the web request.
     */
    private void sendActivationEmail(User user, WebRequest request) {
        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new EmailSenderEvent(user, request.getLocale(), appUrl));
        logger.info("Activation email sent.");
    }

    /**
     * This method is used for logging-in an existing {@link User}.
     *
     * @param userData containing the data from the form.
     * @param response http response for adding the cookie.
     */
    public void login(User userData, HttpServletResponse response) {
        User user;

        try {
            user = userRepository.getUser(userData.getUsername());
        } catch (EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("Username not found!");
        }

        String password = user.getPassword();
        final int COOKIE_EXPIRATION_30_MIN = 60 * 30;

        if (!user.isEnabled())
            throw new AccountNotValidatedException("Account not validated!");
        else if (!passwordEncoder.matches(userData.getPassword(), password))
            throw new IncorrectPasswordException("Incorrect password!");
        else {
            Cookie cookie = new Cookie("token", this.addToken(user));
            cookie.setPath("/");
            cookie.setMaxAge(COOKIE_EXPIRATION_30_MIN);
            response.addCookie(cookie);
            logger.info("User logged in.");
        }
    }

    /**
     * This method is used for checking {@link User}'s token upon login.
     *
     * @param token generated token.
     * @return found user.
     */
    public User checkToken(String token) {
        if (token == null)
            return null;

        return userRepository.checkToken(token);
    }

    /**
     * This method adds a {@link VerificationToken} to a {@link User}.
     *
     * @param user
     * @return generated token.
     */
    private String addToken(User user) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];

        random.nextBytes(bytes);
        String token = Arrays.toString(bytes);

        userRepository.setToken(token, user);
        logger.info("Token set for user.");
        return token;
    }

    /**
     * This method is used to activate a {@link User}'s account if confirmation link has not expired.
     *
     * @param token generated registration token
     */
    public void activateAccount(String token) {
        Calendar cal = Calendar.getInstance();

        VerificationToken verificationToken = verificationTokenRepository.getToken(token);
        User user = verificationToken.getUser();

        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0)
            throw new ActivationLinkExpiredException("Activation link has expired!", user.getUsername());

        userRepository.setEnabled(user.getUsername());
        logger.info("User enabled.");
    }

    /**
     * This method creates {@link User}'s token.
     *
     * @param user  the User to be given a token.
     * @param token the generated token.
     */
    public void createVerificationToken(User user, String token) {
        verificationTokenRepository.addToken(new VerificationToken(token, user));
        logger.info("Verification token created.");
    }

    /**
     * This method deletes {@link User}'s token and re-sends new token.
     *
     * @param username {@link User}'s username
     */
    public void resendToken(String username, WebRequest request) {
        User user = userRepository.getUser(username);
        verificationTokenRepository.deleteToken(user.getId());

        logger.info("Verification token deleted.");

        sendActivationEmail(user, request);
    }

    /**
     * This method returns all registered users.
     *
     * @return list of users
     */
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    /**
     * This method is used to give a user 'MODERATOR' role.
     *
     * @param username of the selected user
     */
    public void setModRole(String username) {
        userRepository.changeRole(username, RoleType.MODERATOR.toString());
    }

    /**
     * This method is used to give a user 'USER' role.
     *
     * @param username of the selected user
     */
    public void setUserRole(String username) {
        userRepository.changeRole(username, RoleType.USER.toString());
    }

}
