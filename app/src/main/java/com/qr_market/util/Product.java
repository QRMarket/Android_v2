package com.qr_market.util;

/**
 * Created by orxan on 11.05.2015.
 */
/**
 * Created by orxan on 10.05.2015.
 */
public class Product {
    private String m_name;
    private String product;
    private String price;


    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "MarketProduct{" +
                "m_name='" + m_name + '\'' +
                ", product='" + product + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
