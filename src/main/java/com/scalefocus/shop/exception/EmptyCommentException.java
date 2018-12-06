package com.scalefocus.shop.exception;

public class EmptyCommentException extends RuntimeException {

    private String productId;

    public EmptyCommentException(String message, String productId) {
        super(message);
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
