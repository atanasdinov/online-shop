package project.Service;

import project.model.DTOS.ProductDTO;

import java.util.List;

public interface ProductService {
    void add(ProductDTO productDTO);

    ProductDTO get(long id);

    boolean remove(long id);

    List<ProductDTO> all();

    List<ProductDTO> allFromCategory(String categoryName);
}