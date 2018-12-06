package com.scalefocus.shop.model.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * <b>This entity is intended to give an additional functionality for {@link User}.
 * They can make statement of certain {@link Product}.</b>
 */
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.PERSIST)
    private User user;

    @ManyToMany(targetEntity = Product.class)
    private Set<Product> products;

    public Comment() {
        this.products = new HashSet<>();
    }

    public Comment(String message) {
        this.message = message;
        this.products = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProduct(Product product) {
        this.products.add(product);
    }

    public void setId(long id) {
        this.id = id;
    }
}
