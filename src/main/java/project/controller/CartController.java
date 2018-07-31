package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import project.exception.InvalidCartException;
import project.exception.InvalidProductException;
import project.model.DTOS.ProductDTO;
import project.model.entities.User;
import project.service.specification.CartService;
import project.service.specification.ProductService;
import project.service.specification.UserService;
import project.service.specification.CartService;
import project.service.specification.ProductService;
import project.service.specification.SaleService;
import project.service.specification.UserService;

import java.security.Principal;
import java.util.List;


@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/cart")
public class CartController {

    private CartService cartService;
    private UserService userService;
    private ProductService productService;

    @Autowired
    public CartController(CartService cartService, UserService userService, ProductService productService, SaleService saleService) {
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public String viewCart(Model model, Principal principal) {
        User loggedIn = userService.getUser(principal.getName());
        try {
            List<ProductDTO> allProducts = cartService.getProducts(loggedIn.getCart().getId());
            model.addAttribute("cartId", loggedIn.getCart().getId());
            model.addAttribute("products", allProducts);
            model.addAttribute("totalPrice", cartService.getTotalPrice(loggedIn.getCart().getId()));
        } catch (InvalidCartException e) {
            return "redirect:/error";
        }
        try {
            List<ProductDTO> allProducts = cartService.getProducts(loggedIn.getCart().getId());
            model.addAttribute("cartId", loggedIn.getCart().getId());
            model.addAttribute("products", allProducts);
            model.addAttribute("totalPrice", cartService.getTotalPrice(loggedIn.getCart().getId()));
        } catch (InvalidCartException e) {
            return "error";
        }
        return "cart";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/addItem/{productId}")
    public String addToCart(@PathVariable("productId") long productId, Principal principal) {
        try {
            User loggedIn = userService.getUser(principal.getName());
            cartService.addProduct(loggedIn.getCart().getId(), productId);
        } catch (InvalidProductException | InvalidCartException e) {
            return "redirect:/cart";
        }
        return "redirect:/cart";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/removeItem/{productId}")
    public String removeFromCart(@PathVariable("productId") Long productId, Principal principal) {
        try {
            User loggedIn = userService.getUser(principal.getName());
            cartService.removeProduct(loggedIn.getCart().getId(), productId);
        } catch (InvalidProductException | InvalidCartException e) {
            return "redirect:/cart";
        }
        return "redirect:/cart";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/emptyCart/{cartId}")
    public String emptyCart(@PathVariable("cartId") long cartId) {
        try {
            cartService.removeAll(cartId);
        } catch (InvalidCartException e) {
            return "error";
        }
        return "redirect:/cart";
    }
}