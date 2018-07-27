package project.repository.specification;

import project.model.entities.Cart;
import project.model.entities.Product;

import java.util.List;


public interface CartRepository {

    void persist();

    void addProductToCart(long cartId, Product product);

    void removeProductFromCart(long cartId, Product product);

    void removeAllProductsFromCart(long cartId);

    List<Product> getAllProductsFromCart(long cartId);

    boolean doesExist(long cartId);

    Cart getCart(long cartId);

    List<Cart> getAllCarts();

    double getTotalPrice(long cartId);

    boolean doesProductExist(long cartId, Product product);
}