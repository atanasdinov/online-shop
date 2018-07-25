package project.model.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Double shipmentPrice;
    private Double totalPrice;
    @OneToMany(mappedBy = "cart",cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private List<Product> products;

    public Cart() {
        this.products = new ArrayList<>();
    }

    public Cart(List<Product> products, Double shipmentPrice, Double totalPrice) {
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

    public Double getShipmentPrice() {
        return shipmentPrice;
    }

    public void setShipmentPrice(Double shipmentPrice) {
        this.shipmentPrice = shipmentPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}