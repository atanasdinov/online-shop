package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.model.DTOS.UserLoginDTO;
import project.model.DTOS.UserRegisterDTO;
import project.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserRegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    //@PreAuthorize(value = "hasAnyRole()")
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
    public String login(@ModelAttribute UserLoginDTO user) {
        userService.login(user);
        return "home";
    }

}
