package com.qr_market;

/**
 * Created by kemal on 05/03/15.
 */
public class Guppy {

    public static String url_scheme             = "http://";
    public static String url_server             = url_scheme + "192.168.2.50";
    public static String url_serverPort         = url_server + ":8080";
    public static String url                    = url_serverPort + "/QR_Market_Web";
    public static String url_Servlet_Auth       = url + "/Auth";
    public static String url_Servlet_Order      = url + "/OrderServlet";


    public static String url_Servlet_Sample     = url_serverPort + "/Sample_WebApplication_3_Upload/SampleServlet";

    // OPERATION KEYS
    public static String http_Map_OP_TYPE   = "opType";
    public static String http_Map_OP_URL    = "opUrl";


}
