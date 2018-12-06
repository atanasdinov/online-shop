package com.scalefocus.shop.repository.specification;

import com.scalefocus.shop.camel.ProductFromCSV;
import com.scalefocus.shop.model.entity.Category;
import com.scalefocus.shop.model.entity.Comment;
import com.scalefocus.shop.model.entity.Product;

import java.util.List;


/**
 * <b>This interface declares manipulations over the {@link Product} data in the database.</b>
 */
public interface ProductRepository {

    /**
     * Create a {@link Product}.
     *
     * @param product contain the product data.
     */
    void createProduct(Product product);


    /** Batch insert of {@link Product}s from .csv file.
     *
     * @param products list of products
     * @param defaultCategoryId id of the default category
     */
    void batchProductsInsert(List<ProductFromCSV> products, long defaultCategoryId);

    /**
     * Edit an existing {@link Product}.
     *
     * @param productData the product to be edited.
     * @param categoryId the criteria to be found.
     */
    void editProduct(Product productData, long categoryId);

    /**
     * Delete a {@link Product}.
     *
     * @param id the criteria to be found.
     */
    void deleteProduct(long id);

    /**
     * Get existing {@link Product}.
     *
     * @param id the criteria to be found.
     * @return found product.
     */
    Product getProduct(long id);

    /**
     * Get all {@link Product}s from {@link Category}.
     *
     * @param categoryId id of the category
     * @return found products
     */
    List<Product> getAllProductsByCategory(Long categoryId);

    /**
     * Decrease the quantity of certain {@link Product}.
     *
     * @param id       the criteria to be found.
     * @param quantity to be decreased.
     */
    void decreaseQuantity(long id, int quantity);

    /**
     * Find all {@link Product}s that match a string.
     *
     * @param keyword the criteria to be found.
     * @return found products.
     */
    List<Product> productsMatching(String keyword);

    /**
     * Appending a {@link Comment} to {@link Product}.
     *
     * @param productId the criteria to be found.
     * @param commentId the criteria to be found.
     */
    void appendComment(long productId, long commentId);

    /**
     * Remove a {@link Comment} from {@link Product}.
     *
     * @param productId the criteria to be found.
     * @param commentId the criteria to be found.
     */
    void removeComment(long productId, long commentId);

    /**
     * Get products by page.
     *
     * @param pageNumber number of the page.
     * @param pageSize   the amount of products per page.
     * @return list of products
     */
    List<Product> getProductsByPage(int pageNumber, int pageSize);

    /**
     * This method checks if a picture is bound to one or more products.
     *
     * @param pictureName name of the picture
     * @return true - if picture exists, false - if picture doesn't exist
     */
    boolean pictureIsBoundToMoreThanOneProduct(String pictureName);
}
