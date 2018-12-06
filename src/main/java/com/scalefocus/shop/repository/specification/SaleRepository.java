package com.scalefocus.shop.repository.specification;

import com.scalefocus.shop.model.entity.Product;
import com.scalefocus.shop.model.entity.Sale;
import com.scalefocus.shop.model.entity.User;

import java.util.List;


/**
 * <b>This interface declares manipulations over the {@link Sale} data in the database.</b>
 */
public interface SaleRepository {

    /**
     * Add a {@link Sale}.
     *
     * @param username of the {@link User}.
     * @param product  {@link Product} object containing data for the sale
     */
    void addSale(String username, Product product);

    /**
     * @return all sales that had been made.
     */
    List<Sale> getAllSales();
}
