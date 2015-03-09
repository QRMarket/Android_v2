package com.qr_market.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qr_market.Guppy;
import com.qr_market.R;
import com.qr_market.activity.LoginActivity;
import com.qr_market.activity.MainActivity;
import com.qr_market.activity.MarketActivity;
import com.qr_market.async.ImageHandler;
import com.qr_market.checker.Checker;
import com.qr_market.db.DBHandler;
import com.qr_market.db.contract.GuppyContract;
import com.qr_market.fragment.ui.BarcodeFragment;
import com.qr_market.fragment.ui.CartFragment;
import com.qr_market.util.MarketProduct;
import com.qr_market.util.MarketUser;


/**
 * @author Kemal Sami KARACA
 * @since 03.2015
 * @version v1.01
 */
public class HttpHandler extends AsyncTask< Map , Integer, String > {

    public static final String HTTP_OP_LOGIN        = "HTTP_OP_LOGIN";
    public static final String HTTP_OP_MULTIPART    = "HTTP_OP_MULTIPART";
    public static final String HTTP_OP_NORMAL       = "HTTP_OP_NORMAL";
    private Map map_param           = null; // Parameters for http request
    private Map map_opr             = null; // Request meta properties such as url, header type ...


    private String servletName      = null;
    private Context context         = null;
    private String OperationStatus  = null;      // Like Result Object



    private Intent intent;
    private View view;
    private JSONObject result;
    private MainActivity mainActivity;


    @Deprecated
    public HttpHandler(){
        context = MainActivity.getMainContext();
    }



    public HttpHandler(Context context){
        this.context = context;
    }
    public HttpHandler(Context context , String servletName ){
        this.context = context;
        this.servletName = servletName;
    }
    public HttpHandler(Context context , String servletName , String url ){
        this.context = context;
        this.servletName = servletName;
    }


    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              ASYNCTASK OVERRIDE METHODS
     ***********************************************************************************************
     ***********************************************************************************************
     */
	@Override
	protected String doInBackground(Map... params) {

            try{
                map_param = params[1];      // its need for db operations
                map_opr = params[0];        // its need for post settings

                return postData();

            }catch (ArrayIndexOutOfBoundsException e){

                OperationStatus = "MAP_PARAM_MISSING_ERROR";
                this.cancel(true);
            }

        return null;
	}

    @Override
    protected void onCancelled() {

            Toast.makeText( context , OperationStatus , Toast.LENGTH_LONG).show();

    }
	
	@Override
    protected void onPostExecute(String resultStr) {


            if(resultStr!=null){

                if(servletName!=null && servletName.equalsIgnoreCase("PRODUCT")){

                        new HttpProcessor(resultStr , context).productGetInfo();

                }else if(servletName!=null && servletName.equalsIgnoreCase("ORDER")) {

                        new HttpProcessor(resultStr , context).orderAddCart();

                }else if(servletName!=null && servletName.equalsIgnoreCase("ORDERCONFIRM")) {

                        new HttpProcessor(resultStr , context).confirmCart();

                }else{
                        String userMail = (String)map_param.get("cduMail");
                        String userPass = (String)map_param.get("cduPass");
                        new HttpProcessor(resultStr , context).userLogin(userMail , userPass);
                }
            }else{

                Toast.makeText(context , "NULL_RESPONSE" , Toast.LENGTH_LONG).show();
            }

    }




    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                                  ENCAPSULATION
     ***********************************************************************************************
     ***********************************************************************************************
     */




    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                                  UTIL FUNCTIONs
     ***********************************************************************************************
     ***********************************************************************************************
     */

        /**
         * @param
         * @return
         *
         *  This function convert Map<key,value> parameters to List<NameValuePair>
         */
        public List getParameterPair(Map m){

            List<NameValuePair> nameValuePairs = new ArrayList();
            try{
                Iterator iter = m.keySet().iterator();
                while(iter.hasNext()){
                    String key = (String) iter.next();
                    nameValuePairs.add(new BasicNameValuePair(key, (String) m.get(key)));

                    Log.i(key , (String) m.get(key));
                }
            }catch (NullPointerException e){
                alert("MAP_PARAMETER_ERROR");
            }
            return nameValuePairs;
        }


        private HttpPost setHttpHeader(HttpPost httppost , String opType ){

                try {
                    if(opType.equalsIgnoreCase(HTTP_OP_LOGIN)){
                        httppost.addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                        httppost.addHeader("accept-encoding", "gzip, deflate, sdch");
                        httppost.addHeader("accept-language" , "en-US,en;q=0.8,tr;q=0.6");
                        httppost.setEntity(new UrlEncodedFormEntity( getParameterPair(map_param) ));

                    }else if(opType.equalsIgnoreCase(HTTP_OP_MULTIPART)){
                        String cookie1 = "JSESSIONID="+ MarketUser.getInstance().getUserSession();
                        String cookie2 = "cduToken="+ MarketUser.getInstance().getUserToken();
                        httppost.addHeader("accept" , "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                        httppost.addHeader("accept-encoding", "gzip, deflate, sdch");
                        httppost.addHeader("accept-language" , "en-US,en;q=0.8,tr;q=0.6");
                        httppost.addHeader("cookie" , cookie1+"; "+ cookie2);
                        httppost.addHeader("content-type" , "multipart/form-data");
                        // MULTI-PART SETTINGs

                        // http://stackoverflow.com/questions/1378920/how-can-i-make-a-multipart-form-data-post-request-using-java
                        // 62 rank sahibi ikinci alanda yaniti veriyor

                    }else if(opType.equalsIgnoreCase(HTTP_OP_NORMAL)){
                        String cookie1 = "JSESSIONID="+ MarketUser.getInstance().getUserSession();
                        String cookie2 = "cduToken="+ MarketUser.getInstance().getUserToken();
                        httppost.addHeader("accept" , "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                        httppost.addHeader("accept-encoding", "gzip, deflate, sdch");
                        httppost.addHeader("accept-language" , "en-US,en;q=0.8,tr;q=0.6");
                        httppost.addHeader("cookie" , cookie1+"; "+ cookie2);
                        httppost.setEntity(new UrlEncodedFormEntity( getParameterPair(map_param) ));
                    }else {
                        alert("UNMATCH_HEADER_TYPE");
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    alert("UNEXPECTED_HEADER_SETTING_ERROR");
                }

            return httppost;
        }

        private String executeData(HttpClient httpclient , HttpPost httppost){
                String resultStr=null;
                try {
                        // Execute HTTP Post Request
                        HttpResponse response = httpclient.execute(httppost);
                        StatusLine statusLine = response.getStatusLine();

                        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            response.getEntity().writeTo(out);

                            resultStr = out.toString();

                        } else{
                            //Closes the connection.
                            response.getEntity().getContent().close();
                            throw new IOException(statusLine.getReasonPhrase());
                        }

                } catch (ClientProtocolException e) {
                    resultStr = null;
                } catch (IOException e) {
                    resultStr = null;
                }

            return resultStr;
        }

        private String executeData(CloseableHttpClient httpclient , HttpPost httppost){
            String resultStr=null;
            try {
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();

                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);

                    resultStr = out.toString();

                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }

            } catch (ClientProtocolException e) {
                resultStr = null;
            } catch (IOException e) {
                resultStr = null;
            }

            return resultStr;
        }

            /**
             ***********************************************************************************************
             *                                  POST FUNCTIONs
             ***********************************************************************************************
             */

                /**
                 * @return
                 *
                 *  This function will used for requests && responses. Function only returns what server side
                 *  return.
                 */
                public String postData() {

                        String resultStr=null;
                        if(is_map_opr_OK()){

                            // ***********************
                            // *** INITIALIZATION ****
                            // ***********************
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost( (String)map_opr.get(Guppy.http_Map_OP_URL) );

                            // ***********************
                            // *** SET HEADER ********
                            // ***********************
                            httppost = setHttpHeader(httppost , (String)map_opr.get(Guppy.http_Map_OP_TYPE));

                            // ***********************
                            // *** POST OPERATION ****
                            // ***********************
                            resultStr = executeData(httpClient, httppost);

                        }else{
                            OperationStatus = "MAP_OPR_NOT_PROPER";
                            this.cancel(true);
                            return null;
                        }

                        return resultStr;
                }




                /**
                 * @param param
                 * @return
                 *
                 *  This function will used for requests && responses. Function only returns what server side
                 *  return. (UPDATED Version )
                 */
                public String postData_4_4_2(Map param , Map opr) {

                        // ***********************
                        // *** INITIALIZATION ****
                        // ***********************
                        String resultStr=null;
                        CloseableHttpClient httpclient = HttpClients.createDefault();

                        HttpPost httppost = new HttpPost( (String)opr.get(Guppy.http_Map_OP_URL) );

                        // ***********************
                        // *** SET HEADER ********
                        // ***********************
                        httppost = setHttpHeader(httppost , (String)opr.get(Guppy.http_Map_OP_TYPE));

                        // ***********************
                        // *** POST OPERATION ****
                        // ***********************
                        resultStr = executeData(httpclient, httppost);


                        if(resultStr!=null){
                            Log.i("RESULT" , resultStr);
                        }else{
                            Log.i("RESULT" , "NULL");
                        }

                        return resultStr;
                }



    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                                  CHECKER FUNCTIONs
     ***********************************************************************************************
     ***********************************************************************************************
     */
    public boolean is_map_opr_OK(){

            boolean isOK=false;
            try{
                isOK = map_opr.get(Guppy.http_Map_OP_URL)!=null &&
                        map_opr.get(Guppy.http_Map_OP_TYPE)!=null;

            } catch (NullPointerException e){

            }

        return isOK;
    }



    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                                  ALERT FUNCTIONs
     ***********************************************************************************************
     ***********************************************************************************************
     */
    public void alert(String errText){
        OperationStatus = "MAP_PARAM_MISSING_ERROR";
        OperationStatus = errText;
        this.cancel(true);
    }


}
