package com.dhakariders.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dhakariders.R;
import com.dhakariders.utils.BlurView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private View logInCardView, signUpCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        ((BlurView)findViewById(R.id.blurView)).setBlurredView(findViewById(R.id.backGround));
        logInCardView  = findViewById(R.id.logInCardView);
        signUpCardView = findViewById(R.id.signUpCardView);
        findViewById(R.id.createAccountButton).setOnClickListener(this);
        findViewById(R.id.cancelSignUp).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createAccountButton:{
                logInCardView.setVisibility(View.GONE);
                signUpCardView.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.cancelSignUp:{
                signUpCardView.setVisibility(View.GONE);
                logInCardView.setVisibility(View.VISIBLE);
                break;
            }
        }
    }
}
