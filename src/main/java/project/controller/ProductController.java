package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.Service.ProductService;
import project.model.DTOS.ProductDTO;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/browse")
    public String browse(Model model) {
        List<ProductDTO> allProducts = productService.all();

        model.addAttribute("productList", allProducts);
        return "browse";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String createProductForm(@ModelAttribute ProductDTO productDTO,
                                Model model){
        model.addAttribute("product", productDTO);

        return "create-product";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String createProduct(@ModelAttribute ProductDTO productDTO,
                                Model model){
        productService.add(productDTO);

        return "redirect:/products/browse";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/remove/{id}")
    public String removeProduct(@PathVariable ("id") long id) {
        try {
            productService.remove(id);
        } catch (NullPointerException e) {
            return "redirect:/error";
        }
        return "redirect:/products/browse";
    }
}
