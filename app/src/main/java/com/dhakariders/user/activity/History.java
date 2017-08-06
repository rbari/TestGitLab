package com.dhakariders.user.activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dhakariders.R;
import com.dhakariders.user.fragment.NameChange;
import com.dhakariders.user.utils.HistoryAdapter;
import com.dhakariders.user.utils.HistoryDTO;
import com.dhakariders.user.utils.SharedPref;
import com.softwaremobility.network.Connection;
import com.softwaremobility.simplehttp.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class History extends AppCompatActivity {

    private final static String baseURL2 = SharedPref.BASE_URL + "session";
    private final static String TAG = "History";
    private RecyclerView recyclerView;
    private HistoryAdapter recyclerViewAdapter;
    private LinkedHashMap <String, HistoryDTO> historyDTOs;
    private ProgressDialog pd;
    private View noHistoryTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.foreground_color));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("History");
        pd = new ProgressDialog(this, R.style.Theme_MyDialog);
        pd.setMessage("REQUESTING DATA");
        pd.setCancelable(false);
        setUpRecycleView();
    }

    private void setUpRecycleView() {
        historyDTOs = new LinkedHashMap();
        recyclerView = (RecyclerView) findViewById(R.id.historyRV);
        LinearLayoutManager recylerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recylerViewLayoutManager);
        recyclerViewAdapter = new HistoryAdapter(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                recylerViewLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.history_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(recyclerViewAdapter);
        noHistoryTV  = findViewById(R.id.noHistoryTV);
        recyclerViewAdapter.setListener(new HistoryAdapter.RequestMoreData() {
            @Override
            public void request(String index) {
                Toast.makeText(History.this, "Loading more", Toast.LENGTH_SHORT).show();
                makeServerRequest(index);
            }
        });
        pd.show();
        makeServerRequest("0");
    }

    private void makeServerRequest(String index) {
        NetworkConnection.testPath(baseURL2);
        NetworkConnection.productionPath(baseURL2);
        Map<String, String> params = new HashMap<>();
        params.put("action", "6");
        params.put("session_id", SharedPref.getSessionId(this));
        params.put("index", ""+index);

        NetworkConnection.with(this).withListener(new NetworkConnection.ResponseListener() {
            @Override
            public void onSuccessfullyResponse(String response) {
                Log.w(TAG, "SERVER MESSAGE" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        addHistory(jsonObject.getJSONArray("data"));
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
    }

    private void addHistory(JSONArray jsonArray){
        final LinkedHashMap <String, HistoryDTO> temp  =  new LinkedHashMap<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                HistoryDTO  historyDTO  =  new HistoryDTO();
                historyDTO.orderId  =  jsonObject.optString("ord_id", "error");
                historyDTO.driverName  =  jsonObject.optString("driver_name", "error");
                historyDTO.driverNumber  =  jsonObject.optString("driver_phone", "error");
                historyDTO.distance  =  jsonObject.optString("dist", "error");
                historyDTO.bill  =  jsonObject.optString("bill", "error");
                temp.put(historyDTO.orderId, historyDTO);
            }
        }catch (Exception ignored){

        }
        temp.keySet().removeAll(historyDTOs.keySet());

            historyDTOs.putAll(temp);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (temp.size() > 0) {
                        recyclerViewAdapter.addHistoryDTOs(temp.values());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                    pd.dismiss();
                    if(recyclerViewAdapter.getItemCount() > 0  && noHistoryTV.getVisibility() == View.VISIBLE){
                        noHistoryTV.setVisibility(View.GONE);
                    }
                }
            });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
