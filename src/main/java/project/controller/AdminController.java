package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.model.DTOS.ProductDTO;
import project.service.ProductService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private ProductService productService;

    @Autowired
    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute ProductDTO productDTO, String categoryName) {
        try {
            productService.add(productDTO, categoryName);
        } catch (NullPointerException e) {
            return "redirect:/error";
        }
        return "admin";
    }

    @PostMapping("/removeProduct")
    public String removeProduct(@ModelAttribute ProductDTO productDTO) {
        try {
            productService.remove(productDTO.getName());
        } catch (NullPointerException e) {
            return "redirect:/error";
        }
        return "admin";
    }

}
