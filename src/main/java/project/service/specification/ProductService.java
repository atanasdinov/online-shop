package project.service.specification;

import project.exception.ProductAlreadyExistsException;
import project.model.DTOS.ProductDTO;

import java.util.List;


public interface ProductService {

    void add(ProductDTO productDTO) throws ProductAlreadyExistsException;

    void editProduct(long id, ProductDTO productDTO, long categoryId);

    boolean removeProductById(long id);

    ProductDTO getProductById(long id);

    List<ProductDTO> getAllProducts();

    List<ProductDTO> getAllProductsFromCategory(String categoryName);

    List<ProductDTO> foundProducts(String searchedBy);

}