package project.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public void add(String username, List<String> productNames, List<String> prices, List<String> productsId, List<String> productsQuantity) {
        for (int i = 0; i < productNames.size(); i++) {
            String productName = productNames.get(i);
            Double price = Double.parseDouble(prices.get(i));
            Long productId = Long.parseLong(productsId.get(i));
            try {
                Integer productQuantity = Integer.parseInt(productsQuantity.get(i));
                if(productQuantity >= 0 && productQuantity <= productRepository.get(productId).getQuantity()) {
                    productRepository.decreaseQuantity(productId, productQuantity);
                    saleRepository.persist(username, productName, price*productQuantity, productQuantity);
                }
                else{
                    throw new IllegalArgumentException("Invalid quantity");
                }
            } catch (ClassCastException e) {
                throw new ClassCastException("Quantity must be positive number");
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException("quantity must be positive");
            }

        }
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository.getAllSales();
    }
}
