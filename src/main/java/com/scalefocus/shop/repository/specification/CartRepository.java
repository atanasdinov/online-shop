package com.scalefocus.shop.repository.specification;

import com.scalefocus.shop.model.entity.Cart;
import com.scalefocus.shop.model.entity.Product;

import java.util.List;


/**
 * <b>This interface declares manipulations over the {@link Cart} data in the database.</b>
 */
public interface CartRepository {

    /**
     * Creates a {@link Cart}.
     *
     * @return created {@link Cart}
     */
    Cart createCart();

    /**
     * Add a {@link Product} to a {@link Cart}.
     *
     * @param cartId  the criteria to be found.
     * @param product contain the product to be added.
     */
    void addProduct(long cartId, Product product);

    /**
     * Remove a {@link Product} from a {@link Cart}.
     *
     * @param cartId  the criteria to be found.
     * @param product contain the product to be added.
     */
    void removeProduct(long cartId, Product product);

    /**
     * Remove all {@link Product}s from a {@link Cart}.
     *
     * @param cartId the criteria to be found.
     */
    void removeAllProducts(long cartId);

    /**
     * Get all {@link Product}s from a {@link Cart}.
     *
     * @param cartId the criteria to be found.
     * @return all products from the cart.
     */
    List<Product> getAllProducts(long cartId);

    /**
     * Get specific {@link Cart}.
     *
     * @param cartId the criteria to be found.
     * @return found cart.
     */
    Cart getCart(long cartId);

    /**
     * Check if {@link Product} exist in a {@link Cart}.
     *
     * @param cartId  the criteria to be found
     * @param productId the criteria to be found
     * @return
     */
    boolean productExists(long cartId, long productId);
}