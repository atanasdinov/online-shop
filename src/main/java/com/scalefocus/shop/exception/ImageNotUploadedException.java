package com.scalefocus.shop.exception;

import com.scalefocus.shop.model.entity.Product;

/**
 * <b>This exception is used to indicate image is not uploaded in 'create-product' form.</b>
 */
public class ImageNotUploadedException extends RuntimeException {
    private Product product;

    public ImageNotUploadedException(String message, Product product) {
        super(message);
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
