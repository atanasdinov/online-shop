package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.exception.InvalidCartException;
import project.exception.InvalidProductException;
import project.model.DTOS.ProductDTO;
import project.service.CartService;

@Controller
@RequestMapping("/browse")
public class CartController {

    private CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // TODO: Use "Principal" in order to get a given user's cartId and update it as required...

    @PostMapping("/addItem")
    public String addToCart(@ModelAttribute ProductDTO productDTO) {
        try {
            cartService.addProduct(0, productDTO);
        } catch (InvalidProductException | InvalidCartException e) {
            return "redirect:/error";
        }
        return "cart";
    }

    @PostMapping("/removeItem")
    public String removeFromCart(@ModelAttribute ProductDTO productDTO) {
        try {
            cartService.removeProduct(0, productDTO.getName());
        } catch (InvalidProductException | InvalidCartException e) {
            return "redirect:/error";
        }
        return "cart";
    }

    @PostMapping("/cart/purchase")
    public String purchase() {
        // TODO (in a later release): Update database after purchase is finalized!
        return "redirect:/success";     // TODO: A simple HTML page for a successful purchase
    }

    @GetMapping("/cart/{cartId}")
    public String showCart(@PathVariable("cartId") int cartId) {
        try {
            cartService.getProducts(cartId);
        } catch (NullPointerException e) {
            return "redirect:/error";
        }
        return "cart";
    }

    @PostMapping("/cart/{cartId}")
    public String emptyCart(@PathVariable("cartId") int cartId) {
        try {
            cartService.removeAll(cartId);
        } catch (InvalidCartException e) {
            return "redirect:/error";
        }
        return "cart";
    }
}