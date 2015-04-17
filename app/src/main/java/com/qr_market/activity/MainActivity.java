package com.qr_market.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.AlarmClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qr_market.Guppy;
import com.qr_market.R;
import com.qr_market.checker.Checker;
import com.qr_market.util.AutoOperationHandler;
import com.qr_market.util.MarketUser;
import com.qr_market.db.DBHandler;
import com.qr_market.db.contract.GuppyContract;
import com.qr_market.http.HttpHandler;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Kemal Sami KARACA
 * @since 02.2015
 * @version v1.01
 */
public class MainActivity extends ActionBarActivity{

    private static View view;
    private static Context mainContext = null;

    public static Context getMainContext() {
        return mainContext;
    }

    public static void setMainContext(Context mainContext) {
        MainActivity.mainContext = mainContext;
    }

    public static View getView() {
        return view;
    }

    public static void setView(View view) {
        MainActivity.view = view;
    }


    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                                  OVERRIDE METHODS
     ***********************************************************************************************
     ***********************************************************************************************
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mainContext = getApplicationContext();

        // -- DB OPERATIONS -- WILL BE DELETED
        // -----------------------------------
            //final DBHandler dbHelper = new DBHandler(getApplicationContext());
            //MainActivity.getMainContext().deleteDatabase(dbHelper.DATABASE_NAME);
        // -----------------------------------
        // -- DB OPERATIONS -- WILL BE DELETED

        AutoOperationHandler.getUserFromDB();
        if( !Checker.anyNull( MarketUser.getInstance().getUserMail() , MarketUser.getInstance().getUserPass() ) ){

            Map parameters = new HashMap();
            parameters.put("authDo", "carpeLogin");
            parameters.put("cduMail", MarketUser.getInstance().getUserMail());
            parameters.put("cduPass", MarketUser.getInstance().getUserPass());

            Map operationInfo = new HashMap();
            operationInfo.put(Guppy.http_Map_OP_TYPE, HttpHandler.HTTP_OP_LOGIN);
            operationInfo.put(Guppy.http_Map_OP_URL, Guppy.url_Servlet_Auth);

            new HttpHandler(getApplicationContext()).execute( operationInfo , parameters );


        }else{

            Intent intent = new Intent( this , LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);

    }


}
