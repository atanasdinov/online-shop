package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.model.DTOS.UserLoginDTO;
import project.model.DTOS.UserRegisterDTO;
import project.model.entities.User;
import project.service.specification.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserRegisterDTO());
        model.addAttribute("usernameDuplication", null);
        return "register";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserRegisterDTO userRegisterDTO,
                           BindingResult bindingResult,
                           Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("user", userRegisterDTO);
            return "register";
        }

        if(userService.doesExist(userRegisterDTO.getUsername())){
            model.addAttribute("usernameDuplication", "Username already exists!");
            return "register";
        }

        userService.register(userRegisterDTO);
        return "redirect:/user/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new UserLoginDTO());
        model.addAttribute("userNotFound", null);
        model.addAttribute("incorrectPassword", null);
        return "login";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public String login(@ModelAttribute UserLoginDTO userLoginDTO,
                        HttpServletResponse response,
                        Model model) {

        if(!userService.doesExist(userLoginDTO.getUsername())){
            model.addAttribute("userNotFound", "Username not found!");
            return "login";
        }

        User user = userService.getUser(userLoginDTO.getUsername());
        if(!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())){
            model.addAttribute("incorrectPassword", "Incorrect password!");
            return "login";
        }
        if(!userService.login(userLoginDTO, response))
            return "redirect:/user/login?error";
        else {
            return "redirect:/home";
        }
    }

    @GetMapping("/logout")
    @PreAuthorize(value = "isAuthenticated()")
    public String logout(@CookieValue(value = "token", required = false) Cookie cookie,
                         HttpServletResponse response) {

        if (cookie != null) {
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return "redirect:/home";
    }
}
