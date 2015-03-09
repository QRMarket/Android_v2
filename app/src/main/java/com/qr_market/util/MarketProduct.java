package com.qr_market.util;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Orxan
 * @author Kemal Sami KARACA
 * @since 07.03.2015
 * @version v1.01
 *
 * @last 08.03.2015
 */
public class MarketProduct {

    private String product_uid;
    private String product_name;
    private String product_price;
    private String product_price_type;
    private List<String> product_image_url;
    private List<Bitmap> product_image;
    private String p_detail;
    private int image_id;

    // Static Market Object
    private static MarketProduct pInstance = null;
    public static MarketProduct getProductInstance() {
        if(pInstance == null) {
            pInstance = new MarketProduct();
        }
        return pInstance;
    }

    // Constructor
    public MarketProduct() {
        setProduct_image_url(new ArrayList());
        setProduct_image(new ArrayList());
    }
    public MarketProduct(String p_name,String p_detail, String product_price,int image_id) {
        this.setProduct_name(p_name);
        this.setImage_id(image_id);
        this.setP_detail(p_detail);
        this.setProduct_price(product_price);

    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "product_name='" + getProduct_name() + '\'' +
                "product_uid='" + getProduct_uid() + '\'' +
                "product_price='" + getProduct_price() + '\'' +
                "product_price_type='" + getProduct_price_type() + '\'' +
                "product_imgURL='" + getProduct_image_url() + '\'' +
                "product_imgSize='" + getProduct_image().size() + '\'' +
                '}';
    }

    @Override
    public MarketProduct clone(){
        MarketProduct p = new MarketProduct();
        p.setProduct_uid(pInstance.getProduct_uid());
        p.setProduct_name(pInstance.getProduct_name());
        p.setProduct_price(pInstance.getProduct_price());
        p.setProduct_price_type(pInstance.getProduct_price_type());
        p.setProduct_image_url(pInstance.getProduct_image_url());
        p.setProduct_image(pInstance.getProduct_image());
        return p;
    }

    public String getProduct_uid() {
        return product_uid;
    }

    public void setProduct_uid(String product_uid) {
        this.product_uid = product_uid;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_price_type() {
        return product_price_type;
    }

    public void setProduct_price_type(String product_price_type) {
        this.product_price_type = product_price_type;
    }

    public String getP_detail() {
        return p_detail;
    }

    public void setP_detail(String p_detail) {
        this.p_detail = p_detail;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public List<Bitmap> getProduct_image() {
        return product_image;
    }

    public void setProduct_image(List<Bitmap> product_image) {
        this.product_image = product_image;
    }

    public List<String> getProduct_image_url() {
        return product_image_url;
    }

    public void setProduct_image_url(List<String> product_image_url) {
        this.product_image_url = product_image_url;
    }
}
