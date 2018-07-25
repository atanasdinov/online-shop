package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.model.DTOS.ProductDTO;
import project.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/browse")
    public String loginForm() {
        return "browse";
    }

    @PostMapping("/products")
    public String add(@ModelAttribute ProductDTO productDTO) {
        productService.add(productDTO, "Cars");
        return "redirect:products/browse";
    }



    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }
}
