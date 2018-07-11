package project.model;

import java.util.List;

public class Cart {

    private List<Product> products;
    private Integer shipmentPrice;
    private Integer totalPrice;

    public Cart() {
    }

    public Cart(List<Product> products, Integer shipmentPrice, Integer totalPrice) {
        this.products = products;
        this.shipmentPrice = shipmentPrice;
        this.totalPrice = totalPrice;
    }
}
