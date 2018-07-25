package project.repositories;


import project.model.entities.Product;

import java.util.List;

public interface CartRepository {
    void persist();

    void addProduct(int cartId, Product product);

    void removeProduct(int cartId, Product product);

    void removeAllProducts(int cartId);

    List<Product> getProducts(int cartId);

    boolean doesExist(int cartId);
}
