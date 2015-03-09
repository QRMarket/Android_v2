package com.qr_market.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kemal Sami KARACA
 * @since 04.03.2015
 * @version v1.01
 */
public class MarketUser {



    private static boolean isAuth;
    private static String userName;
    private static String userMail;
    private static String userToken;
    private static String userSession;
    private static String userPass;
    private static List<MarketProduct> productList;

    static{
        //initializeStaticObjects();
    }


    private static MarketUser instance = getInstance();
    protected MarketUser() {
        isAuth = false;
        userName = null;
        userMail = null;
        userToken = null;
        userSession = null;
        productList = new ArrayList<>();
    }

    public static MarketUser getInstance() {
        if(instance == null) {
            instance = new MarketUser();
        }
        return instance;
    }


    public static List<MarketProduct> getProductList() {
        return productList;
    }

    public static void setProductList(List<MarketProduct> productList) {
        MarketUser.productList = productList;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserSession() {
        return userSession;
    }

    public void setUserSession(String userSession) {
        this.userSession = userSession;
    }


}
