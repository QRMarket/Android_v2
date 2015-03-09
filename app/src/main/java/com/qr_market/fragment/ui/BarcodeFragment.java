package com.qr_market.fragment.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.qr_market.Guppy;
import com.qr_market.R;
import com.qr_market.activity.MarketActivity;
import com.qr_market.fragment.adapter.GuppyFragmentListAdapter;
import com.qr_market.http.HttpHandler;
import com.qr_market.result.Result;
import com.qr_market.util.MarketProduct;
import com.qr_market.util.MarketUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BarcodeFragment extends Fragment {

    // necessary for barcodes
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private static View barcodeView;


    public static BarcodeFragment newInstance(String param1, String param2) {
        BarcodeFragment fragment = new BarcodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BarcodeFragment() {
        // Required empty public constructor
    }


    public static View getViewBarcodeFragment(){
        return barcodeView;
    }
    public void setViewBarcodeFragment(View view) {
        BarcodeFragment.barcodeView = view;
    }

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
        barcodeView = inflater.inflate(R.layout.fragment_home, container, false);

        // Set barcode table to invisible
        TableLayout tableLayout = (TableLayout) barcodeView.findViewById(R.id.barcode_product);
        tableLayout.setVisibility(View.INVISIBLE);

        // BUTTON ACTIONS
        Button barcode = (Button) barcodeView.findViewById(R.id.BtnReadBarcode);
        barcode.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                scanQR(v);
            }
        });

        Button add_button = (Button) barcodeView.findViewById(R.id.btnAddBasket);
        add_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                addToOrderList();
            }
        });


        Button fav_button = (Button) barcodeView.findViewById(R.id.btnAddFav);
        fav_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                List<MarketProduct> mp= MarketUser.getInstance().getProductList();

                System.out.println("-- MP size is " + mp.size());
                for(MarketProduct m : mp){
                    System.out.println(m.getProduct_uid());
                    System.out.println(m.getProduct_name());
                    for(String url: m.getProduct_image_url()){
                        System.out.println("-- URL :: " + url);
                    }
                    System.out.println("Bitmap IMAGE size :: " + m.getProduct_image().size());
                }
            }
        });

        return barcodeView;
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

        if (requestCode == 0) {

            if (resultCode == Activity.RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast toast = Toast.makeText( MarketActivity.getContext(), "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                toast.show();

                Log.i("Content", contents);
                Log.i("Format",format);

                Map parameters = new HashMap();
                parameters.put("cdpsDo", "getProduct");

                Map operationInfo = new HashMap();
                operationInfo.put(Guppy.http_Map_OP_TYPE, HttpHandler.HTTP_OP_NORMAL);
                operationInfo.put(Guppy.http_Map_OP_URL, Guppy.url_serverPort + "/" + contents);

                Log.i("Final URL" , (String)operationInfo.get(Guppy.http_Map_OP_URL));

                new HttpHandler( this.getActivity().getApplicationContext() , "PRODUCT").execute( operationInfo , parameters);
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

    public void scanQR(View v) {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            //showDialog(MarketActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }


    public void addToOrderList(){

        // --------------------------------------
        // -1- SEND REQUEST FOR ADDING TO CART
        Map parameters = new HashMap();
        parameters.put("cdosDo", "addToOrderList");
        parameters.put("cdpUID", MarketProduct.getProductInstance().getProduct_uid());
        parameters.put("cdpAmount", "1");

        Map operationInfo = new HashMap();
        operationInfo.put(Guppy.http_Map_OP_TYPE, HttpHandler.HTTP_OP_NORMAL);
        operationInfo.put(Guppy.http_Map_OP_URL, Guppy.url_Servlet_Order);

        new HttpHandler(getActivity().getApplicationContext(),"ORDER").execute( operationInfo , parameters);
        // --------------------------------------

    }





    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                }

            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }


}
