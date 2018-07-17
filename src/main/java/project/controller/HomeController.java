package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.Service.CategoryService;
import project.Service.ProductService;
import project.model.DTOS.CategoryDTO;
import project.model.DTOS.ProductDTO;


@Controller
@RequestMapping("/")
public class HomeController {

    private ProductService productService;
    private CategoryService categoryService;

    @Autowired
    public HomeController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping()
    public String index(Model model){
        categoryService.create(new CategoryDTO("aaa"));
        productService.add( new ProductDTO(234.0,"asfdsf","5.5",5),"aaa");
        model.addAttribute("product",this.productService.get("asfdsf").getName());
        return "home";
    }



}