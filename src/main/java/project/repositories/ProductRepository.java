package project.repositories;

import org.springframework.stereotype.Repository;
import project.model.Product;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public interface ProductRepository {
    void add(Product product);
    Product get(String name);
    List<Product> all();
}
