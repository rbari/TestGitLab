package com.dhakariders.user.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.dhakariders.R;
import com.dhakariders.user.activity.Settings;
import com.dhakariders.user.utils.SharedPref;
import com.softwaremobility.network.Connection;
import com.softwaremobility.simplehttp.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rezwan on 4/24/16.
 */
public class PasswordChange extends android.support.v4.app.DialogFragment {

    private final static String baseURL2 = SharedPref.BASE_URL + "session";
    private final static String TAG = "PasswordChange";
    private EditText currentPasswordET;
    private EditText newPasswordET_1;
    private EditText newPasswordET_2;
    public static Settings settings;
    private ProgressDialog pd;

    public PasswordChange(){

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
        final View rootView = inflater.inflate(R.layout.content_password_change, container, false);
        intView(rootView);
        rootView.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordChange.this.dismiss();
            }
        });
        rootView.findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PasswordChange.this.dismiss();

                String currentPassword  =  currentPasswordET.getText().toString();
                final String password_0 =  newPasswordET_1.getText().toString();
                String password_1  =  newPasswordET_2.getText().toString();
                if(!currentPassword.equals(SharedPref.getPassword(getContext()))){
                    Toast.makeText(getContext(), "Current password didn't match - "+SharedPref.getPassword(getContext()), Toast.LENGTH_LONG).show();
                    return;
                }
                if(password_0.length() < 4 || password_1.length() < 4){
                    Toast.makeText(getContext(), "Password cannot be less the 4 character", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!password_0.equals(password_1)){
                    Toast.makeText(getContext(), "Both password did not match", Toast.LENGTH_LONG).show();
                    return;
                }
                pd.show();

                NetworkConnection.testPath(baseURL2);
                NetworkConnection.productionPath(baseURL2);
                Map<String, String> params = new HashMap<>();
                params.put("action", "3");
                params.put("session_id", SharedPref.getSessionId(getContext()));
                params.put("password", password_0);

                NetworkConnection.with(getContext()).withListener(new NetworkConnection.ResponseListener() {
                    @Override
                    public void onSuccessfullyResponse(String response) {
                        Log.w(TAG, "SERVER MESSAGE" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.optBoolean("success")) {
                                SharedPref.setPassword(getContext(), password_0);
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                            pd.dismiss();
                            PasswordChange.this.dismiss();
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
            }
        });
        return rootView;
    }

    private void intView(View rootView) {
        currentPasswordET = (EditText)rootView.findViewById(R.id.currentPasswordET);
        newPasswordET_1 = (EditText)rootView.findViewById(R.id.newPasswordET_1);
        newPasswordET_2 = (EditText)rootView.findViewById(R.id.newPasswordET_2);
    }

}
