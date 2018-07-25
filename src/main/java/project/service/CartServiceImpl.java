package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.exception.InvalidCartException;
import project.exception.InvalidProductException;
import project.model.DTOS.ProductDTO;
import project.model.entities.Product;
import project.repositories.CartRepository;
import project.repositories.ProductRepository;
import project.utils.ModelParser;

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
    public void addProduct(int cartId, String productName) throws InvalidProductException, InvalidCartException {
        if (productName == null)
            throw new InvalidProductException("Product must not be null!");

        if (!cartRepository.doesExist(cartId))
            throw new InvalidCartException("Cart does not exists!");

        cartRepository.addProduct(cartId, productRepository.get(productName));
    }

    // TODO: When adding given product several times - increment its quantity inside the given cart and decrement availability inside the database.
    @Override
    public void addProduct(int cardId, ProductDTO productDTO) throws InvalidProductException, InvalidCartException {
        if (productDTO == null)
            throw new InvalidProductException("ProductDTO is null!");

        if (!cartRepository.doesExist(cardId))
            throw new InvalidCartException("Cart does not exist!");

        cartRepository.addProduct(cardId, productRepository.get(productDTO.getName()));
    }

    @Override
    public void removeProduct(int cartId, String productName) throws InvalidProductException, InvalidCartException {
        if (productName == null)
            throw new InvalidProductException("Product name must not be null");

        if (!cartRepository.doesExist(cartId))
            throw new InvalidCartException("Cart must exist.");

        cartRepository.removeProduct(cartId, productRepository.get(productName));
    }

    @Override
    public void removeAll(int cartId) throws InvalidCartException {
        // TODO: Maybe some cartId control logic here...

        cartRepository.removeAllProducts(cartId);
    }

    @Override
    public List<ProductDTO> getProducts(int cartId) {
        if (!cartRepository.doesExist(cartId)) {
            throw new NullPointerException("Cart must exist.");
        }
        List<Product> products = this.cartRepository.getProducts(cartId);
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            productDTOS.add(this.modelParser.convert(product, ProductDTO.class));
        }
        return productDTOS;
    }
}
