package com.qr_market.activity;

import android.content.ContentValues;
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

import com.qr_market.R;
import com.qr_market.db.DBHandler;
import com.qr_market.db.contract.GuppyContract;
import com.qr_market.http.HttpHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kemal Sami KARACA
 * @since 04.03.2015
 * @version v1.01
 */
public class LoginActivity extends ActionBarActivity {


    private static View view;
    public static View getView() {
        return view;
    }

    public static void setView(View view) {
        LoginActivity.view = view;
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
        setContentView(R.layout.activity_login);

        final DBHandler dbHelper = new DBHandler(getApplicationContext());

        Button twitter = (Button) findViewById(R.id.twitter);
        Button facebook = (Button) findViewById(R.id.fb);
        TextView registration = (TextView) findViewById(R.id.registration);
        TextView forget = (TextView) findViewById(R.id.forget);

        setView(this.getWindow().getDecorView().findViewById(android.R.id.content));

        Button button = (Button) findViewById(R.id.Login);
        final EditText cduName = (EditText) findViewById(R.id.LoginUserName);
        final EditText cduPass = (EditText) findViewById(R.id.LoginPassword);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map parameters = new HashMap();
                parameters.put("authDo", "carpeLogin");
                parameters.put("cduMail", cduName.getText().toString());
                parameters.put("cduPass", cduPass.getText().toString());

                new HttpHandler().execute(parameters);

            }
        });



        // TEST ACTIONS
        // -----------------------------------------------------------------------------------------
        // -----------------------------------------------------------------------------------------
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ** INFO ** shortcut to pass second Activity
                goToMarketActivity(true);
            }
        });


        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ** INFO ** Sample -1-
                //createAlarm("Deneme" , 6 , 2);

                // Gets the data repository in write mode
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(GuppyContract.GuppyUser.COLUMN_NAME_USER_TOKEN , "token-1234-1452");
                values.put(GuppyContract.GuppyUser.COLUMN_NAME_USER_NAME , "kemal sami");
                values.put(GuppyContract.GuppyUser.COLUMN_NAME_USER_MAIL , "kemal");
                values.put(GuppyContract.GuppyUser.COLUMN_NAME_USER_PASSWORD , "kemal");

                long newRowId = db.insert( GuppyContract.GuppyUser.TABLE_NAME,  null , values);
                Log.i("DB-OPERATION", "" + newRowId);

                Toast.makeText(getApplicationContext(), "Add new user called", Toast.LENGTH_SHORT).show();

            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String[] projection = {
                        GuppyContract.GuppyUser._ID,
                        GuppyContract.GuppyUser.COLUMN_NAME_USER_NAME,
                        GuppyContract.GuppyUser.COLUMN_NAME_USER_MAIL,
                        GuppyContract.GuppyUser.COLUMN_NAME_USER_TOKEN
                };

                Cursor c = db.query( true , GuppyContract.GuppyUser.TABLE_NAME, projection, null, null, null, null,null,null );
                if (c.moveToFirst()) {
                    do {
                        String userName = c.getString(c.getColumnIndex(GuppyContract.GuppyUser.COLUMN_NAME_USER_NAME));
                        Log.i("CURSOR" , userName);
                    } while (c.moveToNext());
                }

                Toast.makeText(getApplicationContext() , "Get user operation called" , Toast.LENGTH_SHORT).show();
            }

        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        /*
                        String name = Environment.getExternalStorageDirectory().getName();
                        Log.i("Environmet" , name);


                        File folder = new File(Environment.getExternalStorageDirectory() + "/Guppy");
                        boolean success = true;
                        if (!folder.exists()) {
                            success = folder.mkdir();
                        }

                        if (success) {
                            Log.i("Operation" , "OK");
                        } else {
                            // Do something else on failure
                            Log.i("Operation" , "FAIL");
                        }
                        */

                // Check is there db for user
                DBHandler dbHelper = new DBHandler(MainActivity.getMainContext());

                MainActivity.getMainContext().deleteDatabase(dbHelper.DATABASE_NAME);

                Toast.makeText(getApplicationContext() , "DB Deleted" , Toast.LENGTH_SHORT).show();

            }
        });
        // -----------------------------------------------------------------------------------------
        // -----------------------------------------------------------------------------------------
        // TEST ACTIONS

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                                  UTIL FUNCTIONs
     ***********************************************************************************************
     ***********************************************************************************************
     */
    public void goToMarketActivity(boolean isAuthSuccess){
        if(isAuthSuccess){
            Intent intent = new Intent( this, MarketActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);


            // Problem-1-
            /**
             * İkinci Activity sonrasında back tuşuna basılması durumunda Activity1 e dönmez.Ancak
             * activity uykumoduna geçip tekrak uyandırıldığında Activity1 açılır.
             */

        }
    }


    public void createAlarm(String message, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
