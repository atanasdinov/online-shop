package project.repositories;


import project.model.entities.Cart;
import project.model.entities.Product;

import java.util.List;

public interface CartRepository {
    void persist();

    void addProduct(long cartId, Product product);

    void removeProduct(long cartId, Product product);

    void removeAllProducts(long cartId);

    List<Product> getProducts(long cartId);

    boolean doesExist(long cartId);

    Cart getCart(long cartId);

    List<Cart> all();

    double getTotalPrice(long cartId);

    boolean doesProductExist(long cartId, Product product);
}