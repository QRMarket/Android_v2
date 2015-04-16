package com.qr_market.fragment.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qr_market.Guppy;
import com.qr_market.R;
import com.qr_market.fragment.ui.BasketFragment;
import com.qr_market.http.HttpHandler;
import com.qr_market.util.MarketProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kemal Sami KARACA
 * @since 02.03.2015
 * @version v1.02
 */
public class BasketFragmentListAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private Context mContext;
    private SparseBooleanArray mSelectedItemsIds;
    private BasketFragmentListAdapter myBasketAdapter;

    public static MarketProduct workingProduct;

    List<MarketProduct> productList = new ArrayList<MarketProduct>();
    ViewHolder viewHolder;
    LayoutInflater inflater;

    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              CONSTRUCTOR
     ***********************************************************************************************
     ***********************************************************************************************
     */
    public BasketFragmentListAdapter(Context context, List<MarketProduct> productList) {

        myBasketAdapter = BasketFragmentListAdapter.this;
        mSelectedItemsIds = new SparseBooleanArray();
        mContext            = context;
        this.productList    = productList;
        inflater = LayoutInflater.from(context);

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
    public View getView(int position, final View convertView, ViewGroup parent) {

        View v = convertView;

        int type = getItemViewType(position);
        if(convertView==null){

            // -1- Create new viewHolder
            viewHolder = new ViewHolder();

            // -2- inflate the layout
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.basket_list_style, null);

            // -3- Set up the ViewHolder
            viewHolder.title = (TextView) v.findViewById(R.id.ProductTitle);
            viewHolder.price = (TextView) v.findViewById(R.id.PrPrice);
            viewHolder.detail = (TextView) v.findViewById(R.id.ProductDetail);
            viewHolder.image = (ImageView) v.findViewById(R.id.ProductImg);
            viewHolder.amount=(EditText) v.findViewById(R.id.TxtProductAmount);
            viewHolder.AddBtn=(ImageButton) v.findViewById(R.id.button);
            viewHolder.DownBtn=(ImageButton) v.findViewById(R.id.btnReduce);

            // -4-store the holder with the view.
            v.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MarketProduct product = productList.get(position);

        final String item   = product.getProduct_name();
        final String detail = product.getP_detail();
        final String price  = product.getProduct_price();
        final int ImgId     = product.getImage_id();
        final double productPrice = Double.parseDouble(price) * product.getProduct_amount();

        String strProductPrice = ((product.getProduct_amount() % 1==0) ? (""+ (int) product.getProduct_amount()) : (String.format("%,.2f",product.getProduct_amount())) );
        viewHolder.amount.setText( strProductPrice );
        viewHolder.title.setText(item);
        viewHolder.detail.setText(detail);

        try{
            viewHolder.image.setImageBitmap(product.getProduct_image().get(0));
        }catch (NullPointerException err){
            viewHolder.image.setImageResource(ImgId);
        }

        viewHolder.price.setText(String.format("%,.2f",productPrice)+" TL");
        viewHolder.image.setTag(position);


        //  ViewHolder ACTION
        //      ViewHolder ACTION
        //          ViewHolder ACTION
        viewHolder.AddBtn.setTag(position);
        viewHolder.AddBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // !!! Her arttırma yada azaltma işleminde yemeksepeti'nde olduğu gibi post edilecektir !!!

                // -1-get view itself
                int position = ((Integer) v.getTag()).intValue();

                // -2-replace with process view
                // -3-request to increase amount of product
                workingProduct = productList.get(position);

                Map parameters = new HashMap();
                parameters.put("cdosDo", "productUpdate");
                parameters.put("cdpUID", workingProduct.getProduct_uid());
                parameters.put("cdpAmount", "" + (workingProduct.getProduct_amount()+1) );

                Map operationInfo = new HashMap();
                operationInfo.put(Guppy.http_Map_OP_TYPE, HttpHandler.HTTP_OP_NORMAL);
                operationInfo.put(Guppy.http_Map_OP_URL, Guppy.url_Servlet_Order);

                new HttpHandler(mContext,"ORDERUPDATE",myBasketAdapter).execute( operationInfo , parameters);

                /* işlem sonrasında
                updateProduct.setProduct_amount(updateProduct.getProduct_amount()+1);
                notifyDataSetChanged();
                */

            }
        });

        viewHolder.DownBtn.setTag(position);
        viewHolder.DownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Decrease quantity
                int position = ((Integer) v.getTag()).intValue();

                MarketProduct updateProduct = productList.get(position);
                updateProduct.setProduct_amount(updateProduct.getProduct_amount() - 1);

                if(updateProduct.getProduct_amount()==0)
                {
                    productList.remove(updateProduct);
                }
                notifyDataSetChanged();
            }
        });


        return v;
    }



    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              CHILD CLASSES
     ***********************************************************************************************
     ***********************************************************************************************
     */
    public class ViewHolder {
            TextView title;
            TextView detail;
            TextView price;
            ImageView image;
            EditText amount;
            ImageButton AddBtn;
            ImageButton DownBtn;
            TextView totalPrice;
    }
}
