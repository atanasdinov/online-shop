package project.Service;

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
    public void addProduct(long cartId, long productId) throws InvalidProductException, InvalidCartException {
        if (productId < 1)
            throw new InvalidProductException("Product must not be null!");

        if (!cartRepository.doesExist(cartId))
            throw new InvalidCartException("Cart does not exists!");

        Product product = productRepository.get(productId);
        if (!cartRepository.doesProductExist(cartId, product))
            cartRepository.addProduct(cartId, product);
        else
            System.out.println(5);


    }

//    // TODO: When adding given product several times - increment its quantity inside the given cart and decrement availability inside the database.
//    @Override
//    public void addProduct(long cardId, ProductDTO productDTO) throws InvalidProductException, InvalidCartException {
//        if (productDTO == null)
//            throw new InvalidProductException("ProductDTO is null!");
//
//        if (!cartRepository.doesExist(cardId))
//            throw new InvalidCartException("Cart does not exist!");
//
//        cartRepository.addProduct(cardId, productRepository.get(productDTO.getId()));
//    }

    @Override
    public void removeProduct(long cartId, long productId) throws InvalidProductException, InvalidCartException {
        if (productId < 1)
            throw new InvalidProductException("Product name must not be null");

        if (!cartRepository.doesExist(cartId))
            throw new InvalidCartException("Cart must exist.");

        cartRepository.removeProduct(cartId, productRepository.get(productId));
    }

    @Override
    public void removeAll(long cartId) throws InvalidCartException {
        // TODO: Maybe some cartId control logic here...

        cartRepository.removeAllProducts(cartId);
    }

    @Override
    public List<ProductDTO> getProducts(long cartId) {
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

    @Override
    public double getTotalPrice(long cartId) {
        return cartRepository.getTotalPrice(cartId);
    }
}