package com.scalefocus.shop.exception;

import com.scalefocus.shop.enumeration.ProductAction;
import com.scalefocus.shop.model.entity.Product;
import org.springframework.validation.BindingResult;


/**
 * <b>This exception is used to indicate errors in "create/edit product" form's data.</b>
 */
public class InvalidProductDataException extends RuntimeException {
    private Product product;
    private BindingResult bindingResult;
    private ProductAction productAction;

    public InvalidProductDataException(String message, Product product, BindingResult bindingResult, ProductAction productAction) {
        super(message);
        this.product = product;
        this.bindingResult = bindingResult;
        this.productAction = productAction;
    }

    public Product getProduct() {
        return product;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public ProductAction getProductAction() {
        return productAction;
    }
}