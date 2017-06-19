package com.dhakariders.customer.activity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dhakariders.R;
import com.dhakariders.customer.fragment.HomeFragment;
import com.dhakariders.customer.fragment.Order;
import com.dhakariders.customer.utils.SharedPref;
import com.softwaremobility.network.Connection;
import com.softwaremobility.simplehttp.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {

    private final static String baseURL = SharedPref.BASE_URL+"pulls";
    private final static String TAG = "OrderDetails";

    private boolean isActive;
    private boolean shouldUpdateDistance = true;
    private Handler pullRunner;
    private HomeFragment homeFragment;
    private TextView driverStatus;

    private String distance;
    private double curr_lat;
    private double curr_lng;
    private String bill;
    private int driver_status;

    private final static String DISTANCE = "dist";
    private final static String CURR_LAT = "curr_lat";
    private final static String CURR_LON = "curr_lon";
    private final static String BILL = "bill";
    private final static String DRIVER_STATUS = "driver_status";
    private View paidBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.foreground_color));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Order Details");
       /* PulsatorLayout pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsator.start();*/
        TextView textView  = (TextView) findViewById(R.id.driverDetails);
        textView.setText(SharedPref.getDriverDetails(this));

        homeFragment =  (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.orderMap);
        driverStatus = (TextView) findViewById(R.id.driverStatus);
        driverStatus.setText("Status - Synchronizing");
        paidBtn  = findViewById(R.id.paidBtn);
        paidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.setHasAnActiveOrder(v.getContext(), false);
                Intent intent  = new Intent(v.getContext(), Home.class);
                v.getContext().startActivity(intent);
                OrderDetails.this.finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        if(pullRunner == null){
            pullRunner = new Handler();
        }
        pullRunner.postDelayed(runnable, 10000);
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(isActive){

                        NetworkConnection.testPath(baseURL);
                        NetworkConnection.productionPath(baseURL);
                        Map<String, String> params = new HashMap<>();
                        params.put("action", "1");
                        params.put("session_id", SharedPref.getSessionId(OrderDetails.this));
                        params.put("ord_id", SharedPref.getOrderID(OrderDetails.this));

                        NetworkConnection.with(OrderDetails.this).withListener(new NetworkConnection.ResponseListener() {
                            @Override
                            public void onSuccessfullyResponse(String response) {
                                Log.w(TAG, "SERVER MESSAGE" + response);
                                try {
                                    JSONObject jsonObject  =  new JSONObject(response);

                                    distance  = jsonObject.optString(DISTANCE, "0");
                                    curr_lat  = jsonObject.optDouble(CURR_LAT, 0);
                                    curr_lng  = jsonObject.optDouble(CURR_LON, 0);
                                    bill  = jsonObject.optString(BILL, "0");
                                    driver_status  = jsonObject.optInt(DRIVER_STATUS, 0);
                                    if (driver_status == 1) {
                                        driverStatus.setText("Status - Driver Coming");
                                    }
                                    else if (driver_status ==2  && shouldUpdateDistance) {
                                        driverStatus.setText("Status - Ride Started: " + distance + " Bill - "+bill);
                                    }
                                    else if (driver_status == 3){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                               if(shouldUpdateDistance){
                                                   shouldUpdateDistance = false;
                                                   paidBtn.setVisibility(View.VISIBLE);
                                               }
                                            }
                                        });
                                    }
                                    else {
                                        driverStatus.setText("Status - Synchronizing");
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Location mLocation = new Location("");
                                            mLocation.setLatitude(curr_lat);
                                            mLocation.setLongitude(curr_lng);
                                            homeFragment.changeMap(mLocation);
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onErrorResponse(String error, String message, int code) {
                                Log.w(TAG, error + "  " + message + " " + code);

                            }
                        }).doRequest(Connection.REQUEST.GET, Uri.parse(""), params, null, null);

                        pullRunner.postDelayed(this, 10000);
                    }
                }
            });
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
        if(pullRunner != null){
            pullRunner.removeCallbacksAndMessages(null);
        }
    }

}
