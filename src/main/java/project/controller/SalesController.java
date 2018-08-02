package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.exception.InvalidCartException;
import project.exception.QuantityNotAvailableException;
import project.model.entities.Sale;
import project.service.specification.CartService;
import project.service.specification.ProductService;
import project.service.specification.SaleService;
import project.service.specification.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/sales")
public class SalesController {

    private CartService cartService;
    private SaleService saleService;
    private UserService userService;
    private ProductService productService;

    @Autowired
    public SalesController(CartService cartService, SaleService saleService, UserService userService, ProductService productService) {
        this.cartService = cartService;
        this.saleService = saleService;
        this.userService = userService;
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/purchase")
    public String purchaseItems(Model model){
        try {
            List<Sale> allSales = saleService.getAllSales();
            model.addAttribute("sales", allSales);
        } catch (NullPointerException e) {
            return "error";
        }
        return "purchase";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/purchase")
    public String purchase(@RequestParam("productName") List<String> productName,
                           @RequestParam("price") List<String> price,
                           @RequestParam("productId") List<String> productId,
                           @RequestParam("productQuantity") List<String> productQuantity,
                           RedirectAttributes redirectAttributes,
                           Principal principal) throws InvalidCartException {

        try {
            saleService.add(principal.getName(), productName, price, productId, productQuantity);
        } catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("invalidQuantity", "You must enter a valid quantity!");

            return "redirect:/cart";
        } catch (QuantityNotAvailableException qe) {
            redirectAttributes.addFlashAttribute("invalidQuantity", "Selected quantity is not available!");

            return "redirect:/cart";
        }

        cartService.removeAll(userService.getUser(principal.getName()).getCart().getId());

        return "redirect:/home";
    }
}
