package com.scalefocus.shop.service;

import com.scalefocus.common.ProductDTO;
import com.scalefocus.common.ProductMessageDTO;
import com.scalefocus.shop.exception.CheckoutEmptyCartException;
import com.scalefocus.shop.exception.InvalidQuantityException;
import com.scalefocus.shop.exception.QuantityNotAvailableException;
import com.scalefocus.shop.model.entity.Cart;
import com.scalefocus.shop.model.entity.Product;
import com.scalefocus.shop.model.entity.Sale;
import com.scalefocus.shop.repository.specification.CartRepository;
import com.scalefocus.shop.repository.specification.ProductRepository;
import com.scalefocus.shop.repository.specification.SaleRepository;
import com.scalefocus.shop.repository.specification.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Queue;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>This service declares all Sales-related manipulations and actions.</b>
 */
@Service
@Transactional
public class SaleService {

    private static final Logger logger = LoggerFactory.getLogger(SaleService.class);

    private SaleRepository saleRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private UserRepository userRepository;

    private JmsTemplate jmsTemplate;
    private Queue queue;

    @Autowired
    public SaleService(SaleRepository saleRepository, ProductRepository productRepository, CartRepository cartRepository,
                       UserRepository userRepository, JmsTemplate jmsTemplate, Queue queue) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.jmsTemplate = jmsTemplate;
        this.queue = queue;
    }

    /**
     * This method is used to make an order.
     *
     * @param username      username of the user
     * @param cart          the user's cart
     * @param quantityArray array with requested quantities for every product
     */
    public void addSale(String username, Cart cart, String[] quantityArray) {
        List<Product> products = cart.getProducts();

        if (products.isEmpty())
            throw new CheckoutEmptyCartException("Empty cart!");

        int[] validQuantities = validateQuantities(quantityArray, products);

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int requestedQuantity = validQuantities[i];
            products.get(i).setRequestedQuantity(requestedQuantity);
            saleRepository.addSale(username, product);
            logger.info("Sale submitted.");
            productRepository.decreaseQuantity(product.getId(), requestedQuantity);
            logger.info("Product's quantity decreased.");
        }

        cartRepository.removeAllProducts(userRepository.getUser(username).getCart().getId());

        sendProductMessage(username, products);
    }

    /**
     * This method is used to get all sales from users.
     *
     * @return all sales
     */
    public List<Sale> getAllSales() {
        return saleRepository.getAllSales();
    }

    /**
     * This method is used to ensure that the input data for quantities is valid.
     *
     * @param quantities        array with requested quantities for each product
     * @param requestedProducts list of the requested products
     * @return array of int numbers
     */
    private int[] validateQuantities(String[] quantities, List<Product> requestedProducts) {
        int[] productQuantities = new int[quantities.length];
        int requestedQuantity;
        int i = 0;

        for (String quantity : quantities) {
            try {
                productQuantities[i] = Integer.parseInt(quantity);

                requestedQuantity = productQuantities[i];
                if (requestedQuantity <= 0)
                    throw new InvalidQuantityException("Quantity must be valid!");

                Product product = requestedProducts.get(i);
                Integer availableQuantity = productRepository.getProduct(product.getId()).getAvailableQuantity();
                if (requestedQuantity > availableQuantity)
                    throw new QuantityNotAvailableException("Quantity not available!");

                i++;
            } catch (IllegalArgumentException e) {
                throw new InvalidQuantityException("Quantity must be valid!");
            }
        }

        return productQuantities;
    }

    /**
     * This method converts objects and maps them as ProductMessageDTO which is after that sent with ActiveMQ.
     *
     * @param username the user who made the order
     * @param products list of the purchased products
     */
    private void sendProductMessage(String username, List<Product> products) {
        List<ProductDTO> productDTOList = new ArrayList<>();

        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setCategoryName(product.getCategory().toString());
            productDTO.setName(product.getName());
            productDTO.setPrice(product.getPrice());
            productDTO.setQuantity(product.getRequestedQuantity());
            productDTOList.add(productDTO);
        }

        ProductMessageDTO productMessageDTO = new ProductMessageDTO(username, productDTOList);

        jmsTemplate.convertAndSend(queue, productMessageDTO);
        logger.info("Product message sent!");
    }
}
