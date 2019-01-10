package com.scalefocus.shop.service;

import com.scalefocus.shop.exception.CategoryNotFoundException;
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
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Queue;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * @param quantities array with requested quantities for every product
     */
    public void addSale(String username, Cart cart, String[] quantities) {

        Optional.ofNullable(username)
                .orElseThrow(() -> new UsernameNotFoundException("Empty username!"));

        List<Product> requestedProducts = Optional.ofNullable(cart.getProducts())
                .orElseThrow(() -> new CheckoutEmptyCartException("Empty cart!"));

        Optional.of(requestedProducts)
                .map(products -> validateQuantities(quantities, products))
                .ifPresent(products -> products.forEach(product -> {
                    saleRepository.addSale(username, product);
                    logger.debug("Sale submitted.");
                    productRepository.decreaseQuantity(product.getId(), product.getRequestedQuantity());
                    logger.debug("Product's quantity decreased.");
                }));

        cartRepository.removeAllProducts(userRepository.getUser(username).getCart().getId());

        //sendProductMessage(username, products);
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
    private List<Product> validateQuantities(String[] quantities, List<Product> requestedProducts) {
        return requestedProducts.stream()
                .peek(product -> {
                    Integer requestedQuantity = Optional.of(quantities[requestedProducts.indexOf(product)])
                            .map(NumberUtils::toInt)
                            .filter(quantity -> quantity > NumberUtils.INTEGER_ZERO)
                            .orElseThrow(() -> new InvalidQuantityException("Quantity must be valid!"));

                    Optional.of(requestedQuantity)
                            .filter(reqQuantity -> {
                                Integer availableQuantity = Optional.ofNullable(productRepository.getProduct(product.getId()))
                                        .map(Product::getAvailableQuantity)
                                        .orElseThrow(() -> new CategoryNotFoundException("asd"));

                                return reqQuantity < availableQuantity;
                            })
                            .orElseThrow(() -> new QuantityNotAvailableException("Quantity not available!"));

                    product.setRequestedQuantity(requestedQuantity);
                })
                .collect(Collectors.toList());

//    /**
//     * This method converts objects and maps them as ProductMessageDTO which is after that sent with ActiveMQ.
//     *
//     * @param username the user who made the order
//     * @param products list of the purchased products
//     */
//    private void sendProductMessage(String username, List<Product> products) {
//        List<ProductDTO> productDTOList = new ArrayList<>();
//
//        for (Product product : products) {
//            ProductDTO productDTO = new ProductDTO();
//            productDTO.setId(product.getId());
//            productDTO.setCategoryName(product.getCategory().toString());
//            productDTO.setName(product.getName());
//            productDTO.setPrice(product.getPrice());
//            productDTO.setQuantity(product.getRequestedQuantity());
//            productDTOList.add(productDTO);
//        }
//
//        ProductMessageDTO productMessageDTO = new ProductMessageDTO(username, productDTOList);
//
//        jmsTemplate.convertAndSend(queue, productMessageDTO);
//        logger.info("Product message sent!");
//    }
    }
}