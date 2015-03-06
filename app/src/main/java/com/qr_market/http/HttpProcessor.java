package com.qr_market.http;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.qr_market.activity.LoginActivity;
import com.qr_market.activity.MarketActivity;
import com.qr_market.db.DBHandler;
import com.qr_market.db.contract.GuppyContract;
import com.qr_market.util.MarketUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Kemal Sami KARACA
 * @since 06.03.2015
 * @version v1.01
 */
public class HttpProcessor {

    private String requestResult    = null;
    private Context context         = null;
    private Map map_param;




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

    /**
     * @param resultStr
     *
     *  This method is used to try to pass next Activity which is marketActivity
     */
    public void operationLogin(String resultStr){

        boolean operationResultSuccess = false;

        try {
            JSONObject result = new JSONObject(resultStr);

            Iterator iterator = result.keys();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                Log.i("JSON KEY", key);
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


}
