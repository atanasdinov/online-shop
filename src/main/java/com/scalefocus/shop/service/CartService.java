package com.scalefocus.shop.service;

import com.scalefocus.shop.exception.EmptyCartException;
import com.scalefocus.shop.exception.ProductAlreadyInCartException;
import com.scalefocus.shop.model.entity.Cart;
import com.scalefocus.shop.model.entity.User;
import com.scalefocus.shop.repository.specification.CartRepository;
import com.scalefocus.shop.repository.specification.ProductRepository;
import com.scalefocus.shop.repository.specification.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>This service declares all available manipulations that can be done for a {@link Cart}</b>
 */
@Service
@Transactional
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /**
     * This method is used to get {@link User}'s {@link Cart}
     * @param username the username of the user
     *
     * @return {@link User}'s {@link Cart}
     */
    public Cart getCart(String username) {
        long cartId = userRepository.getUser(username).getCart().getId();
        return cartRepository.getCart(cartId);
    }

    /**
     * This method is used to add a product in the cart.
     *
     * @param username   the username of the user
     * @param productId  the id of the added product
     * @param pageNumber current page number
     */
    public void addProduct(String username, long productId, String pageNumber) {
        long cartId = userRepository.getUser(username).getCart().getId();

        if (!cartRepository.productExists(cartId, productId)) {
            cartRepository.addProduct(cartId, productRepository.getProduct(productId));
            logger.info("Product added to cart.");
        } else
            throw new ProductAlreadyInCartException("Product is already in cart!", productId, pageNumber);
    }

    /**
     * This method is used to remove a product from the cart.
     *
     * @param username the username of the user.
     * @param productId the id of the product to be removed.
     */
    public void removeProduct(String username, long productId) {
        long cartId = userRepository.getUser(username).getCart().getId();

        cartRepository.removeProduct(cartId, productRepository.getProduct(productId));
        logger.info("Product removed from cart.");
    }

    /**
     * This method is used to remove all products in a given cart.
     *
     * @param cartId The id of the cart.
     */
    public void removeAll(long cartId) {
        if (cartRepository.getCart(cartId).getProducts().isEmpty())
            throw new EmptyCartException("The cart is already empty!");
        else {
            cartRepository.removeAllProducts(cartId);
            logger.info("All products removed from cart.");
        }
    }
}