package project.model.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(mappedBy = "cart")
    private Set<Product> products;
    private Double shipmentPrice;
    private Double totalPrice;

    @OneToOne(mappedBy = "cart", fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
    private User user;

    public Cart() {
        this.products = new HashSet<>();
        this.shipmentPrice=0.0;
        this.totalPrice=0.0;
    }

    public Cart(Set<Product> products, Double shipmentPrice, Double totalPrice) {
        this.products = products;
        this.shipmentPrice = shipmentPrice;
        this.totalPrice = totalPrice;
    }

    public long getId() {
        return id;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}