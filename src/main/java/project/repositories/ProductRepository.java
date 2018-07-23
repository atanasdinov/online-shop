package project.repositories;

import project.model.entities.Category;
import project.model.entities.Product;

import java.util.List;

public interface ProductRepository {
    void persist(Product product);
    Product get(String name);
    List<Product> all();
    List<Product> allByCategory(String categoryName);
}
