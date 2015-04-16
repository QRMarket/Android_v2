package com.qr_market.fragment.adapter;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qr_market.fragment.ui.BarcodeFragment;
import com.qr_market.fragment.ui.BasketFragment;
import com.qr_market.fragment.ui.CartFragment;
import com.qr_market.fragment.ui.ProfileFragment;
import com.qr_market.fragment.util.FragmentPagerList;
import com.qr_market.fragment.util.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kemal Sami KARACA
 * @since 02.03.2015
 * @version v1.01
 */
public class GuppyFragmentPageAdapter extends FragmentPagerAdapter {

    private List<FragmentPagerList> fragmentTabs;
    public GuppyFragmentPageAdapter(FragmentManager fm , List<FragmentPagerList> fragmentTabs) {
        super(fm);
        this.fragmentTabs = fragmentTabs;
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
        return fragmentTabs.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragmentTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTabs.get(position).getTitle();
    }
}
