package project.repository.specification;

import project.model.DTOS.ProductDTO;
import project.model.entities.Product;

import java.util.List;


public interface ProductRepository {

    void persist(Product product);

    void edit(long id, ProductDTO productDTO, long categoryId);

    void delete(long id);

    Product get(long id);

    Boolean doesExist(String name);

    List<Product> getAllProducts();

    List<Product> getAllProductsByCategory(String categoryName);

    void decreaseQuantity(long id, int quantity);

}
