package com.qr_market.fragment.util;

import android.support.v4.app.Fragment;

import com.qr_market.fragment.ui.BarcodeFragment;
import com.qr_market.fragment.ui.BasketFragment;
import com.qr_market.fragment.ui.CartFragment;
import com.qr_market.fragment.ui.ProfileFragment;

import java.util.ArrayList;

/**
 * @author Kemal Sami KARACA
 * @since 12.04.2015
 * @version v1.01
 * @description
 *      aaa
 */
public class FragmentPagerList {

    private final int fragmentIndicatorColor;
    private final int fragmentDividerColor;
    private final CharSequence fragmentTitle;
    private final Fragment fragment;

    public FragmentPagerList(Fragment fragment , CharSequence title, int indicatorColor, int dividerColor) {
        this.fragmentTitle = title;
        this.fragmentIndicatorColor = indicatorColor;
        this.fragmentDividerColor = dividerColor;
        this.fragment = fragment;
    }


/*
    private Fragment[] listFragments;
    private final int position;

    public FragmentPagerList(int position, CharSequence title, int indicatorColor, int dividerColor) {
        this.mTitle = title;
        this.position = position;
        this.mIndicatorColor = indicatorColor;
        this.mDividerColor = dividerColor;

        listFragments = new Fragment[] {
                BarcodeFragment.newInstance("", ""),
                BasketFragment.newInstance("", ""),
                CartFragment.newInstance("", ""),
                ProfileFragment.newInstance("", "")
            };
    }

    public Fragment getFragment() {
        return listFragments[position];
    }
*/

    public Fragment getFragment() {
        return this.fragment;
    }

    public CharSequence getTitle() {
        return fragmentTitle;
    }

    public int getIndicatorColor() {
        return fragmentIndicatorColor;
    }

    public int getDividerColor() {
        return fragmentDividerColor;
    }

}
