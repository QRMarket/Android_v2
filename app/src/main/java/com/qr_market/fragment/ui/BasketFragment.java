package com.qr_market.fragment.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.qr_market.R;
import com.qr_market.fragment.adapter.BasketFragmentListAdapter;
import com.qr_market.util.MarketUser;

/**
 * @author Kemal Sami KARACA
 * @since 04.2015
 * @version v1.01
 *
 * @description
 *      xxx
 *
 * @last 11.05.2015
 */
public class BasketFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private static View view;
    public static View getViewBasketFragment(){
        return view;
    }
    private BasketFragmentListAdapter mAdapter;
    private boolean isChangeToPaymentFragment = false;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasketFragment.
     */
    public static BasketFragment newInstance(String param1, String param2) {
        BasketFragment fragment = new BasketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BasketFragment() {
        // Required empty public constructor
    }

    public BasketFragmentListAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(BasketFragmentListAdapter mAdapter) {
        this.mAdapter = mAdapter;
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

        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_basket, container, false);

        final LinearLayout emptyBasketLayout = (LinearLayout) view.findViewById(R.id.fragment_basket_main_layout);

        final ListView lv  = (ListView) view.findViewById(R.id.listViewSwp);
        View header = getActivity().getLayoutInflater().inflate(R.layout.basket_lv_header, null);
        lv.addHeaderView(header);

        //Add Footer layout To LV
        View footer=getActivity().getLayoutInflater().inflate(R.layout.basket_lv_footer,null);
        final BootstrapButton LvGoBtn=(BootstrapButton) footer.findViewById(R.id.LvBtnGo);
        lv.addFooterView(footer);

        //SET ADAPTER
        setmAdapter(new BasketFragmentListAdapter(getActivity(), MarketUser.getProductList()));
        lv.setAdapter(getmAdapter());
        lv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if(MarketUser.getProductList().size()>0 && !isChangeToPaymentFragment){
                    //Toast.makeText( getActivity().getApplicationContext(), "Open Continue button", Toast.LENGTH_LONG).show();
                    LvGoBtn.setEnabled(true);
                    emptyBasketLayout.setVisibility(View.INVISIBLE);
                    lv.setVisibility(View.VISIBLE);

                }else if(isChangeToPaymentFragment){
                    lv.setVisibility(View.INVISIBLE);
                    emptyBasketLayout.setVisibility(View.INVISIBLE);
                }

                else{
                    //Toast.makeText( getActivity().getApplicationContext(), "Close Continue button", Toast.LENGTH_LONG).show();
                    LvGoBtn.setEnabled(false);
                    emptyBasketLayout.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.INVISIBLE);
                }

                Log.i("-GUPPY- " , "Size of the basket is " + lv.getAdapter().getCount());
            }
        });

        //Set LV footer Button clickable
        LvGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame2, PaymentFragment.newInstance("",""), "NewFragmentTag");
                ft.commit();

                /**
                 * Payment Fragment, Basket Fragment içerisindeki Fragment Layout'u kullanmaktadır
                 * Bunun yerine ikisininde ustünde bir fragment tanımlanıp, her iki fragment'inde
                 * bu çatı layout üzerinden çağırılması aşağıdaki flag olayını çözecektir.
                 */

                // addOnLayoutChangeListener ilgili listenerden sonra çağırıldığından
                // "isChangeToPaymentFragment" değeri flag olarak kullanılmıştır.
                // Daha sonra daha etkili bir yol seçilmelidir.
                isChangeToPaymentFragment = true;


            }
        });

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
