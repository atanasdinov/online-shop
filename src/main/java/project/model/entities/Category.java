package project.model.entities;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "category",cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private List<Product> products;

    public Category() {
        this.products = new ArrayList<>();
    }

    public Category(String name) {
        this.products = new ArrayList<>();
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public Category(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }
}
