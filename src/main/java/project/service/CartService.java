package project.Service;

import org.springframework.stereotype.Service;
import project.exception.InvalidCartException;
import project.exception.InvalidProductException;
import project.model.DTOS.ProductDTO;

import java.util.List;

@Service
public interface CartService {
    void create();

    void addProduct(int cartId, String productName) throws InvalidProductException, InvalidCartException;

    void addProduct(int cartId, ProductDTO productDTO) throws InvalidProductException, InvalidCartException;

    void removeProduct(int cartId, String productName) throws InvalidProductException, InvalidCartException;

    void removeAll(int cartId) throws InvalidCartException;

    List<ProductDTO> getProducts(int cartId);
}
