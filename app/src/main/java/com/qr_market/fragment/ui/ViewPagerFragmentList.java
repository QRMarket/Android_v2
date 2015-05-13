package com.qr_market.fragment.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
 * @author Kemal Sami KARACA
 * @since 02.2015
 * @version v1.01
 *
 * @description
 *      Bu class FragmentPagerList içerisinden Fragment'lerin alınıp, GuppyFragmentPageAdapter
 *      kullanılarak tablar ve sayfaların konulmasını sağlayan class'tır.
 *
 *      MarketActivity'e eklenecek olan yeni fragment'ler buraya eklenmesi gerekmektedir.
 *
 *
 * @last 20.04.2015
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

        fragmentTabs.add(new FragmentPagerList(BarcodeFragment.newInstance("","") , "Ana Sayfa", getResources().getColor(FragmentUtils.colors[0]),  Color.BLUE));
        fragmentTabs.add(new FragmentPagerList(BasketFragment.newInstance("","") , "Sepet", getResources().getColor(FragmentUtils.colors[3]),  Color.BLUE));
        fragmentTabs.add(new FragmentPagerList(ProfileFragment.newInstance("","") , "Kampanyalar", getResources().getColor(FragmentUtils.colors[5]),  Color.BLUE));
        fragmentTabs.add(new FragmentPagerList(OrderFragment.newInstance("", "") , "Önceki Siparişler", getResources().getColor(FragmentUtils.colors[8]),  Color.BLUE));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_pager_fragment_list, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        // MarketActivity --> PAGES
        // MarketActivity --> PAGES
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.mPager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new GuppyFragmentPageAdapter(getChildFragmentManager(), fragmentTabs));


        // MarketActivity --> TABS
        // MarketActivity --> TABS
        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.mTabs);
        mSlidingTabLayout.setBackgroundResource(R.color.white);
        mSlidingTabLayout.setViewPager(viewPager);
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


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
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
        public void onFragmentInteraction(Uri uri);
    }





    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                              ACTIVITY RESULT CASES
     ***********************************************************************************************
     ***********************************************************************************************
     */
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

                Map parameters = new HashMap();
                parameters.put("cdpsDo", "getProduct");
                parameters.put("cdpUID", contents);

                Map operationInfo = new HashMap();
                operationInfo.put(Guppy.http_Map_OP_TYPE, HttpHandler.HTTP_OP_NORMAL);
                operationInfo.put(Guppy.http_Map_OP_URL, Guppy.url_Servlet_Product);

                new HttpHandler( this.getActivity().getApplicationContext() , "PRODUCT").execute( operationInfo , parameters);
            }
        }
    }

}
