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
    private String orderId;


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
        this.orderId = orderId;
    }



    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              ENCAPSULATION
     ***********************************************************************************************
     ***********************************************************************************************
     */
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
