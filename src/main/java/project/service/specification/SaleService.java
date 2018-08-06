package project.service.specification;

import project.exception.CheckoutEmptyCartException;
import project.exception.QuantityNotAvailableException;
import project.model.entities.Sale;

import java.util.List;

public interface SaleService {

    void add(String username, List<String> productNames, List<String> prices, List<String> productsId, List<String> productsQuantity)
            throws IllegalArgumentException, QuantityNotAvailableException, CheckoutEmptyCartException;

    List<Sale> getAllSales();
}
