package com.qr_market.fragment.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import com.qr_market.R;


/**
 * @author Kemal Sami KARACA
 * @since 20.04.2015
 * @version v1.2
 *
 * @description
 *      xxx
 *
 * @last 20.04.2015
 */
public class PaymentResultFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    private View view;

    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                  CONSTRUCTOR & ENCAPSULATION METHODS
     ***********************************************************************************************
     ***********************************************************************************************
     */
    public static PaymentResultFragment newInstance(String param1, String param2) {
        PaymentResultFragment fragment = new PaymentResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PaymentResultFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // -1- Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_payment_result, container, false);




        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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





    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              INTERFACE CLASS
     ***********************************************************************************************
     ***********************************************************************************************
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
