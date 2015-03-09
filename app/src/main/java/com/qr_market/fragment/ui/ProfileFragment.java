package com.qr_market.fragment.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.qr_market.Guppy;
import com.qr_market.R;
import com.qr_market.activity.MarketActivity;
import com.qr_market.async.ImageHandler;
import com.qr_market.http.HttpHandler;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kemal Sami KARACA
 * @since 03.2015
 * @version v1.01
 */
public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {}

    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              INTERFACE
     ***********************************************************************************************
     ***********************************************************************************************
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              OVERRIDE METHODS
     ***********************************************************************************************
     ***********************************************************************************************
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button uploader = (Button)view.findViewById(R.id.uploader);
        uploader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        final ImageView img = (ImageView)view.findViewById(R.id.imageView);
        Button bitmap = (Button)view.findViewById(R.id.bitmap);
        bitmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(img==null){
                    System.out.println("Element is NULL");
                }else{
                    //new ImageHandler(img).execute(Guppy.url_Servlet_IMAGE + "/pim061001.jpg");
                }

            }
        });

        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*
	 * This method used to get result data of sub-activities(intents)
	 */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {

                Log.i("YY YY YY" , "onActivityResult called");
                Uri selectedImage = intent.getData();

                String[] projection = { MediaStore.MediaColumns.DATA };
                for(String str : projection){
                    Log.i("PROJECTION :: " , str);
                }

                Cursor mCur = getActivity().getContentResolver().query(selectedImage, projection, null, null, null);

                int x = mCur.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                Log.i("CURSOR :: " , "column index " + x);

                mCur.moveToFirst();
                String imagePath = mCur.getString(x);

                Log.i("File Path" , imagePath);


                Map parameters = new HashMap();
                parameters.put("authDo", "carpeLogin");

                Map operationInfo = new HashMap();
                operationInfo.put(Guppy.http_Map_OP_TYPE, HttpHandler.HTTP_OP_MULTIPART);
                operationInfo.put(Guppy.http_Map_OP_URL, Guppy.url_Servlet_Sample);

                new HttpHandler().execute( operationInfo , parameters);

            }

        }
    }




    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                                  UTIL FUNCTIONs
     ***********************************************************************************************
     ***********************************************************************************************
     */

    public String getPath(Uri uri) {
        //String[] projection = { MediaStore.MediaColumns.DATA };
        //Cursor cursor = managedQuery(uri, projection, null, null, null);
        //column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        //cursor.moveToFirst();
        //imagePath = cursor.getString(column_index);

        return "";
    }


}
