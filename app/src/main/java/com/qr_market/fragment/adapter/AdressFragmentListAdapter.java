package com.qr_market.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qr_market.R;
import com.qr_market.util.MarketUser;
import com.qr_market.util.MarketUserAddress;

import java.util.List;

/**
 * Created by orxan on 24.05.2015.
 * @author orxan ALIRZAYEV
 *
 * @description
 *      Adapter for ProfileAdressFragment
 */
public class AdressFragmentListAdapter extends BaseAdapter {

    ViewHolder viewHolder;
    List<MarketUserAddress> list = MarketUser.getAddressList();

    public AdressFragmentListAdapter(Context context, List<MarketUserAddress> list) {
        this.mContext = context;
        this.list = list;
    }


    @Override

    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //Get Adres in Current position
        MarketUserAddress address = list.get(position);
        viewHolder = new ViewHolder();
        if (convertView == null) {

            LayoutInflater inflater;

            //Inflate Layout
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.address_layout_style, null);

            //Initialize the layout views
            viewHolder.city = (TextView) convertView.findViewById(R.id.user_city);
            viewHolder.borough = (TextView) convertView.findViewById(R.id.user_adress);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Get Adress Datas
        final String city = address.getCity();
        final String borough = address.getAddressString();

        //Display Adresses on related views
        viewHolder.city.setText(city);
        viewHolder.borough.setText(borough.toString());


        return convertView;
    }

    public class ViewHolder {
        TextView city;
        TextView borough;

    }

    private final Context mContext;
}

