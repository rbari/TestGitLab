package com.dhakariders.activity;

import android.animation.AnimatorSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;

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
                showSignUp();
                break;
            }
            case R.id.cancelSignUp:{
                closeSignUp();
                break;
            }
        }
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

    private void closeSignUp(){
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
