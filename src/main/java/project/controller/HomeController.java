package project.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import java.security.Principal;


@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping({"", "/home"})
    public String index(Model model,
                        @CookieValue(value = "token", required = false) Cookie cookie,
                        Principal principal) {

        if (cookie != null)
            model.addAttribute("username", principal.getName());
        return "home";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/thanks")
    public String thanks() {
        return "thanks";
    }
}