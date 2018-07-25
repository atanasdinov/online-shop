package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class HomeController {

//    private ProductService productService;
//    private CategoryService categoryService;
//
//    @Autowired
//    public HomeController(ProductService productService, CategoryService categoryService) {
//        this.productService = productService;
//        this.categoryService = categoryService;
//    }

    @GetMapping({"", "/home"})
    public String index(Model model) {
        return "home";
    }

}