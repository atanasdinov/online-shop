package project.repository.specification;

import project.model.entities.Sale;

import java.util.List;


public interface SaleRepository {

    void persist(String username, String productName, Double price, Integer productQuantity);

    List<Sale> getAllSales();
}
