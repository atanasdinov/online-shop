package project.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.exception.CheckoutEmptyCartException;
import project.exception.QuantityNotAvailableException;
import project.model.entities.Sale;
import project.repository.specification.ProductRepository;
import project.repository.specification.SaleRepository;
import project.service.specification.SaleService;

import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private SaleRepository saleRepository;
    private ProductRepository productRepository;

    @Autowired
    public SaleServiceImpl(SaleRepository saleRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void add(String username, List<String> productNames, List<String> prices, List<String> productsId, List<String> productsQuantity)
            throws IllegalArgumentException, QuantityNotAvailableException, CheckoutEmptyCartException {
        boolean flag = true;

        if(productNames.isEmpty())
            throw new CheckoutEmptyCartException("Empty cart!");

        for (int i = 0; i < productNames.size(); i++) {
            String productName = productNames.get(i);
            Double price = Double.parseDouble(prices.get(i));
            Long productId = Long.parseLong(productsId.get(i));
            try {
                Integer productQuantity = Integer.parseInt(productsQuantity.get(i));
                if (productQuantity <= 0) {
                    flag = false;
                    throw new IllegalArgumentException("Quantity must be valid!");
                }
                if (productQuantity > productRepository.get(productId).getQuantity()) {
                    flag = false;
                    throw new QuantityNotAvailableException("Quantity not available!");
                }
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException("Quantity must be valid!");
            }
        }

        if (flag) {
            for (int i = 0; i < productNames.size(); i++) {
                String productName = productNames.get(i);
                Double price = Double.parseDouble(prices.get(i));
                Long productId = Long.parseLong(productsId.get(i));
                Integer productQuantity = Integer.parseInt(productsQuantity.get(i));

                productRepository.decreaseQuantity(productId, productQuantity);
                saleRepository.persist(username, productName, price * productQuantity, productQuantity);
            }
        }
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository.getAllSales();
    }
}
