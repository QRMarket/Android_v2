package com.qr_market.util;

/**
 * @author Kemal Sami KARACA
 * @since 13.04.2015
 * @version v1.01
 */
public class MarketUserAddress {

    private String aid;
    private String city;
    private String borough;
    private String locality;
    private String street;
    private String avenue;
    private String desc;

    public MarketUserAddress(){

    }

    public String getAddressString(){
        return city + " - " + borough + " - " + locality + street + ".Cadde ,"+avenue+".Sokak";
    }

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
