package project.repository.specification;

import project.model.entities.Product;

import java.util.List;


public interface ProductRepository {

    void persist(Product product);

    void delete(long id);

    Product get(long id);

    List<Product> getAllProducts();

    List<Product> getAllProductsByCategory(String categoryName);

    void decreaseQuantity(long id, int quantity);

    int getProductQuantity(long id);
}
