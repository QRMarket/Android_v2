package com.qr_market.fragment.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qr_market.Guppy;
import com.qr_market.R;
import com.qr_market.activity.MarketActivity;
import com.qr_market.fragment.adapter.GuppyFragmentPageAdapter;
import com.qr_market.fragment.util.FragmentPagerList;
import com.qr_market.fragment.util.FragmentUtils;
import com.qr_market.http.HttpHandler;
import com.qr_market.sliding.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewPagerFragmentList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewPagerFragmentList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPagerFragmentList extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private List<FragmentPagerList> fragmentTabs = new ArrayList<FragmentPagerList>();


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewPagerFragmentList.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPagerFragmentList newInstance(String param1, String param2) {
        ViewPagerFragmentList fragment = new ViewPagerFragmentList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ViewPagerFragmentList() {
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

        fragmentTabs.add(new FragmentPagerList(0, "Ana Sayfa", getResources().getColor(FragmentUtils.colors[0]),  Color.GRAY));
        fragmentTabs.add(new FragmentPagerList(1, "Sepet", getResources().getColor(FragmentUtils.colors[0]),  Color.GRAY));
        fragmentTabs.add(new FragmentPagerList(2, "Kampanyalar", getResources().getColor(FragmentUtils.colors[0]),  Color.GRAY));
        fragmentTabs.add(new FragmentPagerList(3, "Önceki Siparişler", getResources().getColor(FragmentUtils.colors[0]),  Color.GRAY));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_pager_fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.mPager);

        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new GuppyFragmentPageAdapter(getChildFragmentManager(), fragmentTabs));


        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.mTabs);
        mSlidingTabLayout.setBackgroundResource(R.color.white);
        mSlidingTabLayout.setViewPager(mViewPager);

        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return fragmentTabs.get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return fragmentTabs.get(position).getDividerColor();
            }
        });

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



    /*
    * This method used to get result data of sub-activities(intents)
    */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // 110 < -- > QRCODE
        if (requestCode == 110) {

            if (resultCode == Activity.RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast toast = Toast.makeText( MarketActivity.getContext(), "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                toast.show();

                Log.i("Content", contents);
                Log.i("Format",format);

                Map parameters = new HashMap();
                parameters.put("cdpsDo", "getProduct");
                parameters.put("cdpUID", contents);


                Map operationInfo = new HashMap();
                operationInfo.put(Guppy.http_Map_OP_TYPE, HttpHandler.HTTP_OP_NORMAL);
                operationInfo.put(Guppy.http_Map_OP_URL, Guppy.url_Servlet_Product);
                //operationInfo.put(Guppy.http_Map_OP_URL, Guppy.url_serverPort + "/" + contents);

                Log.i("Final URL" , (String)operationInfo.get(Guppy.http_Map_OP_URL));

                new HttpHandler( this.getActivity().getApplicationContext() , "PRODUCT").execute( operationInfo , parameters);
            }
        }
    }

}
