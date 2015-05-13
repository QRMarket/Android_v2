package com.qr_market.fragment.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.qr_market.Guppy;
import com.qr_market.R;
import com.qr_market.fragment.adapter.OrderFragmentListAdapter;
import com.qr_market.http.HttpHandler;
import com.qr_market.util.MarketOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kemal Sami KARACA, Orxan ALIRZAYEV
 * @since 26.04.2015
 * @version v1.01
 *
 * @description
 *      This fragment holds list of user orders
 *
 * @last 12.05.2015
 */
public class OrderFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private View view;

    private List<MarketOrder> parent;
    private HashMap<String, List<MarketOrder>> child;

    private final String TAG = "ExpAdapter";
    private OnFragmentInteractionListener mListener;
    private OrderFragmentListAdapter orderAdapter;
    private List<MarketOrder> orderList = MarketOrder.getOrderListInstance();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
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


    public OrderFragmentListAdapter getOrderAdapter() {
        return orderAdapter;
    }

    public void setOrderAdapter(OrderFragmentListAdapter orderAdapter) {
        this.orderAdapter = orderAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        // Get View
        this.view = inflater.inflate(R.layout.fragment_order, container, false);

        // Get list view
        final ExpandableListView orderExpandableListView=(ExpandableListView)view.findViewById(R.id.order_expandable_list_view);

        View header = getActivity().getLayoutInflater().inflate(R.layout.order_lv_header, null);
        orderExpandableListView.addHeaderView(header);

        //SET ADAPTER
        setOrderAdapter(new OrderFragmentListAdapter(getActivity(), MarketOrder.getOrderListInstance()));
        orderExpandableListView.setAdapter(getOrderAdapter());

        //INITIAL ORDER-LIST REQUEST
        Map parameters;
        parameters = new HashMap();
        parameters.put("cdosDo", "getOrderList");

        Map operationInfo = new HashMap();
        operationInfo.put(Guppy.http_Map_OP_TYPE, HttpHandler.HTTP_OP_NORMAL);
        operationInfo.put(Guppy.http_Map_OP_URL, Guppy.url_Servlet_Order);
        new HttpHandler( getActivity() , "GETORDERLIST" , getOrderAdapter()).execute( operationInfo , parameters);


        // Refresh page
        final FontAwesomeText orderRefresh = (FontAwesomeText)view.findViewById(R.id.order_lv_refresh);
        orderRefresh.setIcon("fa-refresh");
        orderRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.clockwise);
                orderRefresh.startAnimation(animation);

                // API CALL
                Toast.makeText( getActivity(), "Refresh clicked", Toast.LENGTH_LONG).show();

                Map parameters = new HashMap();
                parameters.put("cdosDo", "getOrderList");

                Map operationInfo = new HashMap();
                operationInfo.put(Guppy.http_Map_OP_TYPE, HttpHandler.HTTP_OP_NORMAL);
                operationInfo.put(Guppy.http_Map_OP_URL, Guppy.url_Servlet_Order);

                new HttpHandler( getActivity() , "GETORDERLIST" , getOrderAdapter()).execute( operationInfo , parameters);

            }
        });


        //-----Listener Events-----
        orderExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.i(TAG, "Group " + groupPosition + " expanded.");
                Toast.makeText(getActivity(), "Group clicked..", Toast.LENGTH_LONG).show();
            }
        });
        orderExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.i(TAG, "Group " + groupPosition + " collapsed.");
                Toast.makeText(getActivity(), "Group collapsed..", Toast.LENGTH_LONG).show();
            }
        });
        orderExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.i(TAG, "item " + childPosition + " of group " + groupPosition + " clicked.");
                Toast.makeText(getActivity(), "Child Clikc..", Toast.LENGTH_LONG).show();
                return false;
            }
        });


        return view;
    }







   public void PrepareDataForExpandableLv(){
       //Parent for ExpandableLv
        parent=new ArrayList<>();

       //Child of Parent for ExpandableLv
        child = new HashMap<>();


       for(int i=0;i<orderList.size();i++)
       {

           //In here we are getting user orderList
           MarketOrder order=getOrderLists().get(i);

           //and then we add taken order to parent of ExpandableLv
           parent.add(order);

           //We are define child data for order and set this child datas to Child in ExpandableLv with related Parent
           List<MarketOrder>list_data_for_child=new ArrayList<>();
           list_data_for_child.add(order);

           //put child value with related parent
           child.put(parent.get(i).getOrderId(), list_data_for_child);


       }



   }


    public List<MarketOrder> getOrderLists() {
        return orderList;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
