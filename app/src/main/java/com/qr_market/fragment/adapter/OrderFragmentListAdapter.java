package com.qr_market.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.qr_market.R;
import com.qr_market.util.MarketOrder;

import java.util.List;

/**
 * @author Kemal Sami KARACA
 * @since 26.04.2015
 * @version v1.01
 *
 * @description
 *      Adapter of order list
 *
 * @last 26.04.2015
 */
public class OrderFragmentListAdapter extends BaseAdapter {


    private Activity activity;
    private Context context;
    private OrderFragmentListAdapter orderListAdapter;
    private List<MarketOrder> orderList = MarketOrder.getOrderListInstance();
    private LayoutInflater inflater;
    private ViewHolder viewHolder;


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
        this.setOrderList(orderList);
        inflater                = LayoutInflater.from(context);
    }



    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              OVERRIDE METHODS
     ***********************************************************************************************
     ***********************************************************************************************
     */
    @Override
    public int getCount() {
        return getOrderList().size();
    }

    @Override
    public Object getItem(int position) {
        return getOrderList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View orderView = convertView;
        if(convertView==null){

                // -1- Create new viewHolder
                viewHolder = new ViewHolder();

                // -2- inflate the layout
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                orderView = inflater.inflate(R.layout.order_list_style, null);

                // -3- Set up the ViewHolder
                viewHolder.orderId          = (TextView) orderView.findViewById(R.id.order_item_title);
                viewHolder.orderCompany     = (TextView) orderView.findViewById(R.id.order_market_name);
                viewHolder.orderDate        = (TextView) orderView.findViewById(R.id.order_date);
                viewHolder.orderAddressCity = (TextView) orderView.findViewById(R.id.order_market_city);
                viewHolder.orderAddress     = (TextView) orderView.findViewById(R.id.ordered_market_adress);

                // -4-store the holder with the view.

                //Set Time Icon
                FontAwesomeText timeIcon = (FontAwesomeText) orderView.findViewById(R.id.order_time_icon);
                timeIcon.setIcon("fa-clock-o");
                orderView.setTag(viewHolder);




        }else{

                viewHolder = (ViewHolder) convertView.getTag();
        }

        // At this point we have viewHolder and we need to fill it with orderList
        MarketOrder order = getOrderList().get(position);

        // Start settings
        viewHolder.orderId.setText(order.getOrderId());
        viewHolder.orderCompany.setText(order.getCompanyName());
        viewHolder.orderDate.setText(order.getDate());
        viewHolder.orderAddressCity.setText(order.getAddress().getCity());
        viewHolder.orderAddress.setText(order.getAddress().getBorough() + "/" + order.getAddress().getLocality());


        //ReOrder Button here
        BootstrapButton reOrder=(BootstrapButton) orderView.findViewById(R.id.ReOrderBtn);
        reOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Bu işlem yapım aşamasındadır..", Toast.LENGTH_LONG).show();

            }
        });

        return orderView;
    }

    public List<MarketOrder> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<MarketOrder> orderList) {
        this.orderList = orderList;
    }


    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              CHILD CLASSES
     ***********************************************************************************************
     ***********************************************************************************************
     */
    public class ViewHolder {
        TextView orderId;
        TextView orderCompany;
        TextView orderDate;
        TextView orderAddressCity;
        TextView orderAddress;
    }
}
