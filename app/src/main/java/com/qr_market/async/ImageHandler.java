package com.qr_market.async;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.qr_market.util.MarketProduct;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kemal Sami KARACA
 * @since 08.03.2015
 * @version v1.01
 */
public class ImageHandler extends AsyncTask<MarketProduct, Void, Bitmap> {

    private MarketProduct products;
    private ImageView bmImage;

    public ImageHandler(ImageView bmImage) {
        if (bmImage==null){
            System.out.println("Incoming value is NULL");
        }else{
            System.out.println("Incoming value is normal");
        }
        this.bmImage = bmImage;
    }


    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              OVERRIDE METHODS
     ***********************************************************************************************
     ***********************************************************************************************
     */
    @Override
    protected Bitmap doInBackground(MarketProduct... products) {

            this.products = products[0];

            String urldisplay = products[0].getProduct_image_url().get(0);
            Bitmap image = null;
            List<Bitmap> imageList;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            products[0].getProduct_image().add(0, image);


        return image;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
