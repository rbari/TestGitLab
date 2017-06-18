package com.dhakariders.customer.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhakariders.R;
import com.dhakariders.customer.activity.PickUpAndDropOff;
import com.dhakariders.customer.utils.SharedPref;
import com.softwaremobility.network.Connection;
import com.softwaremobility.simplehttp.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rezwan on 4/24/16.
 */
public class Order extends android.support.v4.app.DialogFragment {


    private static final String TAG = "Order";
    public static JSONObject json;
    private final static String baseURL = SharedPref.BASE_URL+"order";
    //private final static String baseURL = "http://192.168.21.101:9000/api/v1/order";

    private String orderID;
    private String msg;

    public Order(){

    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.content_confirm, container, false);
        intView(rootView);
        rootView.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkConnection.testPath(baseURL);
                NetworkConnection.productionPath(baseURL);
                Map<String, String> params = new HashMap<>();
                params.put("action", "4");
                params.put("session_id", SharedPref.getSessionId(getContext()));
                params.put("ord_id", orderID);

                NetworkConnection.with(getContext()).withListener(new NetworkConnection.ResponseListener() {
                    @Override
                    public void onSuccessfullyResponse(String response) {
                        Order.this.dismiss();
                    }

                    @Override
                    public void onErrorResponse(String error, String message, int code) {
                        Log.w(TAG, error + "  " + message + " " + code);
                        Order.this.dismiss();
                    }
                }).doRequest(Connection.REQUEST.GET, Uri.parse(""), params, null, null);
                //Order.this.dismiss();
            }
        });
        rootView.findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Order.this.dismiss();
                NetworkConnection.testPath(baseURL);
                NetworkConnection.productionPath(baseURL);
                Map<String, String> params = new HashMap<>();
                params.put("action", "1");
                params.put("session_id", SharedPref.getSessionId(getContext()));
                params.put("ord_id", orderID);

                NetworkConnection.with(getContext()).withListener(new NetworkConnection.ResponseListener() {
                    @Override
                    public void onSuccessfullyResponse(String response) {

                        SharedPref.setHasAnActiveOrder(getContext(), true);
                        SharedPref.setOrderID(getContext(), orderID);
                        SharedPref.setDriverDetails(getContext(), msg);
                        getActivity().finish();
                        Order.this.dismiss();
                    }

                    @Override
                    public void onErrorResponse(String error, String message, int code) {
                        Log.w(TAG, error + "  " + message + " " + code);
                        Order.this.dismiss();
                    }
                }).doRequest(Connection.REQUEST.GET, Uri.parse(""), params, null, null);

            }
        });
        return rootView;
    }

    private void intView(View rootView) {
        try {
            orderID = json.getString("order_id");
            String driverName = json.getString("driver_name");
            String driverPhone = json.getString("driver_phone");
            String driverCar = json.getString("driver_car");
            TextView textView4 = (TextView) rootView.findViewById(R.id.textView4);
            msg = "Driver Name - "+driverName+"\nPhone No. - "+driverPhone+"\nSerial - "+driverCar;
            textView4.setText(msg);
        }catch (Exception ignored){

        }
    }

}
