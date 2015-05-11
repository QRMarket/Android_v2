package com.qr_market.fragment.ui;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.qr_market.R;
import com.qr_market.util.Address;

import java.util.ArrayList;
import java.util.List;


public class ProfileAddressFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private MyListAdapter mAdapter;
    private List<Address> list =new ArrayList<Address>();

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileAddressFragment.
     */
    public static ProfileAddressFragment newInstance(String param1, String param2) {
        ProfileAddressFragment fragment = new ProfileAddressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileAddressFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_address, container, false);


                Address item1=new Address();
                item1.setCity("Ankara");
                item1.setLocality("Emek");
                item1.setBorough("Cankaya");
                list.add(item1);

                // Get the ListView by Id and Get Data using Adapter
                final ListView lv = (ListView)view.findViewById(R.id.listView1);


                View footer=getActivity().getLayoutInflater().inflate(R.layout.address_lv_footer,null);
                Button LvGoBtn=(Button) footer.findViewById(R.id.AddAdress);
                LvGoBtn.setEnabled(true);
                lv.addFooterView(footer);
                lv.setAdapter(new MyListAdapter(getActivity(),list));
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));

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



    //**********************LV ADAPTER**************************************
    private class MyListAdapter extends BaseAdapter {

        ViewHolder viewHolder;
        List<Address> list = new ArrayList<Address>();

        public MyListAdapter(Context context, List<Address> list) {
            mContext = context;
        }


        @Override

        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View adapterView = convertView;

            viewHolder = new ViewHolder();
            if(convertView == null) {

                LayoutInflater inflater;

                inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                adapterView = inflater.inflate(R.layout.address_layout_style, null);


                viewHolder.city = (TextView)adapterView.findViewById(R.id.lv_item_header);
                viewHolder.borough  = (TextView)adapterView.findViewById(R.id.lv_item_subtext);
                adapterView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder)convertView.getTag();
            }


            final String city = list.get(position).getCity();
            final String borough = list.get(position).getBorough();

            viewHolder.city.setText(city);
            viewHolder.borough.setText(borough);


            return adapterView;
        }

        public class ViewHolder {
            TextView city;
            TextView borough;

        }

        private final Context mContext;
    }


}
