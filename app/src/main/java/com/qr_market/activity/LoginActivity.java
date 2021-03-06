package com.qr_market.activity;


import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.qr_market.Guppy;
import com.qr_market.R;
import com.qr_market.db.DBHandler;
import com.qr_market.http.HttpHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kemal Sami KARACA
 * @since 04.03.2015
 * @version v1.01
 */
public class LoginActivity extends ActionBarActivity implements View.OnClickListener {


    private CallbackManager callbackManager;

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

    // FACEBOOK LOGIN TUTORIAL PAGE
    //      - https://developers.facebook.com/docs/facebook-login/android/v2.2
    //      - https://developers.facebook.com/docs/android/getting-started#login_share

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FACEBOOK INITIALIZATION
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

       setContentView(R.layout.activity_login);



        final DBHandler dbHelper = new DBHandler(getApplicationContext());

        setView(this.getWindow().getDecorView().findViewById(android.R.id.content));

        BootstrapButton login = (BootstrapButton) findViewById(R.id.guppyLogin);
        BootstrapButton register = (BootstrapButton) findViewById(R.id.guppyRegister);
        final BootstrapEditText cduName = (BootstrapEditText) findViewById(R.id.TextUsrname);
        final BootstrapEditText cduPass = (BootstrapEditText) findViewById(R.id.TextPsw);



        // FACEBOOK LOGIN
        // -----------------------------------------------------------------------------------------
        // -----------------------------------------------------------------------------------------
        List<String> permissionList = new ArrayList<>();
        permissionList.add("user_friends");
        permissionList.add("email");
        permissionList.add("user_about_me");
        permissionList.add("public_profile");
        permissionList.add("user_photos");

        LoginButton facebookLoginButton = (LoginButton) this.findViewById(R.id.facebook_login_button);

        facebookLoginButton.setReadPermissions(permissionList);
        // **** If using in a fragment ****
        // facebookLoginButton.setFragment(this);


        // Callback registration
        facebookLoginButton.registerCallback( callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(),"Facebook login success " + AccessToken.getCurrentAccessToken().getUserId(),Toast.LENGTH_LONG).show();
                Log.i("AAAAAAAAAAAAAA","AAAAAAAAAAAAAA");

                //Intent intent = new Intent( getApplicationContext() , MarketActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //startActivity(intent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Facebook login cancelled ",Toast.LENGTH_LONG).show();
                Log.i("BBBBBBBBBBBBBBB","BBBBBBBBBBBBBBB");
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(),"Facebook login error occur ",Toast.LENGTH_LONG).show();
                Log.i("CCCCCCCCCCCCCCC","CCCCCCCCCCCCCCC");
            }
        });





        // GUPPY
        // -----------------------------------------------------------------------------------------
        // -----------------------------------------------------------------------------------------
        login.setOnClickListener(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map parameters = new HashMap();
                parameters.put("authDo", "carpeLogin");
                parameters.put("cduMail", cduName.getText().toString());
                parameters.put("cduPass", cduPass.getText().toString());

                Map operationInfo = new HashMap();
                operationInfo.put(Guppy.http_Map_OP_TYPE, HttpHandler.HTTP_OP_LOGIN);
                operationInfo.put(Guppy.http_Map_OP_URL, Guppy.url_Servlet_Auth);

                new HttpHandler(getApplicationContext()).execute( operationInfo , parameters );
            }
        });

        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext() , "Register button does not work for now" , Toast.LENGTH_LONG);
            }
        });




        // TEST ACTIONS
        // -----------------------------------------------------------------------------------------
        // -----------------------------------------------------------------------------------------
        /*
        Button twitter = (Button) findViewById(R.id.twitter);
        Button facebook = (Button) findViewById(R.id.fb);
        TextView registration = (TextView) findViewById(R.id.registration);
        TextView forget = (TextView) findViewById(R.id.forget);

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


                // Check is there db for user
                DBHandler dbHelper = new DBHandler(MainActivity.getMainContext());

                MainActivity.getMainContext().deleteDatabase(dbHelper.DATABASE_NAME);

                Toast.makeText(getApplicationContext() , "DB Deleted" , Toast.LENGTH_SHORT).show();

            }
        });

        */
        // -----------------------------------------------------------------------------------------
        // -----------------------------------------------------------------------------------------
        // TEST ACTIONS

    }


    @Override
    protected void onResume() {
        super.onResume();

        // FACEBOOK analytics
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // FACEBOOK analytics
        AppEventsLogger.deactivateApp(this);
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


    @Override
    public void onClick(View v) {

    }

    /**
     * FACEBOOK CALLBACK HANDLER
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
