package project.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.DTOS.ProductDTO;
import project.model.entities.Category;
import project.model.entities.Product;
import project.repositories.CategoryRepository;
import project.repositories.ProductRepository;

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
    public void add(ProductDTO productDTO, String categoryName) {
        if(categoryName==null) {
            throw new NullPointerException("category name must not be null");
        }
        if(!categoryRepository.doExist(categoryName)) {
            this.categoryRepository.persist(new Category(categoryName));
        }
        Category category = this.modelMapper.map(categoryRepository.get(categoryName), Category.class);
        Product product = new Product(category,productDTO.getPrice(), productDTO.getName(), productDTO.getQuantity());
        this.productRepository.persist(product);
    }

    @Override
    public ProductDTO get(String name) {
        if(name==null) {
            throw new NullPointerException("product name must not be null");
        }
        Product product = this.productRepository.get(name);
        return modelMapper.map(product,ProductDTO.class);
    }

    @Override
    public List<ProductDTO> all() {
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : this.productRepository.all()) {
            productDTOS.add(this.modelMapper.map(product,ProductDTO.class));
        }
        return productDTOS;
    }

    @Override
    public List<ProductDTO> allFromCategory(String categoryName) {
        if(categoryName==null) {
            throw new NullPointerException("category name must not be null");
        }
        List<Product> products = this.productRepository.allByCategory(categoryName);
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            productDTOS.add(this.modelMapper.map(product,ProductDTO.class));
        }
        return productDTOS;
    }
}
