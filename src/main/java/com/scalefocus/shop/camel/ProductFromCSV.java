package com.scalefocus.shop.camel;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;


/**
 * <b>A custom class which is used to wrap a single entry/row read from a CSV file.</b>
 */
@CsvRecord(separator = ",")
public class ProductFromCSV {

    @DataField(pos = 1)
    private String name;

    @DataField(pos = 2)
    private double price;

    @DataField(pos = 3)
    private int quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}

