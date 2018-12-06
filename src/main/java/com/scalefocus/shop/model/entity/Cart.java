package com.scalefocus.shop.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <b>This entity is intended to store all {@link Product}s and some additional info for them.</b>
 */
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(targetEntity = Product.class)
    private List<Product> products;

    @Column(name = "total_price")
    private Double totalPrice;

    @OneToOne(mappedBy = "cart", fetch = FetchType.EAGER)
    private User user;

    public Cart() {
        this.products = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    public Cart(List<Product> products, Double totalPrice) {
        this.products = products;
        this.totalPrice = totalPrice;
    }

    public Cart(List<Product> products) {
        this.products = products;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}