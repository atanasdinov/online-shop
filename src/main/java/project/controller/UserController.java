package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.Service.UserService;
import project.model.DTOS.UserLoginDTO;
import project.model.DTOS.UserRegisterDTO;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserRegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserRegisterDTO userRegisterDTO) {
        userService.register(userRegisterDTO);
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new UserLoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserLoginDTO user,
                        HttpServletResponse response) {

        if(!userService.login(user, response))
            return "redirect:/user/login?error";

        else{
            return "redirect:/home";
        }

    }


    @GetMapping("/logout")
    @PreAuthorize(value = "isAuthenticated()")
    public String logout(@CookieValue(value = "token", required = false) Cookie cookie,
                         HttpServletResponse response) {

        if(cookie!=null) {
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return "redirect:/home";
    }

}
