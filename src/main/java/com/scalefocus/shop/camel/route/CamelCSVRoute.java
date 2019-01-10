package com.scalefocus.shop.camel.route;

import com.scalefocus.shop.camel.ProductFromCSV;
import com.scalefocus.shop.camel.processor.CamelCSVProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * <b>Custom Camel route used to process a CSV file.</b>
 */
//@Component
public class CamelCSVRoute extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(CamelCSVRoute.class);

    @Autowired
    private CamelCSVProcessor camelCSVProcessor;

    /**
     * {@inheritDoc}
     * <p>
     * <p><i>Configures a CSV processing route which executes in parallel {@link CamelCSVProcessor}.</i></p>
     */
    @Override
    public void configure() throws Exception {
        BindyCsvDataFormat bindy = new BindyCsvDataFormat(ProductFromCSV.class);

        onCompletion().log("Insertion of records from .csv file completed.");

        from("file://src/main/resources?fileName=XXDATA.csv&noop=true")
                .log(LoggingLevel.INFO, logger, "CSV Processing has started...")
                .split(body().tokenize("\n", 200, false))
                .streaming()
                .parallelProcessing(true)
                .unmarshal(bindy)
                .process(camelCSVProcessor);
    }
}
