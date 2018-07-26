package project.repositories;

import project.model.entities.Product;

import java.util.List;

public interface ProductRepository {
    void persist(Product product);

    void delete(long id);

    Product get(long id);

    List<Product> all();

    List<Product> allByCategory(String categoryName);
}