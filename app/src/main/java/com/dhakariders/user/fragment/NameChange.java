package com.dhakariders.user.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.dhakariders.R;
import com.dhakariders.user.activity.Home;
import com.dhakariders.user.activity.OrderDetails;
import com.dhakariders.user.activity.Settings;
import com.dhakariders.user.utils.SharedPref;
import com.softwaremobility.network.Connection;
import com.softwaremobility.simplehttp.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rezwan on 4/24/16.
 */
public class NameChange extends android.support.v4.app.DialogFragment {

    private EditText nameChangeET;
    private final static String baseURL2 = SharedPref.BASE_URL + "session";
    private final static String TAG = "NameChange";
    private ProgressDialog pd;
    public static Settings settings;

    public NameChange(){

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
        pd = new ProgressDialog(getContext(), R.style.Theme_MyDialog);
        pd.setMessage("REQUESTING");
        pd.setCancelable(false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.content_name_change, container, false);
        intView(rootView);
        rootView.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NameChange.this.dismiss();
            }
        });
        rootView.findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NameChange.this.dismiss();
                pd.show();
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                final String newName  =  nameChangeET.getText().toString();
                if(newName.length() > 0 && !newName.equals(SharedPref.getUserName(getContext()))) {
                    NetworkConnection.testPath(baseURL2);
                    NetworkConnection.productionPath(baseURL2);
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "5");
                    params.put("session_id", SharedPref.getSessionId(getContext()));
                    params.put("name", newName);

                    NetworkConnection.with(getContext()).withListener(new NetworkConnection.ResponseListener() {
                        @Override
                        public void onSuccessfullyResponse(String response) {
                            Log.w(TAG, "SERVER MESSAGE" + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.optBoolean("success")) {
                                    SharedPref.setUserName(getContext(), newName);
                                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        settings.updateField();
                                    }
                                });
                                pd.dismiss();
                                NameChange.this.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onErrorResponse(String error, String message, int code) {
                            Log.w(TAG, error + "  " + message + " " + code);
                            pd.dismiss();

                        }
                    }).doRequest(Connection.REQUEST.GET, Uri.parse(""), params, null, null);
                }else{

                    if(newName.length() < 1){
                        Toast.makeText(getContext(), "Name Cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    if(newName.equals(SharedPref.getUserName(getContext()))){
                        Toast.makeText(getContext(), "Name is same as previous name", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return rootView;
    }

    private void intView(View rootView) {
        nameChangeET = (EditText)rootView.findViewById(R.id.nameChangeET);
        nameChangeET.setHint(SharedPref.getUserName(rootView.getContext()));
    }
    

}
