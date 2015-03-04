package com.qr_market.util;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qr_market.activity.MainActivity;
import com.qr_market.db.DBHandler;
import com.qr_market.db.contract.GuppyContract;


/**
 * @author Kemal Sami KARACA
 * @since 03.2015
 * @version v1.01
 */
public class AutoOperationHandler {



    public static MarketUser getUserFromDB(){

        MarketUser user = null;

        // Check is there db for user
        DBHandler dbHelper = new DBHandler(MainActivity.getMainContext());
        // Get db
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Create column info
        String[] userInfo = {
                GuppyContract.GuppyUser._ID,
                GuppyContract.GuppyUser.COLUMN_NAME_USER_NAME,
                GuppyContract.GuppyUser.COLUMN_NAME_USER_MAIL,
                GuppyContract.GuppyUser.COLUMN_NAME_USER_PASSWORD,
                GuppyContract.GuppyUser.COLUMN_NAME_USER_TOKEN,
                GuppyContract.GuppyUser.COLUMN_NAME_USER_SESSIONID };

        // Get user from db
        Cursor cursor = db.query( true , GuppyContract.GuppyUser.TABLE_NAME, userInfo, null, null, null, null,null,null );
        if (cursor.moveToFirst()) {

            user = MarketUser.getInstance();
            user.setUserName(cursor.getString(cursor.getColumnIndex(GuppyContract.GuppyUser.COLUMN_NAME_USER_NAME)));
            user.setUserMail(cursor.getString(cursor.getColumnIndex(GuppyContract.GuppyUser.COLUMN_NAME_USER_MAIL)));
            user.setUserPass(cursor.getString(cursor.getColumnIndex(GuppyContract.GuppyUser.COLUMN_NAME_USER_PASSWORD)));
            user.setUserToken(cursor.getString(cursor.getColumnIndex(GuppyContract.GuppyUser.COLUMN_NAME_USER_TOKEN)));
            user.setUserToken(cursor.getString(cursor.getColumnIndex(GuppyContract.GuppyUser.COLUMN_NAME_USER_SESSIONID)));
            user.setAuth(false);

        }

        return user;
    }

}
