package com.qr_market;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Kemal Sami KARACA
 * @since 05.03.2015
 * @version v1.01
 */
public class Guppy {

    public static String url_scheme             = "http://";
    public static String url_server             = url_scheme + "193.140.63.162";                    //193.140.63.162
    public static String url_serverPort         = url_server + ":8080";
    public static String url                    = url_serverPort + "/QR_Market_Web";
    public static String url_Servlet_Auth       = url + "/Auth";
    public static String url_Servlet_Order      = url + "/OrderServlet";
    public static String url_Servlet_Product    = url + "/ProductServlet";

    public static String url_Servlet_IMAGE      = url + "/images";
    public static String url_Servlet_Sample     = url_serverPort + "/Sample_WebApplication_3_Upload/SampleServlet";

    // OPERATION KEYS
    public static String http_Map_OP_TYPE   = "opType";
    public static String http_Map_OP_URL    = "opUrl";



    public static boolean checkInternetConnection(Context context){
        ConnectivityManager con=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=con.getActiveNetworkInfo();
        if(con.getActiveNetworkInfo()==null || !nf.isConnected()){
            return false;
        }
        return true;
    }



}

