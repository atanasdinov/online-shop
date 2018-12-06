package com.scalefocus.shop.controller;

import com.scalefocus.shop.model.entity.User;
import com.scalefocus.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * <b>User controller used to handle all logic that is user related.</b>
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Returns the "register" form/page for new registration.
     *
     * @param model model attributes
     * @return the logical name of the view
     */
    @PreAuthorize("isAnonymous()")
    @GetMapping("/register")
    public String registerForm(Model model) {
        if (!model.containsAttribute("user"))
            model.addAttribute("user", new User());

        return "register";
    }

    /**
     * Submits a filled "register" form for new user registration.
     *
     * @param userData      contains user data
     * @param bindingResult contains errors from form validation (if any)
     * @param request       {@link WebRequest}
     * @return user login page
     */
    @PreAuthorize("isAnonymous()")
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User userData,
                           BindingResult bindingResult,
                           WebRequest request) {
        userService.register(userData, bindingResult, request);

        return "redirect:/user/login";
    }

    /**
     * Returns a "login" form/page.
     *
     * @param model model attributes
     * @return the logical name of the view
     */
    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new User());

        return "login";
    }

    /**
     * Submits a filled-in user login form.
     *
     * @param userData user credentials
     * @param response response
     * @return homepage
     */
    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public String login(@ModelAttribute User userData,
                        HttpServletResponse response) {
        userService.login(userData, response);

        return "redirect:/home";
    }

    /**
     * Sends a "logout" request for the currently logged {@link User}.
     *
     * @param cookie   currently logged user's cookie
     * @param response {@link HttpServletResponse}
     * @return the logical name of the view
     */
    @PreAuthorize(value = "isAuthenticated()")
    @GetMapping("/logout")
    public String logout(@CookieValue(value = "token", required = false) Cookie cookie,
                         HttpServletResponse response) {
        if (cookie != null) {
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return "redirect:/home";
    }

    /**
     * Registration confirmation for a newly registered user by a given token.
     *
     * @param token generated registration token
     * @return the logical name of the view
     */
    @PreAuthorize(value = "isAnonymous()")
    @GetMapping(value = "/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token) {
        userService.activateAccount(token);

        return "redirect:/user/login";
    }

    /**
     * Returns a "expired token" page.
     *
     * @return the logical name of the view
     */
    @PreAuthorize(value = "isAnonymous()")
    @GetMapping(value = "/tokenExpired")
    public String expiredActivationToken() {
        return "expired-token";
    }

    /**
     * User registration confirmation re-send.
     *
     * @param username {@link User}'s username
     * @param request  Web Request data
     * @return the logical name of the view
     */
    @PreAuthorize(value = "isAnonymous()")
    @GetMapping(value = "/resendRegistrationConfirmation")
    public String resendRegistrationConfirmation(@RequestParam("username") String username,
                                                 WebRequest request) {
        userService.resendToken(username, request);

        return "redirect:/home";
    }

    /**
     * Show a list of all registered users.
     *
     * @param model model attributes
     * @return list of users
     */
    @PreAuthorize(value = "hasRole('ADMIN')")
    @GetMapping(value = "/userList")
    public String showUserList(Model model) {
        model.addAttribute("users", userService.getAllUsers());

        return "user-list";
    }

    /**
     * Change user's role to 'MODERATOR'.
     *
     * @param username of the selected user
     */
    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping(value = "/setModeratorRole")
    public String makeModerator(@RequestParam("username") String username) {
        userService.setModRole(username);

        return "redirect:/user/userList";
    }

    /**
     * Change user's role to 'USER'.
     *
     * @param username of the selected user
     */
    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping(value = "/setUserRole")
    public String makeUser(@RequestParam("username") String username) {
        userService.setUserRole(username);

        return "redirect:/user/userList";
    }
}
