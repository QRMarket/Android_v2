package com.qr_market.adapter;

import android.content.Context;

import com.qr_market.R;
import com.qr_market.fragment.util.FragmentUtils;

/**
 * @author Kemal Sami KARACA
 * @since 14.04.2015
 * @version v1.02
 *
 * @last 14.04.2015
 */
public class NavigationList {


    public static NavigationAdapter getNavigationAdapter(Context context){

        NavigationAdapter navigationAdapter = new NavigationAdapter(context);
        String[] menuItems = context.getResources().getStringArray(R.array.nav_menu_items);

        for (int i=0; i<menuItems.length; i++) {

            String title = menuItems[i];
            NavigationItem itemNavigation;
            itemNavigation = new NavigationItem(title, FragmentUtils.iconNavigation[i]);
            navigationAdapter.addItem(itemNavigation);
        }
        return navigationAdapter;
    }

}
