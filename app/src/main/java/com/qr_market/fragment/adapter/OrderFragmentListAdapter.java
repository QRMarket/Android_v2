package com.qr_market.fragment.adapter;

/**
 * Created by orxan on 11.05.2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.qr_market.R;
import com.qr_market.util.MarketOrder;
import com.qr_market.util.MarketProduct;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;



/**
 * @author Kemal Sami KARACA
 * @since 26.04.2015
 * @version v1.01
 *
 * @description
 *      Adapter of order list
 *
 * @last 12.05.2015
 */
public class OrderFragmentListAdapter extends BaseExpandableListAdapter {

    /*
    private HashMap<String,List<MarketOrder>> list_child;
    public OrderFragmentListAdapter(HashMap<String, List<MarketOrder>> list_child, List<MarketOrder> orderList, Context context) {
        this.list_child = list_child;
        //this.orderList=orderList;
        this.context = context;
    }
    */



    private Activity activity;
    private Context context;
    private OrderFragmentListAdapter orderListAdapter;
    private List<MarketOrder> orderList = MarketOrder.getOrderListInstance();
    private List<MarketProduct> orderProductList = null;
    private LayoutInflater inflater;
    //private ViewHolder viewHolder;

    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              CONSTRUCTOR
     ***********************************************************************************************
     ***********************************************************************************************
     */
    public OrderFragmentListAdapter(Activity activity, List<MarketOrder> orderList) {

        this.activity           = activity;
        this.context            = activity.getApplicationContext();
        this.orderListAdapter   = OrderFragmentListAdapter.this;
        this.inflater           = LayoutInflater.from(context);
        this.setOrderList(orderList);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.orderList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        if(this.orderList.get(groupPosition).getOrderProductList().size()!=0){

            return this.orderList.get(groupPosition).getOrderProductList().get(childPosition);

        }else{

            // GET PRODUCT LIST FROM SERVER
            // HTTP CALL
            MarketProduct myProduct = new MarketProduct();
            myProduct.setProduct_amount(12);
            myProduct.setProduct_price("13");
            return myProduct;
        }

/*
        MarketProduct childProduct;
        try{
            childProduct = this.orderList.get(groupPosition).getOrderProductList().get(childPosition);

        }catch (NullPointerException err){

            // GET PRODUCT LIST FROM SERVER
            // HTTP CALL
            MarketProduct myProduct = new MarketProduct();
            myProduct.setProduct_amount(12);
            myProduct.setProduct_price("13");
            childProduct = myProduct;
        }

        return childProduct;
*/

    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return 1;
        // return this.orderList.get(groupPosition).getOrderProductList().size();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {

        //--Here we are getting child values from current position--
        MarketProduct currentProduct = (MarketProduct)getChild(groupPosition,childPosition);

        //--Here we are getting child view--
        if(view==null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.order_list_child_layout, null);
        }

        final TextView product_amount   = (TextView)view.findViewById(R.id.ordered_product_amount);
        final TextView product_date     = (TextView)view.findViewById(R.id.order_date);
       // final FontAwesomeText timeIcon  = (FontAwesomeText) view.findViewById(R.id.order_time_icon);


        //--Set The child values to the related layout view--
        product_amount.setText(""+currentProduct.getProduct_amount());

        //Set Time Icon
        //timeIcon.setIcon("fa-clock-o");

        return view;
    }





    @Override
    public int getGroupCount() {
        return this.orderList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {

        //--Here we are getting parent values from current position--
        final MarketOrder parentOrderData = orderList.get(groupPosition);


        if(view==null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.order_list_parent_layout, null);
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
