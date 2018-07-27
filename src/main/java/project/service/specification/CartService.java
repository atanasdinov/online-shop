package project.service.specification;

import org.springframework.stereotype.Service;
import project.exception.InvalidCartException;
import project.exception.InvalidProductException;
import project.model.DTOS.ProductDTO;

import java.util.List;


public interface CartService {

    void create();

    void addProduct(long cartId, long productId) throws InvalidProductException, InvalidCartException;

    void removeProduct(long cartId, long productId) throws InvalidProductException, InvalidCartException;

    void removeAll(long cartId) throws InvalidCartException;

    List<ProductDTO> getProducts(long cartId) throws InvalidCartException;

    double getTotalPrice(long cartId) throws InvalidCartException;
}