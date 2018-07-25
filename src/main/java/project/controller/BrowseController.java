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

    /* TODO:
     *   showAll() методът взима от базата с данни наличните
     *   продукти и ги записва в списъка allProducts.
     *   От там нататък вече трябва да се навържат с front-end.
     */
    @GetMapping("/showAll")
    public ResponseEntity<List<ProductDTO>> showAll() {
        List<ProductDTO> allProducts = productService.all();
        return ResponseEntity.ok(allProducts);
    }
}
