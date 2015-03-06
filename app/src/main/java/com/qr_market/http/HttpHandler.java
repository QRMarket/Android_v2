package com.qr_market.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.qr_market.Guppy;
import com.qr_market.R;
import com.qr_market.activity.LoginActivity;
import com.qr_market.activity.MainActivity;
import com.qr_market.activity.MarketActivity;
import com.qr_market.db.DBHandler;
import com.qr_market.db.contract.GuppyContract;
import com.qr_market.fragment.ui.CartFragment;
import com.qr_market.util.MarketUser;


/**
 * @author Kemal Sami KARACA
 * @since 03.2015
 * @version v1.01
 */
public class HttpHandler extends AsyncTask< Map , Integer, String> {

    public static final String HTTP_OP_LOGIN        = "HTTP_OP_LOGIN";
    public static final String HTTP_OP_MULTIPART    = "HTTP_OP_MULTIPART";
    public static final String HTTP_OP_NORMAL       = "HTTP_OP_NORMAL";
    private Map map_param           = null; // Parameters for http request
    private Map map_opr             = null; // Request meta properties such as url, header type ...


    private String servletName      = null;
    private Context context         = null;
    private String OperationStatus         = null;      // Like Result Object



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
                map_param = params[0];      // its need for db operations
                map_opr = params[1];        // for nothing :)

                return postData(params[0], params[1]);

            }catch (ArrayIndexOutOfBoundsException e){
                OperationStatus = "PARAM_ERROR";
                this.cancel(true);
            }

        return null;
	}

    @Override
    protected void onCancelled() {

            Log.i("ONCANCELLED", OperationStatus);
            if(OperationStatus.equalsIgnoreCase("PARAM_ERROR")){
                Toast.makeText(context , "HTTPHANDLER Parameter error " , Toast.LENGTH_LONG);
            }else {
                Toast.makeText(context , "HTTPHANDLER Unknown error " , Toast.LENGTH_LONG);
            }

    }
	
	@Override
    protected void onPostExecute(String resultStr) {

            if(resultStr!=null){
                if(servletName!=null && servletName.equalsIgnoreCase("PRODUCT")){
                    productGetInfo(resultStr);
                }else if(servletName!=null && servletName.equalsIgnoreCase("ORDER")) {

                }else{
                    tryToGoMarketActivity(resultStr);
                }
            }else{
                Toast.makeText(context , "HTTP REQUEST FAILURE" , Toast.LENGTH_LONG);
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

                Iterator iter = m.keySet().iterator();
                while(iter.hasNext()){
                    String key = (String) iter.next();
                    nameValuePairs.add(new BasicNameValuePair(key, (String) m.get(key)));

                    Log.i(key , (String) m.get(key));
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
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
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
                 * @param param
                 * @return
                 *
                 *  This function will used for requests && responses. Function only returns what server side
                 *  return.
                 */
                public String postData(Map param , Map opr) {

                        // ***********************
                        // *** INITIALIZATION ****
                        // ***********************
                        String resultStr=null;
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost( (String)opr.get(Guppy.http_Map_OP_URL) );

                        // ***********************
                        // *** SET HEADER ********
                        // ***********************
                        httppost = setHttpHeader(httppost , (String)opr.get(Guppy.http_Map_OP_TYPE));

                        // ***********************
                        // *** POST OPERATION ****
                        // ***********************
                        resultStr = executeData(httpClient, httppost);


                        if(resultStr!=null){
                            Log.i("RESULT" , resultStr);
                        }else{
                            Log.i("RESULT" , "NULL");
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
             *                                  RESULT OPERATIONs
             ***********************************************************************************************
             */


            /**
             * @param resultStr
             *
             *  This method is used to try to pass next Activity which is marketActivity
             */
            public void tryToGoMarketActivity(String resultStr){

                boolean operationResultSuccess = false;

                try {
                    result = new JSONObject(resultStr);

                    Iterator iterator = result.keys();
                    while(iterator.hasNext()){
                        String key = (String)iterator.next();
                        Log.i("JSON KEY" , key);
                        Log.i("JSON KEY" , result.getString(key));
                    }


                    String resCode = (String) result.get("resultCode");
                    if(resCode.equalsIgnoreCase("GUPPY.001")){

                            JSONObject resContent = result.getJSONObject("content");

                            MarketUser.getInstance().setUserMail((String)map_param.get("cduMail"));
                            MarketUser.getInstance().setUserPass((String)map_param.get("cduPass"));
                            MarketUser.getInstance().setUserToken(resContent.getString("userToken"));
                            MarketUser.getInstance().setUserSession(resContent.getString("userSession"));
                            MarketUser.getInstance().setUserName(resContent.getString("userName"));

                            // Get dbHelper
                            DBHandler dbHelper = new DBHandler(context);
                            // Check if there is exist user data then update it otherwise add new user to db
                            SQLiteDatabase db = dbHelper.getReadableDatabase();
                            // Set selection
                            String[] selection = {
                                    GuppyContract.GuppyUser._ID,
                                    GuppyContract.GuppyUser.COLUMN_NAME_USER_NAME,
                                    GuppyContract.GuppyUser.COLUMN_NAME_USER_TOKEN,
                                    GuppyContract.GuppyUser.COLUMN_NAME_USER_SESSIONID,
                                    GuppyContract.GuppyUser.COLUMN_NAME_USER_MAIL,
                                    GuppyContract.GuppyUser.COLUMN_NAME_USER_PASSWORD
                            };

                            Cursor c = db.query( true , GuppyContract.GuppyUser.TABLE_NAME, selection, null, null, null, null,null,null );

                            // Prepare for db operation
                            db = dbHelper.getWritableDatabase();

                            // Create a new map of values, where column names are the keys
                            // ...USER_MAIL && ...USER_PASS are taken from parameter which is used for http request
                            ContentValues values = new ContentValues();
                            values.put(GuppyContract.GuppyUser.COLUMN_NAME_USER_TOKEN , resContent.getString("userToken"));
                            values.put(GuppyContract.GuppyUser.COLUMN_NAME_USER_SESSIONID , resContent.getString("userSession"));
                            values.put(GuppyContract.GuppyUser.COLUMN_NAME_USER_NAME , resContent.getString("userName"));
                            values.put(GuppyContract.GuppyUser.COLUMN_NAME_USER_MAIL , (String)map_param.get("cduMail"));
                            values.put(GuppyContract.GuppyUser.COLUMN_NAME_USER_PASSWORD , (String)map_param.get("cduPass"));

                            if (c.moveToFirst()){
                                // if user exist then update it
                                String whereClause = GuppyContract.GuppyUser._ID + "=" + 1;
                                long userId = db.update(GuppyContract.GuppyUser.TABLE_NAME , values , whereClause , null);
                                Log.i("USER-ID update" , "" + userId);

                            }else {
                                // add new user to db
                                // Edit db
                                long userId = db.insert( GuppyContract.GuppyUser.TABLE_NAME,  null , values);
                                Log.i("USER-ID insert" , "" + userId);
                            }

                            operationResultSuccess = true;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // After operations finish then start intent
                if(operationResultSuccess){
                    Intent intent = new Intent( context , MarketActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent( context , LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            }




            /**
             * @param resultStr
             *
             *  This method is used to try to pass next Activity which is marketActivity
             */
            public void productGetInfo(String resultStr){

                try {
                    result = new JSONObject(resultStr);
                    JSONObject resultContent = null;

                    Iterator iterator = result.keys();
                    while(iterator.hasNext()){
                        String key = (String)iterator.next();
                        Log.i("JSON KEY" , key);
                        Log.i("JSON KEY" , result.getString(key));

                        if(key.equalsIgnoreCase("content")){
                            resultContent = new JSONObject(result.getString(key));
                        }
                    }


                    // This area get result from JSON and put it in EditText value ...
                    View cartFragmentView = CartFragment.getViewCartFragment();
                    EditText productText = (EditText)cartFragmentView.findViewById(R.id.product_id);
                    productText.setText(resultContent.getString("productID"));

                    // This area used for toast message ...
                    String productInfo = "Product:" + resultContent.getString("productName") + " - Price:" + resultContent.getString("price");
                    Toast.makeText( context , productInfo, Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            public void addToOrderList(String resultStr){
                try {
                    result = new JSONObject(resultStr);
                    JSONObject resultContent = null;

                    Iterator iterator = result.keys();
                    while(iterator.hasNext()){
                        String key = (String)iterator.next();
                        Log.i("JSON KEY" , key);
                        Log.i("JSON KEY" , result.getString(key));

                        if(key.equalsIgnoreCase("content")){
                            resultContent = new JSONObject(result.getString(key));
                        }
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


}
