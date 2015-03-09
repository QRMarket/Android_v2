package com.qr_market.fragment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qr_market.fragment.ui.BarcodeFragment;
import com.qr_market.fragment.ui.CartFragment;
import com.qr_market.fragment.ui.ProfileFragment;

/**
 * @author Kemal Sami KARACA
 * @since 02.03.2015
 * @version v1.01
 */
public class GuppyFragmentPageAdapter extends FragmentPagerAdapter {


    public GuppyFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              OVERRIDE METHODS
     ***********************************************************************************************
     ***********************************************************************************************
     */
    @Override
    public Fragment getItem(int position) {

        Fragment fragment=null;
        switch (position){
            case 0:
                fragment = BarcodeFragment.newInstance("","");
                break;
            case 1:
                fragment = CartFragment.newInstance("","");
                break;
            case 2:
                fragment = ProfileFragment.newInstance("","");
                break;
            default:
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
