package project.model.entities;

import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Double price;
    @Nullable
    private byte[] picture;
    @ManyToOne(targetEntity = Category.class,cascade = CascadeType.PERSIST)
    @JoinColumn(name="category_id")
    private Category category;
    private String name;
    private String rating;
    private Integer quantity;
    @ManyToOne(targetEntity = Cart.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name="cart_id")
    private Cart cart;

    public Product() {
    }

    public Product(Double price, String name) {
        this.price = price;
        this.name = name;
    }

    public Product(Double price, Category category, String name, Integer quantity) {
        this.price = price;
        this.category = category;
        this.name = name;
        this.quantity = quantity;
    }
    public Product(Category category,Double price, String name, Integer quantity) {
        this.category=category;
        this.category.addProduct(this);
        this.price = price;
        this.name = name;
        this.quantity = quantity;
    }


    public int getId() {
        return id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
