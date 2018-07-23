package project.service;

import project.model.DTOS.ProductDTO;

import java.util.List;

public interface ProductService {
    void add(ProductDTO productDTO, String categoryName);
    ProductDTO get(String name);
    List<ProductDTO> all();
    List<ProductDTO> allFromCategory(String categoryName);
}