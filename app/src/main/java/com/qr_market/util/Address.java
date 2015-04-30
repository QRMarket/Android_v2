package com.qr_market.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kemal Sami KARACA
 * @since 30.04.2015
 * @version v1.02
 *
 * @last 30.04.2015
 *
 */
public class Address {

    private static List<Address> addressList = null;
    private static Address addressInstance = null;

    private String aid;
    private String city;
    private String borough;
    private String locality;
    private String street;
    private String avenue;
    private String desc;

    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              CONSTRUCTORs
     ***********************************************************************************************
     ***********************************************************************************************
     */
    // Static Order List
    public static List<Address> getAddressListInstance() {
        if(addressList == null) {
            addressList = new ArrayList();
        }
        return addressList;
    }

    public static void setAddressListInstance(List<Address> addressList) {
        addressList = addressList;
    }

    // Static Address Object
    public static Address getAddressInstance() {
        if(addressInstance == null) {
            addressInstance = new Address();
        }
        return addressInstance;
    }

    // Constructor
    public Address() {
    }

    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              ENCAPSULATION
     ***********************************************************************************************
     ***********************************************************************************************
     */

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBorough() {
        return borough;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAvenue() {
        return avenue;
    }

    public void setAvenue(String avenue) {
        this.avenue = avenue;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }




}
