package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.DTOS.ProductDTO;
import project.model.entities.Product;
import project.repositories.CartRepository;
import project.repositories.ProductRepository;
import project.utils.ModelParser;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService{

    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private ModelParser modelParser;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, ModelParser modelParser) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.modelParser = modelParser;
    }

    @Override
    public void create() {
        cartRepository.persist();
    }

    @Override
    public void addProduct(int cartId, String productName) {
        if(productName==null) {
            throw new NullPointerException("Product name must not be null");
        }
        if(!cartRepository.doesExist(cartId)) {
            throw new NullPointerException("Cart must exist.");
        }
        this.cartRepository.addProduct(cartId,productRepository.get(productName));

    }

    @Override
    public void removeProduct(int cartId, String productName) {
        if(productName==null) {
            throw new NullPointerException("Product name must not be null");
        }
        if(!cartRepository.doesExist(cartId)) {
            throw new NullPointerException("Cart must exist.");
        }
        this.cartRepository.removeProduct(cartId,productRepository.get(productName));
    }

    @Override
    public List<ProductDTO> getProducts(int cartId) {
        if(!cartRepository.doesExist(cartId)) {
            throw new NullPointerException("Cart must exist.");
        }
        List<Product> products = this.cartRepository.getProducts(cartId);
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            productDTOS.add(this.modelParser.convert(product,ProductDTO.class));
        }
        return productDTOS;
    }
}
