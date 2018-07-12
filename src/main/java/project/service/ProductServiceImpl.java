package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.model.Product;
import project.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void add(String name, Double price) {
        this.productRepository.add(new Product(price,name));
    }

    @Override
    public Product get(String name) {
        return productRepository.get(name);
    }
}
