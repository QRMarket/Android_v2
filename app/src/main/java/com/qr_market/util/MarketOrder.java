package com.qr_market.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kemal Sami KARACA
 * @since 26.04.2015
 * @version v1.01
 *
 * @description
 *      xxx
 *
 * @last 26.04.2015
 */
public class MarketOrder {

    private static List<MarketOrder> orderList = null;
    private static MarketOrder oInstance = null;

    private String orderID;
    private String paymentType;
    private String note;
    private String date;
    private String companyName;
    private Address address;
    private List<MarketProduct> orderProductList = null;


    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              CONSTRUCTORs
     ***********************************************************************************************
     ***********************************************************************************************
     */
    // Static Order List
    public static List<MarketOrder> getOrderListInstance() {
        if(orderList == null) {
            orderList = new ArrayList();
        }
        return orderList;
    }

    public static void setOrderListInstance(List<MarketOrder> marketOrders) {
        orderList = marketOrders;
    }

    // Static Market Object
    public static MarketOrder getOrderInstance() {
        if(oInstance == null) {
            oInstance = new MarketOrder();
        }
        return oInstance;
    }

    // Constructor
    public MarketOrder() {
    }
    public MarketOrder(String orderId) {
        this.orderID = orderId;
    }



    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              ENCAPSULATION
     ***********************************************************************************************
     ***********************************************************************************************
     */
    public String getOrderId() {
        return orderID;
    }
    public void setOrderId(String orderId) {
        this.orderID = orderId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<MarketProduct> getOrderProductList() {
        if(orderProductList == null) {
            orderProductList = new ArrayList();
        }
        return orderProductList;
    }

    public void setOrderProductList(List<MarketProduct> orderProductList) {
        this.orderProductList = orderProductList;
    }
}
