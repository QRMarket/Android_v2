package com.qr_market.activity;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.qr_market.Guppy;
import com.qr_market.R;
import com.qr_market.fragment.adapter.GuppyFragmentPageAdapter;
import com.qr_market.fragment.ui.BarcodeFragment;
import com.qr_market.fragment.ui.CartFragment;
import com.qr_market.fragment.ui.ProfileFragment;
import com.qr_market.db.DBHandler;
import com.qr_market.http.HttpHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kemal Sami KARACA
 * @since 02.2015
 * @version v1.01
 *
 * @last 09.03.2015
 */
public class MarketActivity extends ActionBarActivity implements CartFragment.OnFragmentInteractionListener,
                                                                    ProfileFragment.OnFragmentInteractionListener,
                                                                    BarcodeFragment.OnFragmentInteractionListener{

    private static Context context = null;


    public static Context getContext() {
        return context;
    }

    public static void setContext(Context marketContext) {
        context = marketContext;
    }

    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              OVERRIDE METHODS
     ***********************************************************************************************
     ***********************************************************************************************
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_market);

        setContext(getApplicationContext());

        ViewPager viewPager = (ViewPager)findViewById(R.id.activity_market);
        viewPager.setAdapter(new GuppyFragmentPageAdapter(getSupportFragmentManager()));


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.market, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            case R.id.db_delete:
                getApplicationContext().deleteDatabase(DBHandler.DATABASE_NAME);
                break;
            case R.id.confirmCart:

                confirmCart();
                break;
            default:
                break;
        }

		return super.onOptionsItemSelected(item);
	}


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public void confirmCart(){

        Map parameters = new HashMap();
        parameters.put("cdosDo", "confirmOrderList");

        Map operationInfo = new HashMap();
        operationInfo.put(Guppy.http_Map_OP_TYPE, HttpHandler.HTTP_OP_NORMAL);
        operationInfo.put(Guppy.http_Map_OP_URL, Guppy.url_Servlet_Order);

        new HttpHandler( getApplicationContext() , "ORDERCONFIRM").execute( operationInfo , parameters);

    }

}
