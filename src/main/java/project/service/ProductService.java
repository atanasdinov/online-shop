package project.Service;

import project.model.DTOS.ProductDTO;

import java.util.List;

public interface ProductService {
    void add(ProductDTO productDTO, String categoryName);

    ProductDTO get(String productName);

    boolean remove(String productName);

    List<ProductDTO> all();

    List<ProductDTO> allFromCategory(String categoryName);
}