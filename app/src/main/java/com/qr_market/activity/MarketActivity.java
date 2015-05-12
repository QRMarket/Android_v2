package com.qr_market.activity;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qr_market.Guppy;
import com.qr_market.R;
import com.qr_market.adapter.NavigationAdapter;
import com.qr_market.adapter.NavigationList;
import com.qr_market.db.DBHandler;
import com.qr_market.fragment.ui.BarcodeFragment;
import com.qr_market.fragment.ui.BasketFragment;
import com.qr_market.fragment.ui.CartFragment;
import com.qr_market.fragment.ui.ExpandableFragment;
import com.qr_market.fragment.ui.OrderFragment;
import com.qr_market.fragment.ui.PaymentFragment;
import com.qr_market.fragment.ui.PaymentResultFragment;
import com.qr_market.fragment.ui.ProfileAddressFragment;
import com.qr_market.fragment.ui.ProfileFragment;
import com.qr_market.fragment.ui.ViewPagerFragmentList;
import com.qr_market.http.HttpHandler;
import com.qr_market.util.MarketUser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kemal Sami KARACA
 * @since 02.2015
 * @version v1.01
 *
 * @last 15.04.2015
 */
public class MarketActivity extends ActionBarActivity implements ViewPagerFragmentList.OnFragmentInteractionListener,
                                                                    CartFragment.OnFragmentInteractionListener,
                                                                    ProfileFragment.OnFragmentInteractionListener,
                                                                    ProfileAddressFragment.OnFragmentInteractionListener,
                                                                    OrderFragment.OnFragmentInteractionListener,
                                                                    BarcodeFragment.OnFragmentInteractionListener,
                                                                    BasketFragment.OnFragmentInteractionListener,
                                                                    PaymentFragment.OnFragmentInteractionListener,
                                                                    PaymentResultFragment.OnFragmentInteractionListener{

    private static Context context = null;
    public static Context getContext() {
        return context;
    }
    public static void setContext(Context marketContext) {
        context = marketContext;
    }

    private int counterItemDownloads;
    private int lastPosition = 0;

    private ListView listDrawer;
    private DrawerLayout layoutDrawer;
    private LinearLayout linearDrawer;
    private RelativeLayout userDrawer;

    private NavigationAdapter navigationAdapter;
    private ActionBarDrawerToggleCompat drawerToggle;

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
        setContentView(R.layout.navigation_main);
        setContext(getApplicationContext());

        getSupportActionBar().setIcon(R.drawable.ic_action_play);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // Sol tarafta acilan dinamik menu element'leri
        layoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);      // linearDrawer parent'i
        linearDrawer = (LinearLayout) findViewById(R.id.linearDrawer);      // listDrawer + userDrawer
        listDrawer = (ListView) findViewById(R.id.listDrawer);              // Ana Sayfa, Kisisel Bilgiler, ...
        userDrawer = (RelativeLayout) findViewById(R.id.userDrawer);        // Kisi resim&isim alani


        if (listDrawer != null) {
            navigationAdapter = NavigationList.getNavigationAdapter(this);
        }


        listDrawer.setAdapter(navigationAdapter);
        listDrawer.setOnItemClickListener(new DrawerItemClickListener());

        drawerToggle = new ActionBarDrawerToggleCompat(this, layoutDrawer);
        layoutDrawer.setDrawerListener(drawerToggle);

        if (savedInstanceState != null) {
            setLastPosition(savedInstanceState.getInt("LAST_POSITION"));

            if (lastPosition < 5){
                navigationAdapter.resetarCheck();
                navigationAdapter.setChecked(lastPosition, true);
            }

        }else{
            setLastPosition(lastPosition);
            setFragmentList(lastPosition);
        }

        TextView drawerUserName = (TextView)findViewById(R.id.drawer_user_name);
        drawerUserName.setText(MarketUser.getInstance().getUserName());

        TextView drawerUserMail = (TextView)findViewById(R.id.drawer_user_mail);
        drawerUserMail.setText(MarketUser.getInstance().getUserMail());

        // *****************
        // **** Actions ****
        // *****************
        userDrawer.setOnClickListener(userOnClick);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.market, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            Toast.makeText(getApplicationContext() , "HOME MENU CALLED" , Toast.LENGTH_LONG).show();
            return true;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()) {

            case android.R.id.home:

                    Toast.makeText(getApplicationContext() , "HOME MENU SELECTED" , Toast.LENGTH_LONG).show();
                    if (layoutDrawer.isDrawerOpen(linearDrawer)) {
                        layoutDrawer.closeDrawer(linearDrawer);
                    } else {
                        layoutDrawer.openDrawer(linearDrawer);
                    }

            case R.id.menu_basket:

                    fragmentManager.beginTransaction().replace(R.id.content_frame, new BasketFragment()).commit();
                break;

            case R.id.action_settings:
                break;

            case R.id.db_delete:
                getApplicationContext().deleteDatabase(DBHandler.DATABASE_NAME);
                break;

            case R.id.confirmCart:
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
		return super.onOptionsItemSelected(item);
	}


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              ACTION METHODS
     ***********************************************************************************************
     ***********************************************************************************************
     */
    // Kişinin resim alanın tıklatılması olayı
    private View.OnClickListener userOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layoutDrawer.closeDrawer(linearDrawer);
        }
    };


    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              UTIL METHODS
     ***********************************************************************************************
     ***********************************************************************************************
     */
    public void confirmCart(){

        Map parameters = new HashMap();
        parameters.put("cdosDo", "confirmOrderList");

        Map operationInfo = new HashMap();
        operationInfo.put(Guppy.http_Map_OP_TYPE, HttpHandler.HTTP_OP_NORMAL);
        operationInfo.put(Guppy.http_Map_OP_URL, Guppy.url_Servlet_Order);

        new HttpHandler( getApplicationContext() , "ORDERCONFIRM").execute( operationInfo , parameters);

    }


    public int getCounterItemDownloads() {
        return counterItemDownloads;
    }
    public void setCounterItemDownloads(int value) {
        this.counterItemDownloads = value;
    }


    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              ACTIONBAR DRAWER
     ***********************************************************************************************
     ***********************************************************************************************
     */
    private class ActionBarDrawerToggleCompat extends ActionBarDrawerToggle {

        public ActionBarDrawerToggleCompat(Activity mActivity, DrawerLayout mDrawerLayout){
            super(
                    mActivity,
                    mDrawerLayout,
                    R.drawable.ic_action_navigation_drawer,
                    R.string.drawer_open,
                    R.string.drawer_close);
        }

        @Override
        public void onDrawerClosed(View view) {
            supportInvalidateOptionsMenu();
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            navigationAdapter.notifyDataSetChanged();
            supportInvalidateOptionsMenu();
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Log.i("DRAWER ITEM" , "Position :: " + position);

            setLastPosition(position);
            setFragmentList(lastPosition);
            layoutDrawer.closeDrawer(linearDrawer);
        }
    }
    public void setLastPosition(int position){
        this.lastPosition = position;
    }
    private void setFragmentList(int position){

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            case 0:

                fragmentManager.beginTransaction().replace(R.id.content_frame, new ViewPagerFragmentList()).commit();
                break;
            case 1:
                Toast.makeText(getApplicationContext(), "Kişisel bilgilerim called", Toast.LENGTH_SHORT).show();
                fragmentManager.beginTransaction().replace(R.id.content_frame, ProfileFragment.newInstance("","")).commit();
                break;
            case 2:
                Toast.makeText(getApplicationContext(), "Adreslerim called", Toast.LENGTH_SHORT).show();
                fragmentManager.beginTransaction().replace(R.id.content_frame, ProfileAddressFragment.newInstance("","")).commit();
                break;
            case 3:
                Toast.makeText(getApplicationContext(), "Önceki Siparişlerim called", Toast.LENGTH_SHORT).show();
                fragmentManager.beginTransaction().replace(R.id.content_frame, ExpandableFragment.newInstance("", "")).commit();
                break;

            default:
                Toast.makeText(getApplicationContext(), "implement other fragments here", Toast.LENGTH_SHORT).show();
                break;
        }

        if (position < 5){
            navigationAdapter.resetarCheck();
            navigationAdapter.setChecked(position, true);
        }
    }

}
