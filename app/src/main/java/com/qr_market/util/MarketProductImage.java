package com.qr_market.util;

/**
 * @author Kemal Sami KARACA
 * @since 13.04.2015
 * @version v1.01
 *
 * @last 13.04.2015
 */
public class MarketProductImage {

    private String imageID;
    private String imageSource;
    private String imageSourceType;     // base64, url, ...
    private String imageContentType;    // image/png
    private String imageType;           // tumbnail,normal, ...

    public MarketProductImage(){

    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getImageSourceType() {
        return imageSourceType;
    }

    public void setImageSourceType(String imageSourceType) {
        this.imageSourceType = imageSourceType;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
