package project.service.specification;

import project.model.DTOS.ProductDTO;

import java.util.List;


public interface ProductService {

    void add(ProductDTO productDTO);

    ProductDTO getProductById(long id);

    boolean removeProductById(long id);

    List<ProductDTO> getAllProducts();

    List<ProductDTO> getAllProductsFromCategory(String categoryName);
}