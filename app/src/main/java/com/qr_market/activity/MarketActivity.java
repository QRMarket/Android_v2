package com.qr_market.activity;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.qr_market.R;
import com.qr_market.fragment.adapter.GuppyFragmentPageAdapter;
import com.qr_market.fragment.ui.CartFragment;
import com.qr_market.fragment.ui.ProfileFragment;
import com.qr_market.db.DBHandler;

/**
 * @author Kemal Sami KARACA
 * @since 02.2015
 * @version v1.01
 */
public class MarketActivity extends ActionBarActivity implements CartFragment.OnFragmentInteractionListener,
                                                                    ProfileFragment.OnFragmentInteractionListener{

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
            default:
                break;
        }

		return super.onOptionsItemSelected(item);
	}


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
