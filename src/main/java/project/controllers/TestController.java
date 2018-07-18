package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import project.model.DTOS.ProductDTO;
import project.service.CartService;
import project.service.ProductService;

@Controller
public class TestController {
    private CartService cartService;
    private ProductService productService;

    @Autowired
    public TestController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping
    public void test() {
        this.cartService.create();
        ProductDTO productDTO = new ProductDTO(432.4, "Sony Headphones", "5.4", 4);
        ProductDTO productDTO1 = new ProductDTO(432.4, "PlayStation 4", "5.6", 5);
        ProductDTO productDTO2 = new ProductDTO(432.4, "Xbox 720", "5.8", 3);
        this.productService.add(productDTO, "Headphones");
        this.productService.add(productDTO1, "Consoles");
        this.productService.add(productDTO2, "Consoles");
        this.cartService.addProduct(1,productDTO.getName());
        this.cartService.addProduct(1,productDTO1.getName());
        this.cartService.addProduct(1,productDTO2.getName());
        this.cartService.getProducts(1);
        this.cartService.removeProduct(1, productDTO.getName());
    }
}
