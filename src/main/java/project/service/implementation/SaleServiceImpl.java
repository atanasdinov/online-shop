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
        boolean flag = true;

        for (int i = 0; i < productNames.size(); i++) {
            String productName = productNames.get(i);
            Double price = Double.parseDouble(prices.get(i));
            Long productId = Long.parseLong(productsId.get(i));
            try {
                Integer productQuantity = Integer.parseInt(productsQuantity.get(i));
                if (productQuantity <= 0 || productQuantity > productRepository.get(productId).getQuantity()) {
                    flag = false;
                    throw new IllegalArgumentException("Quantity must be valid");
                }
            } catch (IllegalArgumentException iae) {
                flag = false;
                throw new IllegalArgumentException("Quantity must be valid");
            }
        }


        if (flag) {
            for (int i = 0; i < productNames.size(); i++) {
                String productName = productNames.get(i);
                Double price = Double.parseDouble(prices.get(i));
                Long productId = Long.parseLong(productsId.get(i));
                Integer productQuantity = Integer.parseInt(productsQuantity.get(i));

                productRepository.decreaseQuantity(productId, productQuantity);
                if(productRepository.getProductQuantity(productId) == 0)
                    productRepository.delete(productId);
                saleRepository.persist(username, productName, price * productQuantity, productQuantity);
            }

        }
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository.getAllSales();
    }
}
