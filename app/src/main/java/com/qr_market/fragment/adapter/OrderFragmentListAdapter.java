package com.qr_market.fragment.adapter;

/**
 * Created by orxan on 11.05.2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.qr_market.R;
import com.qr_market.util.MarketOrder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by orxan on 10.05.2015.
 */
public class OrderFragmentListAdapter extends BaseExpandableListAdapter {
    private List<MarketOrder> list_parent;
    private HashMap<String,List<MarketOrder>> list_child;
    Context context;

    private List<MarketOrder> orderList = MarketOrder.getOrderListInstance();


    public OrderFragmentListAdapter(HashMap<String, List<MarketOrder>> list_child, List<MarketOrder> list_parent, Context context) {
        this.list_child = list_child;
        this.list_parent=list_parent;
        this.context = context;

    }


    @Override
    public Object getGroup(int groupPosition) {


        return this.list_parent.get(groupPosition).getCompanyName();

    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {


        return list_child.get(list_parent.get(groupPosition).getCompanyName()).get(childPosititon);

    }



    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return this.list_child.get(this.list_parent.get(groupPosition).getCompanyName()).size();
    }



    @Override
    public View getChildView(int groupPosition, int childPosititon, boolean b, View view, ViewGroup viewGroup) {

        //--Here we are getting child values from current position--
        final MarketOrder childOrderData = (MarketOrder) list_parent.get(groupPosition);

        if(view==null){
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.order_list_child_layout, null);
        }
        final TextView product_amount=(TextView)view.findViewById(R.id.ordered_product_amount);
        final TextView product_date=(TextView)view.findViewById(R.id.order_date);
        final FontAwesomeText timeIcon = (FontAwesomeText) view.findViewById(R.id.order_time_icon);


        //  --Set The child values to the related layout view--


        //Set Time Icon
        timeIcon.setIcon("fa-clock-o");

        return view;
    }


    @Override
    public int getGroupCount() {
        return list_parent.size();
    }





    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {

        //--Here we are getting parent values from current position--
        final MarketOrder parentOrderData = list_parent.get(groupPosition);

        if(view==null){
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.order_list_parent_layout, null);

        }
        final TextView market=(TextView)view.findViewById(R.id.market_name);
        final TextView market_city=(TextView)view.findViewById(R.id.market_city);
        final TextView market_adress=(TextView)view.findViewById(R.id.market_adress);
        final TextView product_date=(TextView)view.findViewById(R.id.date);
        final FontAwesomeText timeIcon = (FontAwesomeText) view.findViewById(R.id.order_time_icon);

        //  --Set The parent values to the related layout view--
        market.setText(parentOrderData.getCompanyName());
        market_city.setText(parentOrderData.getAddress().getCity());
        market_adress.setText(parentOrderData.getAddress().getBorough() + "/" + parentOrderData.getAddress().getLocality());

        // DATE SETTINGS
        try {
            Calendar cal = Calendar.getInstance();
            long date = Long.parseLong(parentOrderData.getDate(), 10);
            cal.setTimeInMillis(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
            product_date.setText(simpleDateFormat.format(cal.getTime()));

        }catch (NumberFormatException e){

            product_date.setText(parentOrderData.getDate());
        }

        //Set Time Icon
        timeIcon.setIcon("fa-clock-o");

        return view;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;

    }

    public List<MarketOrder> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<MarketOrder> orderList) {
        this.orderList = orderList;
    }

}
