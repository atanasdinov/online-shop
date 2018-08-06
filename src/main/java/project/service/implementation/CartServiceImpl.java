package project.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.exception.ProductAlreadyInCartException;
import project.model.DTOS.ProductDTO;
import project.model.entities.Product;
import project.repository.specification.CartRepository;
import project.repository.specification.ProductRepository;
import project.service.specification.CartService;
import project.utility.ModelParser;

import java.util.ArrayList;
import java.util.List;


@Service
public class CartServiceImpl implements CartService {

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
    public void addProduct(long cartId, long productId) throws ProductAlreadyInCartException{
        Product product = productRepository.get(productId);
        if (!cartRepository.doesProductExist(cartId, product))
            cartRepository.addProductToCart(cartId, product);
        else
            throw new ProductAlreadyInCartException("Product is already in cart!");
    }

    @Override
    public void removeProduct(long cartId, long productId) {
        cartRepository.removeProductFromCart(cartId, productRepository.get(productId));
    }

    @Override
    public void removeAll(long cartId){
        cartRepository.removeAllProductsFromCart(cartId);
    }

    @Override
    public List<ProductDTO> getProducts(long cartId) {
        List<Product> products = cartRepository.getAllProductsFromCart(cartId);
        List<ProductDTO> productDTOS = new ArrayList<>();

        for (Product product : products)
            productDTOS.add(modelParser.convert(product, ProductDTO.class));

        return productDTOS;
    }

    @Override
    public double getTotalPrice(long cartId) {
        return cartRepository.getTotalPrice(cartId);
    }
}