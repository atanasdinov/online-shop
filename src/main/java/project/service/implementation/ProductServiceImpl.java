package project.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public void add(ProductDTO productDTO) {
        if (productDTO.getCategoryName() == null)
            throw new NullPointerException("Category name must not be null!");

        if (!categoryRepository.doesExist(productDTO.getCategoryName()))
            categoryRepository.persist(new Category(productDTO.getCategoryName()));

        Category category = modelMapper.map(categoryRepository.getCategory(productDTO.getCategoryName()), Category.class);
        Product product = new Product(category, productDTO.getPrice(), productDTO.getName(), productDTO.getQuantity());

        productRepository.persist(product);
    }

    @Override
    public boolean removeProductById(long id) {
        if (id < 1)
            throw new NullPointerException("Product name must not be null!");

        productRepository.delete(id);
        return false;
    }

    @Override
    public ProductDTO getProductById(long productId) {
        if (productId < 1)
            throw new NullPointerException("Product name must not be null");

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
        if (categoryName == null)
            throw new NullPointerException("category name must not be null");

        List<Product> products = productRepository.getAllProductsByCategory(categoryName);
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products)
            productDTOS.add(modelMapper.map(product, ProductDTO.class));

        return productDTOS;
    }

}