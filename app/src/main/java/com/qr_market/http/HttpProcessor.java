package com.qr_market.http;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qr_market.Guppy;
import com.qr_market.R;
import com.qr_market.activity.LoginActivity;
import com.qr_market.activity.MarketActivity;
import com.qr_market.async.ImageHandler;
import com.qr_market.db.DBHandler;
import com.qr_market.db.contract.GuppyContract;
import com.qr_market.fragment.adapter.GuppyFragmentListAdapter;
import com.qr_market.fragment.ui.BarcodeFragment;
import com.qr_market.fragment.ui.CartFragment;
import com.qr_market.util.MarketProduct;
import com.qr_market.util.MarketUser;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;




/**
 * @author Kemal Sami KARACA
 * @since 06.03.2015
 * @version v1.01
 *
 * @last 09.03.2015
 *
 * This class takes http request results(responses) and takes care of them.
 *
 */
public class HttpProcessor {

    private String requestResult    = null;
    private Context context         = null;


    public HttpProcessor(String requestResult , Context context){
        this.requestResult = requestResult;
        this.context = context;
    }




    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                                  RESULT OPERATIONs
     ***********************************************************************************************
     ***********************************************************************************************
     */



    // ---------------------------------------------------------------------------------------------
    /**
     * @param userMail
     * @param userPass
     *
     * This function is used to handle "login" RESPONSEs
     */
    public void userLogin(String userMail , String userPass){

        boolean operationResultSuccess = false;

        try {
            JSONObject result = new JSONObject(requestResult);

            Iterator iterator = result.keys();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                Log.i("JSON KEY" , key);
                Log.i("JSON KEY" , result.getString(key));
            }


            String resCode = (String) result.get("resultCode");
            if(resCode.equalsIgnoreCase("GUPPY.001")){

                JSONObject resContent = result.getJSONObject("content");

                MarketUser.getInstance().setUserMail(userMail);
                MarketUser.getInstance().setUserPass(userPass);
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
                values.put(GuppyContract.GuppyUser.COLUMN_NAME_USER_MAIL , userMail);
                values.put(GuppyContract.GuppyUser.COLUMN_NAME_USER_PASSWORD , userPass);

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





    // ---------------------------------------------------------------------------------------------
    /**
     * @return
     *
     * This function is used to handle "order" RESPONSEs
     *
     */
    public boolean orderAddCart(){
        boolean operationResultSuccess = false;

        try {
            JSONObject result = new JSONObject(requestResult);

            String resCode = (String) result.get("resultCode");
            if(resCode.equalsIgnoreCase("GUPPY.001")){

                // ADD PRODUCT TO USER
                MarketProduct lastProduct = MarketProduct.getProductInstance().clone();
                MarketUser.getInstance().getProductList().add(lastProduct);

                // GET VIEW LIST
                ListView listViewBasket   = (ListView) CartFragment.getViewCartFragment().findViewById(R.id.BasketList);
                listViewBasket.setAdapter(new GuppyFragmentListAdapter( context , MarketUser.getInstance().getProductList() ));

                // SET PRODUCT INFO VISIBLE
                BarcodeFragment.getViewBarcodeFragment().findViewById(R.id.barcode_product).setVisibility(View.INVISIBLE);

                operationResultSuccess = true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return operationResultSuccess;
    }





    // ---------------------------------------------------------------------------------------------
    /**
     * @return
     *
     * This function is used to handle "confirmOrder" RESPONSEs
     *
     */
    public boolean confirmCart(){
        boolean operationResultSuccess = false;

        try {
            JSONObject result = new JSONObject(requestResult);

            String resCode = (String) result.get("resultCode");
            if(resCode.equalsIgnoreCase("GUPPY.001")){

                MarketUser.getInstance().setProductList(new ArrayList<MarketProduct>());
                ListView listViewBasket   = (ListView) CartFragment.getViewCartFragment().findViewById(R.id.BasketList);
                listViewBasket.setAdapter(new GuppyFragmentListAdapter( context , MarketUser.getInstance().getProductList() ));

                System.out.println(requestResult);
                Toast.makeText(context , "CART CONFIRM SUCCESS" , Toast.LENGTH_LONG).show();

                operationResultSuccess = true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return operationResultSuccess;
    }





    // ---------------------------------------------------------------------------------------------
    /**
     * @return
     *
     * This function is used to handle "product" RESPONSEs
     *
     */
    public void productGetInfo(){

        try {
            JSONObject result = new JSONObject(requestResult);
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
            View barcodeFragmentView = BarcodeFragment.getViewBarcodeFragment();

            // URL to ImageView AREA
            ImageView imgElem = (ImageView)barcodeFragmentView.findViewById(R.id.product_image);


            // ---------------------------------------
            // UPDATE STATIC MARKET PRODUCT OBJECT
            MarketProduct newProduct = MarketProduct.getProductInstance();
            newProduct.setProduct_name(resultContent.getString("productName"));
            newProduct.setProduct_uid(resultContent.getString("productID"));
            newProduct.setProduct_price_type(resultContent.getString("priceType"));
            newProduct.setProduct_price(resultContent.getString("price"));
            newProduct.setProduct_image(new ArrayList<Bitmap>());
            newProduct.setProduct_image_url(new ArrayList<String>());
            for(int i=0; i<resultContent.getJSONArray("images").length(); i++) {
                newProduct.getProduct_image_url().add(Guppy.url_Servlet_IMAGE + "/" + (String) resultContent.getJSONArray("images").get(i));
            }
            new ImageHandler(imgElem).execute(newProduct);
            // ---------------------------------------


            TextView productText = (TextView)barcodeFragmentView.findViewById(R.id.product_name);
            productText.setText(newProduct.getProduct_name());

            TextView productPrice = (TextView)barcodeFragmentView.findViewById(R.id.product_price);
            productPrice.setText(newProduct.getProduct_price());



            // SET PRODUCT INFO VISIBLE
            barcodeFragmentView.findViewById(R.id.barcode_product).setVisibility(View.VISIBLE);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
