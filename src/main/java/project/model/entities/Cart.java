package project.model.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToMany(mappedBy = "cart")
    private List<Product> products;
    private Integer shipmentPrice;
    private Integer totalPrice;

    public Cart() {
        this.products = new ArrayList<>();
    }

    public Cart(List<Product> products, Integer shipmentPrice, Integer totalPrice) {
        this.products = products;
        this.shipmentPrice = shipmentPrice;
        this.totalPrice = totalPrice;
    }

    public long getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Integer getShipmentPrice() {
        return shipmentPrice;
    }

    public void setShipmentPrice(Integer shipmentPrice) {
        this.shipmentPrice = shipmentPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }
}