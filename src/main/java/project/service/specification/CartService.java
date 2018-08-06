package project.service.specification;

import project.exception.ProductAlreadyInCartException;
import project.model.DTOS.ProductDTO;

import java.util.List;


public interface CartService {

    void create();

    void addProduct(long cartId, long productId) throws ProductAlreadyInCartException;

    void removeProduct(long cartId, long productId);

    void removeAll(long cartId);

    List<ProductDTO> getProducts(long cartId);

    double getTotalPrice(long cartId);
}