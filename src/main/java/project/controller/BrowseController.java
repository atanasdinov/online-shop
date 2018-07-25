package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.model.DTOS.ProductDTO;
import project.service.ProductService;

import java.util.List;

@Controller
@RequestMapping("/browse")
public class BrowseController {

    private ProductService productService;

    @Autowired
    public BrowseController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/showAll")
    public ResponseEntity<List<ProductDTO>> showAll() {
        List<ProductDTO> allProducts = productService.all();    // All the products available in the DB
        return ResponseEntity.ok(allProducts);
    }
}
