package com.qr_market.fragment.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.qr_market.Guppy;
import com.qr_market.R;
import com.qr_market.fragment.adapter.OrderFragmentListAdapter;
import com.qr_market.http.HttpHandler;
import com.qr_market.util.MarketOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kemal Sami KARACA
 * @since 26.04.2015
 * @version v1.01
 *
 * @description
 *      This fragment holds list of user orders
 *
 * @last 26.04.2015
 */
public class OrderFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private OrderFragmentListAdapter orderAdapter;

    private Map parameters;
    private View view;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public OrderFragment() {
        // Required empty public constructor
    }


    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              ENCAPSULATION
     ***********************************************************************************************
     ***********************************************************************************************
     */
    public OrderFragmentListAdapter getOrderAdapter() {
        return orderAdapter;
    }

    public void setOrderAdapter(OrderFragmentListAdapter orderAdapter) {
        this.orderAdapter = orderAdapter;
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

        this.view = inflater.inflate(R.layout.fragment_order, container, false);

        // Get list view
        final ListView orderListView  = (ListView) view.findViewById(R.id.order_list_view);
        View header = getActivity().getLayoutInflater().inflate(R.layout.order_lv_header, null);
        orderListView.addHeaderView(header);

        //SET ADAPTER
        setOrderAdapter(new OrderFragmentListAdapter(getActivity(), MarketOrder.getOrderListInstance()));
        orderListView.setAdapter(getOrderAdapter());



        // Refresh page
        FontAwesomeText orderRefresh = (FontAwesomeText)view.findViewById(R.id.order_lv_refresh);
        orderRefresh.setIcon("fa-refresh");
        parameters = new HashMap();
        orderRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // API CALL
                Toast.makeText( getActivity(), "Refresh clicked", Toast.LENGTH_LONG).show();

                parameters = new HashMap();
                parameters.put("cdosDo", "getOrderLists");

                Map operationInfo = new HashMap();
                operationInfo.put(Guppy.http_Map_OP_TYPE, HttpHandler.HTTP_OP_NORMAL);
                operationInfo.put(Guppy.http_Map_OP_URL, Guppy.url_Servlet_Order);

                new HttpHandler( getActivity() , "GETORDERLIST" , orderAdapter).execute( operationInfo , parameters);

            }
        });


        parameters = new HashMap();
        parameters.put("cdosDo", "getOrderLists");

        Map operationInfo = new HashMap();
        operationInfo.put(Guppy.http_Map_OP_TYPE, HttpHandler.HTTP_OP_NORMAL);
        operationInfo.put(Guppy.http_Map_OP_URL, Guppy.url_Servlet_Order);

        new HttpHandler( getActivity() , "GETORDERLIST" , orderAdapter).execute( operationInfo , parameters);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
