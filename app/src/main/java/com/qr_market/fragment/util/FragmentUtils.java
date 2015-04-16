package com.qr_market.fragment.util;

import android.content.Context;

import com.qr_market.R;

/**
 * @author Kemal Sami KARACA
 * @since 12.04.2015
 * @version v1.02
 */
public class FragmentUtils {

    //Set all the navigation icons and always to set "zero 0" for the item is a category
    public static int[] iconNavigation = new int[] {
            R.drawable.ic_home,
            R.drawable.ic_profile,
            R.drawable.ic_pages,
            R.drawable.ic_communities,
            R.drawable.ic_whats_hot,
            0
        };

    //get title of the item navigation
    public static String getTitleItem(Context context, int pos){
        String[] title = context.getResources().getStringArray(R.array.nav_menu_items);
        return title[pos];
    }

    public static int[] colors = new int[] {
            R.color.blue_dark,
            R.color.blue_dark,
            R.color.red_dark,
            R.color.red_light,
            R.color.green_dark,
            R.color.green_light,
            R.color.orange_dark,
            R.color.orange_light,
            R.color.purple_dark,
            R.color.purple_light
        };

}
