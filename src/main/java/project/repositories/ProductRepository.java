package project.repositories;

import project.model.Product;

import java.util.List;

public interface ProductRepository {
    void add(Product product);
    Product get(String name);
    List<Product> all();
}