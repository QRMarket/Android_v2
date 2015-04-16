package com.qr_market.adapter;

/**
 * @author Kemal Sami KARACA
 * @since 14.04.2015
 * @version v1.02
 *
 * @last 14.04.2015
 */
public class NavigationItem {

    public String title;
    public int counter;
    public int icon;
    public boolean isHeader;

    public NavigationItem(String title, int icon, boolean header, int counter) {
        this.title = title;
        this.icon = icon;
        this.isHeader = header;
        this.counter = counter;
    }

    public NavigationItem(String title, int icon, boolean header){
        this(title, icon, header, 0 );
    }

    public NavigationItem(String title, int icon) {
        this(title, icon, false);
    }

}
