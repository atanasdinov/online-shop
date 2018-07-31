package project.service.specification;

import project.model.entities.Sale;

import java.util.List;

public interface SaleService {

    void add(String username, List<String> productNames, List<String> prices, List<String> productsId, List<String> productsQuantity);

    List<Sale> getAllSales();
}
