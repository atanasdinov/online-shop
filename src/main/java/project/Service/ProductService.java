package project.Service;

import project.model.Product;

public interface ProductService {
    void add(String name, Double price);
    Product get(String name);
}