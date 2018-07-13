package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.Service.ProductService;
import project.Service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register() {

        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login() {
        return "home";
    }
}
//    public String index(Model model){
//    this.userService.add("Gosho", "Petrov", "peshetu", "123456", "Plovdiv");
//        model.addAttribute("user", this.userService.get("peshetu").getUsername());
//}
