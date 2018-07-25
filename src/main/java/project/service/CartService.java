package project.Service;

import org.springframework.stereotype.Service;
import project.model.DTOS.ProductDTO;

import java.util.List;

@Service
public interface CartService {
    void create();
    void addProduct(int cartId, String productName);
    void removeProduct(int cartId, String productName);
    List<ProductDTO> getProducts(int cartId);
}
