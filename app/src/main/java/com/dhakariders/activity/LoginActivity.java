package com.dhakariders.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dhakariders.R;
import com.dhakariders.utils.BlurView;
import com.dhakariders.utils.SharedPref;
import com.softwaremobility.network.Connection;
import com.softwaremobility.simplehttp.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "LoginActivity";

    private View logInCardView, signUpCardView;
    private boolean isInForgetPassword;
    private TextView logInTitle;
    private View signUpBtn, forgetPasswordToolTip;
    private TextView forgotPasswordBtn;

    private EditText loginPhoneNumberET,
            loginPasswordET,
            signUpNameET,
            signUpPhoneNumberET,
            signUpPasswordET_0,
            signUpPasswordET_1;
    private Button logInBtn;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        pd = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("LOADING");
        pd.setCancelable(false);

        ((BlurView) findViewById(R.id.blurView)).setBlurredView(findViewById(R.id.backGround));
        logInCardView = findViewById(R.id.logInCardView);
        signUpCardView = findViewById(R.id.signUpCardView);

        logInTitle = (TextView) findViewById(R.id.logInTitle);
        signUpBtn = findViewById(R.id.createAccountButton);
        forgotPasswordBtn = (TextView) findViewById(R.id.forgotPasswordButton);
        loginPhoneNumberET = (EditText) findViewById(R.id.phoneNumberET);
        loginPasswordET = (EditText) findViewById(R.id.passwordET);
        logInBtn = (Button) findViewById(R.id.logInButton);
        forgetPasswordToolTip = findViewById(R.id.forgetPasswordToolTip);

        signUpBtn.setOnClickListener(this);
        findViewById(R.id.cancelSignUp).setOnClickListener(this);
        logInBtn.setOnClickListener(this);
        forgotPasswordBtn.setOnClickListener(this);

        signUpNameET = (EditText)findViewById(R.id.signUpNameET);
        signUpPhoneNumberET = (EditText)findViewById(R.id.signUpPhoneNumberET);
        signUpPasswordET_0 = (EditText)findViewById(R.id.signUpPasswordET_0);
        signUpPasswordET_1 = (EditText)findViewById(R.id.signUpPasswordET_1);

        findViewById(R.id.signUpButton).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createAccountButton: {
                showSignUp();
                break;
            }
            case R.id.cancelSignUp: {
                closeSignUp();
                break;
            }
            case R.id.logInButton: {
                if (isInForgetPassword) {
                    recoveryTask();
                } else {
                    logInTask();
                }
                break;
            }
            case R.id.forgotPasswordButton: {
                toggleForgetPassword();
                break;
            }
            case R.id.signUpButton:{
                signUpTask();
                break;
            }
        }
    }

    private void recoveryTask() {
        String phoneNumber = loginPhoneNumberET.getText().toString();
        if (phoneNumber.length() != 11) {
            Toast.makeText(this, "Please provide a valid bangladeshi 11 digits mobile number without country code", Toast.LENGTH_LONG).show();
            return;
        }
        String baseURL = "http://ec2-52-36-4-117.us-west-2.compute.amazonaws.com/api/v1/session";
        NetworkConnection.testPath(baseURL);
        NetworkConnection.productionPath(baseURL);
        Map<String, String> params = new HashMap<>();
        params.put("action", "2");
        params.put("phone_number", phoneNumber);

        NetworkConnection.with(this).withListener(new NetworkConnection.ResponseListener() {
            @Override
            public void onSuccessfullyResponse(String response) {
                Log.wtf(TAG, response);
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                     final String message = jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setTitle("Response!")
                                        .setMessage(message)
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        });
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
        pd.show();
    }

    private void signUpTask() {
        String userName = signUpNameET.getText().toString();
        String phoneNumber = signUpPhoneNumberET.getText().toString();
        String password_0 = signUpPasswordET_0.getText().toString();
        String password_1 = signUpPasswordET_1.getText().toString();
        if(userName.length() < 1){
            Toast.makeText(this, "User name cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if(phoneNumber.length() != 11){
            Toast.makeText(this, "Please provide a valid bangladeshi 11 digits mobile number without country code", Toast.LENGTH_LONG).show();
            return;
        }
        if(password_0.length() < 4 || password_1.length() < 4){
            Toast.makeText(this, "Password cannot be less the 4 character", Toast.LENGTH_LONG).show();
            return;
        }
        if(!password_0.equals(password_1)){
            Toast.makeText(this, "Both password did not match", Toast.LENGTH_LONG).show();
            return;
        }
        String baseURL = "http://ec2-52-36-4-117.us-west-2.compute.amazonaws.com/api/v1/session";
        NetworkConnection.testPath(baseURL);
        NetworkConnection.productionPath(baseURL);
        Map<String, String> params = new HashMap<>();
        params.put("action", "0");
        params.put("name", userName);
        params.put("phone_number", phoneNumber);
        params.put("password", password_0);

        NetworkConnection.with(this).withListener(new NetworkConnection.ResponseListener() {
            @Override
            public void onSuccessfullyResponse(String response) {
                Log.wtf(TAG, response);
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        String session_id = jsonObject.getString("session_id");
                        SharedPref.setIsLoggedIn(LoginActivity.this, true);
                        SharedPref.setSessionId(LoginActivity.this, session_id);
                        Intent intent = new Intent(LoginActivity.this, Home_V2.class);
                        startActivity(intent);
                        finish();
                    } else {
                        final String/* final String message = jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setTitle("ERROR!")
                                        .setMessage(message)
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        });*/ message = jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setTitle("ERROR!")
                                        .setMessage(message)
                                        .setPositiveButton("OK", null)
                                        .show();
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
                pd.dismiss();
            }
        }).doRequest(Connection.REQUEST.GET, Uri.parse(""), params, null, null);
        pd.show();
    }

    private void logInTask() {
        String phoneNumber = loginPhoneNumberET.getText().toString();
        String password = loginPasswordET.getText().toString();
        if (phoneNumber.length() != 11) {
            Toast.makeText(this, "Please provide a valid bangladeshi 11 digits mobile number without country code", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() < 4) {
            Toast.makeText(this, "Password cannot be less the 4 character", Toast.LENGTH_LONG).show();
            return;
        }

        String baseURL = "http://ec2-52-36-4-117.us-west-2.compute.amazonaws.com/api/v1/session";
        NetworkConnection.testPath(baseURL);
        NetworkConnection.productionPath(baseURL);
        Map<String, String> params = new HashMap<>();
        params.put("action", "1");
        params.put("phone_number", phoneNumber);
        params.put("password", password);

        NetworkConnection.with(this).withListener(new NetworkConnection.ResponseListener() {
            @Override
            public void onSuccessfullyResponse(String response) {
                Log.wtf(TAG, response);
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        String session_id = jsonObject.getString("session_id");
                        SharedPref.setIsLoggedIn(LoginActivity.this, true);
                        SharedPref.setSessionId(LoginActivity.this, session_id);
                        Intent intent = new Intent(LoginActivity.this, Home_V2.class);
                        startActivity(intent);
                        finish();
                    } else {
                        final String message = jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setTitle("ERROR!")
                                        .setMessage(message)
                                        .setPositiveButton("OK", null)
                                        .show();
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
                pd.dismiss();
            }
        }).doRequest(Connection.REQUEST.GET, Uri.parse(""), params, null, null);
        pd.show();
    }

    private void toggleForgetPassword() {
        if (isInForgetPassword) {
            isInForgetPassword = false;
            hideForgetPassword();
        } else {
            isInForgetPassword = true;
            showForgetPassword();
        }
    }

    private void showForgetPassword() {
        forgetPasswordToolTip.setVisibility(View.VISIBLE);
        loginPasswordET.setVisibility(View.GONE);
        signUpBtn.setVisibility(View.GONE);
        logInTitle.setText("Recovery");
        forgotPasswordBtn.setText("Cancel");
        logInBtn.setText("SEND SMS");
    }

    private void hideForgetPassword() {
        forgetPasswordToolTip.setVisibility(View.GONE);
        loginPasswordET.setVisibility(View.VISIBLE);
        signUpBtn.setVisibility(View.VISIBLE);
        logInTitle.setText("Login");
        forgotPasswordBtn.setText(R.string.forgot_password);
        logInBtn.setText("LOGIN");
    }

    private void showSignUp() {
        Animation side_in_bottom = AnimationUtils.loadAnimation(this,
                R.anim.side_in_bottom);
        Animation side_out_top = AnimationUtils.loadAnimation(this,
                R.anim.side_out_top);
        signUpCardView.setAnimation(side_in_bottom);
        logInCardView.setAnimation(side_out_top);
        signUpCardView.setVisibility(View.VISIBLE);
        logInCardView.setVisibility(View.GONE);
    }

    private void closeSignUp() {
        Animation side_in_top = AnimationUtils.loadAnimation(this,
                R.anim.side_in_top);
        Animation side_out_bottom = AnimationUtils.loadAnimation(this,
                R.anim.side_out_bottom);
        signUpCardView.setAnimation(side_out_bottom);
        logInCardView.setAnimation(side_in_top);
        logInCardView.setVisibility(View.VISIBLE);
        signUpCardView.setVisibility(View.GONE);

    }
}
