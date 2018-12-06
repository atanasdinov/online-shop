package com.scalefocus.shop.controller;

import com.scalefocus.shop.model.entity.Cart;
import com.scalefocus.shop.model.entity.Product;
import com.scalefocus.shop.model.entity.User;
import com.scalefocus.shop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * <b>Cart controller used to handle all logic that is cart-relevant.</b>
 */
@Controller
@PreAuthorize("hasAnyRole('USER','MODERATOR')")
@RequestMapping("/cart")
public class CartController {

    private CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Returns a view of the current logged-in {@link User}'s {@link Cart}.
     *
     * @param model     model attributes
     * @param principal current logged-in user
     * @return the logical name of the view
     */
    @GetMapping
    public String viewCart(Model model, Principal principal) {
        model.addAttribute("cart", cartService.getCart(principal.getName()));

        return "cart";
    }

    /**
     * Adds a given {@link Product} to a given {@link User}'s {@link Cart}.
     *
     * @param productId  product's id
     * @param pageNumber current page number
     * @param principal  current logged-in user
     * @return the logical name of the view
     */
    @PostMapping("/addItem/{productId}")
    public String addToCart(@PathVariable("productId") long productId,
                            @RequestParam(value = "pageNumber") String pageNumber,
                            Principal principal) {
        cartService.addProduct(principal.getName(), productId, pageNumber);

        return "redirect:/cart";
    }

    /**
     * Removes a given {@link Product} from a given {@link User}'s {@link Cart}.
     *
     * @param productId product's id
     * @param principal current logged-in user
     * @return the logical name of the view
     */
    @PostMapping("/removeItem/{productId}")
    public String removeFromCart(@PathVariable("productId") Long productId,
                                 Principal principal) {
        cartService.removeProduct(principal.getName(), productId);

        return "redirect:/cart";
    }

    /**
     * Removes all products from a given {@link User}'s cart.
     *
     * @param cartId the {@link Cart} to be emptied
     * @return the logical name of the view
     */
    @PostMapping("/emptyCart/{cartId}")
    public String emptyCart(@PathVariable("cartId") long cartId) {
        cartService.removeAll(cartId);

        return "redirect:/cart";
    }
}