package com.qr_market.fragment.util;

import android.support.v4.app.Fragment;

import com.qr_market.fragment.ui.BarcodeFragment;
import com.qr_market.fragment.ui.BasketFragment;
import com.qr_market.fragment.ui.CartFragment;
import com.qr_market.fragment.ui.ProfileFragment;

/**
 * @author Kemal Sami KARACA
 * @since 12.04.2015
 * @version v1.01
 */
public class FragmentPagerList {


    private final CharSequence mTitle;
    private final int mIndicatorColor;
    private final int mDividerColor;
    private final int position;


    private Fragment[] listFragments;
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

    public CharSequence getTitle() {
        return mTitle;
    }

    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    public int getDividerColor() {
        return mDividerColor;
    }

}
