package project.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.exception.ProductAlreadyExistsException;
import project.model.DTOS.ProductDTO;
import project.model.entities.Category;
import project.model.entities.Product;
import project.repository.specification.CategoryRepository;
import project.repository.specification.ProductRepository;
import project.service.specification.ProductService;

import java.util.ArrayList;
import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public void add(ProductDTO productDTO) throws ProductAlreadyExistsException{
        if(productRepository.doesExist(productDTO.getName()))
            throw new ProductAlreadyExistsException("Product already exists!");

        if (!categoryRepository.doesExist(productDTO.getCategoryName()))
            categoryRepository.persist(new Category(productDTO.getCategoryName()));

        Category category = modelMapper.map(categoryRepository.getCategory(productDTO.getCategoryName()), Category.class);
        Product product = new Product(category, productDTO.getPrice(), productDTO.getName(), productDTO.getQuantity());

        productRepository.persist(product);
    }

    @Override
    public void editProduct(long id, ProductDTO productDTO, long categoryId) {
        productRepository.edit(id, productDTO, categoryId);
    }

    @Override
    public boolean removeProductById(long id) {
        productRepository.delete(id);
        return false;
    }

    @Override
    public ProductDTO getProductById(long productId) {
        Product product = productRepository.get(productId);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : productRepository.getAllProducts())
            productDTOS.add(modelMapper.map(product, ProductDTO.class));

        return productDTOS;
    }

    @Override
    public List<ProductDTO> getAllProductsFromCategory(String categoryName) {
        List<Product> products = productRepository.getAllProductsByCategory(categoryName);
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products)
            productDTOS.add(modelMapper.map(product, ProductDTO.class));

        return productDTOS;
    }

    @Override
    public List<ProductDTO> foundProducts(String searchedBy) {
        List<Product> products = this.productRepository.productsMatching(searchedBy);
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products)
            productDTOS.add(modelMapper.map(product, ProductDTO.class));

        return productDTOS;
    }
}