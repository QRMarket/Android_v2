package com.qr_market.fragment.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.qr_market.Guppy;
import com.qr_market.R;
import com.qr_market.http.HttpHandler;
import com.qr_market.util.MarketUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * @author Kemal Sami KARACA
 * @since 02.2015
 * @version v1.01
 *
 * @description
 *      xxx
 *
 * @last 20.04.2015
 */
public class PaymentFragment extends Fragment implements OnItemSelectedListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    private static View view;
    private OnFragmentInteractionListener mListener;

    private Spinner SpinnerPaymentMethode,SpinnerArrivalTime;
    private Button confirmOrder;
    EditText timeText;
    StringBuilder CurrentDate;
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    String time,SelectedDate;
    private static EditText TimeSelect;


    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                  CONSTRUCTOR & ENCAPSULATION METHODS
     ***********************************************************************************************
     ***********************************************************************************************
     */
    public static PaymentFragment newInstance(String param1, String param2) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PaymentFragment() {
        // Required empty public constructor
    }

    public static View getViewPaymentFragment(){
        return view;
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

        // -0- Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_payment, container, false);

        // -1- SET ADDRESS
        TextView userAddressView = (TextView) view.findViewById(R.id.AdressOfUser);
        userAddressView.setText(MarketUser.getAddressList().get(0).getAddressString());

        timeText = (EditText)view.findViewById(R.id.timeselect);

        //Spinners
        SpinnerPaymentMethode =(Spinner) view.findViewById(R.id.PaymentSpinner);
        SpinnerArrivalTime=(Spinner) view.findViewById(R.id.ArrivalTimeSpinner);

        // display the current date (this method is below)
        CurrentDate = new StringBuilder().append(day).append("-")
                .append(month+1).append("-")
                .append(year).append("");


        //List and Array Adapter For Payment method
        List<String> list = new ArrayList<String>();
        list.add("Kredi");
        list.add("Nakit");
        list.add("Online");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerPaymentMethode.setAdapter(dataAdapter);


        //List and Array Adapter For Order Arrival Time
        List<String> ListTime=new ArrayList<String>();
        ListTime.add("Bugün");
        ListTime.add("Zaman ve tarih seç");

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,ListTime);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerArrivalTime.setAdapter(dataAdapter2);


        SpinnerPaymentMethode.setOnItemSelectedListener(this);
        SpinnerArrivalTime.setOnItemSelectedListener(this);


        TimeSelect=(EditText) view.findViewById(R.id.timeselect);
        TimeSelect.setText(new StringBuilder().append(month+1).append("-")
                .append(day).append("-")
                .append(year).append(""));
        TimeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment2 =new TimePickerFragment();
                newFragment2.show(getFragmentManager(), "timePicker");

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");

            }
        });



        confirmOrder= (Button) view.findViewById(R.id.btnGo);
        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String aid = MarketUser.getAddressList().get(0).getAid();
                String ptype = String.valueOf(SpinnerPaymentMethode.getSelectedItem());
                String date = ((EditText)view.findViewById(R.id.timeselect)).getText().toString();
                String note = ((EditText)view.findViewById(R.id.editTextNot)).getText().toString();

                String otime;
                try{

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
                    Calendar cal = Calendar.getInstance();

                    // Eğer hemen isteniyorsa şimdiki zamanı yolla
                    // Hemen değilse seçilen zamanı yolla
                    if(!date.contains("Hemen")){
                        cal.setTime(simpleDateFormat.parse(date));
                    }
                    otime = ""+cal.getTimeInMillis();


                    Map parameters = new HashMap();
                    parameters.put("cdosDo", "confirmOrderList");
                    parameters.put("aid", aid);
                    parameters.put("ptype", ptype);
                    parameters.put("date", otime);
                    parameters.put("note", note);

                    Map operationInfo = new HashMap();
                    operationInfo.put(Guppy.http_Map_OP_TYPE, HttpHandler.HTTP_OP_NORMAL);
                    operationInfo.put(Guppy.http_Map_OP_URL, Guppy.url_Servlet_Order);

                    new HttpHandler( getActivity() , "ORDERCONFIRM" ).execute( operationInfo , parameters);

                    // Bu noktada artık sipariş verilmiştir. Sonuç ise diğer fragment'e dönecektir
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame2, PaymentResultFragment.newInstance("",""), "Payment result");
                    ft.commit();


                }catch (ParseException ex){

                }

            }
        });

        Button BackBtn=(Button) view.findViewById(R.id.btnBack);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame2, BasketFragment.newInstance("",""), "NewFragmentTag");
                ft.commit();
            }
        });

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
     ***********************************************************************************************
     ***********************************************************************************************
     *                  <<< OnItemSelectedListener >>> OVERRIDE METHODS
     ***********************************************************************************************
     ***********************************************************************************************
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item=parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),"On Item Select : \n" + item.toString(), Toast.LENGTH_LONG).show();


        if(SpinnerArrivalTime.getSelectedItemPosition()==1){

            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getFragmentManager(), "datePicker");

            if(newFragment.getShowsDialog())
            {
                DialogFragment newFragment2 =new TimePickerFragment();
                newFragment2.show(getFragmentManager(), "timePicker");


            }

        }else{
            TimeSelect.setText("Hemen("+CurrentDate+")");
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }





    /**
     ***********************************************************************************************
     ***********************************************************************************************
     *                                  SUB-FRAGMENTS
     ***********************************************************************************************
     ***********************************************************************************************
     */

    //Time Picker Fragment
    @SuppressLint("ValidFragment")
    public  class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);


            // Create a new instance of DatePickerDialog and return it
            TimePickerDialog dpd=new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
            dpd.setButton(TimePickerDialog.BUTTON_POSITIVE, "Tamam", dpd);
            return dpd;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user

            time = hourOfDay+":"+minute+ ":00";

        }
    }


    //Date Picker Fragment
    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dpd=new DatePickerDialog(getActivity(), this, year, month, day);
            dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Tamam", dpd);
            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            SelectedDate=day+"-"+month+"-"+year+" ";
            TimeSelect.setText(SelectedDate + time);

        }

    }


}
