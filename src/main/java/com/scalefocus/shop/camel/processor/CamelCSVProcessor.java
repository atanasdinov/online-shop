package com.scalefocus.shop.camel.processor;

import com.scalefocus.shop.camel.ProductFromCSV;
import com.scalefocus.shop.service.ProductService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * <b>Custom Camel {@link Processor} used to process a CSV file with products information.</b>
 */
//@Component
public class CamelCSVProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(CamelCSVProcessor.class);

    @Autowired
    private ProductService productService;

    /**
     * @param productService an autowired {@link ProductService} instance.
     */
    public CamelCSVProcessor(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Custom process() method for handling rows from a CSV file and
     * sending them to {@link ProductService} for further insertion in the database.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        List<ProductFromCSV> productList = (List<ProductFromCSV>) exchange.getIn().getBody();

        try {
            productService.batchProductsAdd(productList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }

    }

}