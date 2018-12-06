package com.scalefocus.shop.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import java.security.Principal;


/**
 * <b>Home page controller.</b>
 */
@Controller
@RequestMapping("/")
public class HomeController {

    /**
     * Returns a logical view of the ScaleShop's homepage.
     *
     * @param model     model attributes
     * @param cookie    {@link Cookie}
     * @param principal the current logged-in user
     * @return the logical name of the view
     */
    @GetMapping({"", "home"})
    public String index(Model model,
                        @CookieValue(value = "token", required = false) Cookie cookie,
                        Principal principal) {
        if (cookie != null)
            model.addAttribute("username", principal.getName());

        return "home";
    }

    /**
     * Returns a logical view of the ScaleShop's "contact" page.
     *
     * @return the logical name of the view
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("contact")
    public String contact() {
        return "contact";
    }

    /**
     * Returns a logical view of the ScaleShop's "thanks" page.
     *
     * @return the logical name of the view
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("thanks")
    public String thanks() {
        return "thanks";
    }

    /**
     * Returns a logical view of the ScaleShop's "success" page.
     *
     * @return the logical name of the view
     */
    @PreAuthorize("hasAnyRole('USER','MODERATOR')")
    @GetMapping("success")
    public String success() {
        return "purchase-success";
    }

}
