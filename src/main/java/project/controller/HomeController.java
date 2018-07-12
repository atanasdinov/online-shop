package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.service.ProductService;

@Controller
@RequestMapping("/products")
public class HomeController {

    @Autowired
    private ProductService productService;


    @GetMapping()
    public String index(Model model){
        this.productService.add("BMW 5 2003",5600.0);
        model.addAttribute("product",this.productService.get("BMW 5 2003").getName());
        return "home";
    }
}