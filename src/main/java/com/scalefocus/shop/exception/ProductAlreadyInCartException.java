package com.scalefocus.shop.exception;


/**
 * <b>This exception is used to indicate whether product is already in the cart.</b>
 */
public class ProductAlreadyInCartException extends RuntimeException {
    private long productId;
    private String pageNumber;

    public ProductAlreadyInCartException(String message, long productId, String pageNumber) {
        super(message);
        this.productId = productId;
        this.pageNumber = pageNumber;
    }

    public long getProductId() {
        return productId;
    }

    public String getPageNumber() {
        return pageNumber;
    }
}
