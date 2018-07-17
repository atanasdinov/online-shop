package project.Service;

import project.model.DTOS.ProductDTO;
import project.model.entities.Category;
import project.model.entities.Product;

import java.util.List;

public interface ProductService {
    void add(ProductDTO productDTO, String categoryName);
    ProductDTO get(String name);
    List<ProductDTO> all();
    List<ProductDTO> allFromCategory(String categoryName);
}