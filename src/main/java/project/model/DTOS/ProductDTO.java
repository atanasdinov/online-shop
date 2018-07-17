package project.model.DTOS;

public class ProductDTO {
    private Double price;
    private byte[] picture;
    private String name;
    private String rating;
    private Integer quantity;

    public ProductDTO() {
    }

    public ProductDTO(Double price, String name, String rating, Integer quantity) {
        this.price = price;
        this.name = name;
        this.rating = rating;
        this.quantity = quantity;
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
}