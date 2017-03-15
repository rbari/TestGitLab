package com.dhakariders.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.dhakariders.R;

public class Splash extends AppCompatActivity {

    private TextView splashTextView;
    private View splashImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/splash_typeface.ttf");
        splashTextView = (TextView) findViewById(R.id.splashTextView);
        splashTextView.setTypeface(myTypeface);
        splashImageView = findViewById(R.id.splashImageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        show();
    }

    public void show() {
        AnimatorSet animSetXY = new AnimatorSet();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        ObjectAnimator anim = ObjectAnimator.ofFloat(splashImageView, "translationX", -screenWidth, 0);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(splashTextView, "translationX", screenWidth, 0);
        animSetXY.playTogether(anim,anim1);
        animSetXY.setDuration(1000);
        animSetXY.setInterpolator( new AccelerateInterpolator(2f));
        animSetXY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Splash.this, LoginActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();

                    }
                }, 200);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        splashImageView.setVisibility(View.VISIBLE);
        splashTextView.setVisibility(View.VISIBLE);
        animSetXY.start();
    }


}
