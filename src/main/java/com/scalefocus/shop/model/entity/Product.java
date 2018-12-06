package com.scalefocus.shop.model.entity;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static com.scalefocus.shop.model.constant.ValidationConstants.EMPTY_FIELD_MESSAGE;
import static com.scalefocus.shop.model.constant.ValidationConstants.PRICE_PATTERN_MISMATCH_ERROR;


/**
 * <b>This entity is intended to be bought by the {@link User}.
 * This should be stored in a {@link Cart} in order to purchase.</b>
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = EMPTY_FIELD_MESSAGE)
    @DecimalMax(value = "1500.0", message = PRICE_PATTERN_MISMATCH_ERROR)
    @DecimalMin(value = "0.1", message = PRICE_PATTERN_MISMATCH_ERROR)
    private Double price;

    @Column(name = "picture_name")
    private String pictureName;

    @ManyToOne(targetEntity = Category.class)
    @JoinColumn(name = "category_id")
    private Category category;

    @Transient
    private String image;

    @NotEmpty(message = EMPTY_FIELD_MESSAGE)
    private String name;

    @NotNull(message = EMPTY_FIELD_MESSAGE)
    @Column(name = "available_quantity")
    private Integer availableQuantity;

    @Column(name = "requested_quantity")
    private Integer requestedQuantity;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Comment> comments;

    @Transient
    private MultipartFile file;

    public Product() {
        this.comments = new ArrayList<>();
    }

    public Product(Category category, Double price, String name, Integer availableQuantity, String pictureName) {
        this.category = category;
        this.category.addProduct(this);
        this.price = price;
        this.name = name;
        this.availableQuantity = availableQuantity;
        this.pictureName = pictureName;
    }

    public Product(String name, Double price, Integer availableQuantity) {
        this.name = name;
        this.price = price;
        this.availableQuantity = availableQuantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
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

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComment(Comment comment) {
        this.comments.add(comment);
    }

    public Integer getRequestedQuantity() {
        return requestedQuantity;
    }

    public void setRequestedQuantity(Integer requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
