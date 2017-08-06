package com.dhakariders.user.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dhakariders.R;
import com.dhakariders.user.fragment.HomeFragment;
import com.dhakariders.user.fragment.Order;
import com.dhakariders.user.utils.SharedPref;
import com.softwaremobility.network.Connection;
import com.softwaremobility.simplehttp.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {

    private final static String baseURL = SharedPref.BASE_URL+"pulls";
    private final static String baseURL2 = SharedPref.BASE_URL + "order";
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
    private TextView paidBtn;
    private TextView distanceTv;
    private View detailsHolder;
    private TextView billTV;
    private View mapHolder;
    private View handShakeView;

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
        TextView textView  = (TextView) findViewById(R.id.driverName);
        textView.setText("Driver : "+SharedPref.getDriverName(this));
        TextView textView1  = (TextView) findViewById(R.id.driverNumber);
        textView1.setText("Phone : "+SharedPref.getDriverNumber(this));

        findViewById(R.id.driverNumberHolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDriver();
            }
        });

        homeFragment =  (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.orderMap);
        driverStatus = (TextView) findViewById(R.id.driverStatus);
        driverStatus.setText("Status - Synchronizing");
        detailsHolder  = findViewById(R.id.detailsHolder);
        distanceTv = (TextView) findViewById(R.id.distanceTV);
        billTV  = (TextView) findViewById(R.id.billTV);
        paidBtn  = (TextView) findViewById(R.id.paidBtn);
        paidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paidButtonPressed(OrderDetails.this);
            }
        });
        paidBtn.setVisibility(View.GONE);
        mapHolder =  findViewById(R.id.mapHolder);
        handShakeView  = findViewById(R.id.handShakeView);
    }

    private void paidButtonPressed(Context context) {
        if(driver_status == 1){
            try {
                NetworkConnection.testPath(baseURL2);
                NetworkConnection.productionPath(baseURL2);
                Map<String, String> params = new HashMap<>();
                params.put("action", "5");
                params.put("session_id", SharedPref.getSessionId(OrderDetails.this));
                params.put("ord_id", "" + SharedPref.getOrderID(OrderDetails.this));

                NetworkConnection.with(OrderDetails.this).withListener(new NetworkConnection.ResponseListener() {
                    @Override
                    public void onSuccessfullyResponse(String response) {
                        Log.w(TAG, "SERVER MESSAGE" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.optBoolean("success")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            SharedPref.setHasAnActiveOrder(getApplicationContext(), false);
                                            Intent intent = new Intent(OrderDetails.this, Home.class);
                                            OrderDetails.this.startActivity(intent);
                                            OrderDetails.this.finish();
                                        }catch (Exception ignored){

                                        }

                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(String error, String message, int code) {
                        Log.w(TAG, error + "  " + message + " " + code);

                    }
                }).doRequest(Connection.REQUEST.GET, Uri.parse(""), params, null, null);
            }catch (Exception ignored){

            }

        }else {
            SharedPref.setHasAnActiveOrder(context, false);
            Intent intent = new Intent(context, Home.class);
            context.startActivity(intent);
            OrderDetails.this.finish();
        }
    }

    private void callDriver() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    474);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+SharedPref.getDriverNumber(this))));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 474: {
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    callDriver();
                } else {
                    Toast.makeText(this, "Call Permission Not Granted", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
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
        pullRunner.postDelayed(runnable, 5000);
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
                        final Map<String, String> params = new HashMap<>();
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
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                driverStatus.setText("Status - Driver Coming");
                                                if(paidBtn.getVisibility() != View.VISIBLE){
                                                    paidBtn.setVisibility(View.VISIBLE);
                                                    paidBtn.setText("CANCEL RIDE");
                                                }
                                            }
                                        });

                                    }
                                    else if (driver_status ==2) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    if (detailsHolder.getVisibility() != View.VISIBLE) {
                                                        detailsHolder.setVisibility(View.VISIBLE);
                                                    }
                                                    billTV.setText("Bill : " + bill + "tk");
                                                    distanceTv.setText("Distance : " + distance + "km");
                                                    driverStatus.setText("Status - Ride Started");
                                                    if (paidBtn.getVisibility() == View.VISIBLE) {
                                                        paidBtn.setVisibility(View.GONE);
                                                    }
                                                }catch (Exception ignored){

                                                }
                                            }
                                        });


                                    }
                                    else if (driver_status == 3){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    if (paidBtn.getVisibility() != View.VISIBLE) {
                                                        driverStatus.setText("Status - Ride ENDED");
                                                        paidBtn.setVisibility(View.VISIBLE);
                                                        paidBtn.setText("PAID");
                                                        if (mapHolder.getVisibility() != View.GONE) {
                                                            mapHolder.setVisibility(View.GONE);
                                                            handShakeView.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                }catch (Exception ignored){

                                                }

                                            }
                                        });
                                    }
                                    else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                driverStatus.setText("Status - Synchronizing");
                                            }
                                        });
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

                        pullRunner.postDelayed(this, 5000);
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
