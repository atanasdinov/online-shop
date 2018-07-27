package project.model.DTOS;

public class ProductDTO {

    private long id;
    private String categoryName;
    private String name;
    private Double price;
    private Integer quantity;

    public ProductDTO() {
    }

    public ProductDTO(Double price, String name, Integer quantity) {
        this.price = price;
        this.name = name;
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
