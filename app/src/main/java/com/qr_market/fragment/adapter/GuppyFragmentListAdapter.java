package com.qr_market.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qr_market.R;
import com.qr_market.util.MarketProduct;
import com.qr_market.util.MarketUser;

import java.util.ArrayList;
import java.util.List;



/**
 * @author Orxan
 * @author Kemal Sami KARACA
 * @since 07.03.2015
 * @version v1.01
 *
 * @last 08.03.2015
 */
public class GuppyFragmentListAdapter extends BaseAdapter {

    ViewHolder viewHolder;

    private List<MarketProduct> productList = new ArrayList<MarketProduct>();
    private Context mContext;

    public GuppyFragmentListAdapter(Context context, List<MarketProduct> productList) {
        mContext = context;
        this.productList=productList;
    }


    public class ViewHolder {
        TextView title;
        TextView detail;
        TextView price;
        ImageView image;
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
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            System.out.println("-- PRODUCT POSITION IS " + position);
            System.out.println("++ " + MarketUser.getInstance().getProductList().get(position).getProduct_image_url());

            View view = convertView;
            if(productList.size()>=1){

                if(convertView==null){


                    // inflate the layout
                    LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflator.inflate(R.layout.basket_list_style, null);

                    // well set up the ViewHolder
                    viewHolder = new ViewHolder();
                    viewHolder.title = (TextView) view.findViewById(R.id.ProductTitle);
                    viewHolder.price = (TextView) view.findViewById(R.id.PrPrice);
                    viewHolder.detail = (TextView) view.findViewById(R.id.ProductDetail);
                    viewHolder.image = (ImageView) view.findViewById(R.id.ProductImg);

                    // store the holder with the view.
                    view.setTag(viewHolder);

                }else{
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                System.out.println("-- PRODUCTION IMAGE SIZE :: " + productList.get(position).getProduct_image().size() );
                if(productList.get(position).getProduct_image().size() >0 ){
                    viewHolder.image.setImageBitmap(productList.get(position).getProduct_image().get(0));
                }else{

                }

                viewHolder.title.setText(productList.get(position).getProduct_name());
                viewHolder.detail.setText("sss");
                viewHolder.price.setText(productList.get(position).getProduct_price());
            }


        return view;

    }
}
