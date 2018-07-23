package project.service;

import org.springframework.stereotype.Service;
import project.model.DTOS.ProductDTO;
import project.model.entities.Product;

import java.util.List;

@Service
public interface CartService {
    void create();
    void addProduct(int cartId, String productName);
    void removeProduct(int cartId, String productName);
    List<ProductDTO> getProducts(int cartId);
}
