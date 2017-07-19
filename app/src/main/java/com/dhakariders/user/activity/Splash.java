package com.dhakariders.user.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.animation.DecelerateInterpolator;

import com.daimajia.androidanimations.library.Techniques;
import com.dhakariders.R;
import com.dhakariders.user.utils.SharedPref;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import static com.dhakariders.R.id.splashImageView;

public class Splash extends AwesomeSplash {


    private boolean appIsVisible;


    @Override
    protected void onResume() {
        super.onResume();
        appIsVisible =  true;
    }

    public void show() {
        AnimatorSet animSetXY = new AnimatorSet();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        ObjectAnimator anim = ObjectAnimator.ofFloat(splashImageView, "translationX", screenWidth, 0);
        //ObjectAnimator anim1 = ObjectAnimator.ofFloat(splashTextView, "translationX", screenWidth, 0);
       // animSetXY.playTogether(anim,anim1);
        animSetXY.playTogether(anim);
        animSetXY.setDuration(1000);
        animSetXY.setInterpolator( new DecelerateInterpolator(2f));
        animSetXY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(appIsVisible) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                          /*  try {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Date date = sdf.parse("15/6/2017");
                                if (System.currentTimeMillis() > date.getTime()) {
                                    Intent intent =  new Intent(Splash.this, Trial.class);
                                    startActivity(intent);
                                    return;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }*/
                            if(SharedPref.isLoggedIn(Splash.this)){
                                Intent intent =  new Intent(Splash.this, Home.class);
                                startActivity(intent);
                            }else{
                                Intent i = new Intent(Splash.this, LoginSignUpActivity.class);
                                startActivity(i);
                            }
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();

                        }
                    }, 200);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

      //  splashTextView.setVisibility(View.VISIBLE);
        animSetXY.start();
    }

    @Override
    protected void onPause() {
        appIsVisible = false;
        super.onPause();
    }

    @Override
    public void initSplash(ConfigSplash configSplash) {
        configSplash.setBackgroundColor(R.color.background_color); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(500); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        configSplash.setLogoSplash(R.drawable.dhaka_riders_slpash); //or any other drawable
        configSplash.setAnimLogoSplashDuration(500); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Title
        configSplash.setTitleSplash("Dhaka Riders");
        configSplash.setTitleTextColor(R.color.foreground_color);
        configSplash.setTitleTextSize(36f); //float value
        configSplash.setAnimTitleDuration(500);
        configSplash.setAnimTitleTechnique(Techniques.SlideInUp);
        //configSplash.setTitleFont("fonts/sweet_sensations.ttf"); //provide string to your font located in assets/fonts/


    }

    @Override
    public void animationsFinished() {
        if(appIsVisible) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                          /*  try {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Date date = sdf.parse("15/6/2017");
                                if (System.currentTimeMillis() > date.getTime()) {
                                    Intent intent =  new Intent(Splash.this, Trial.class);
                                    startActivity(intent);
                                    return;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }*/
                    if(SharedPref.isLoggedIn(Splash.this)){
                        Intent intent =  new Intent(Splash.this, Home.class);
                        startActivity(intent);
                    }else{
                        Intent i = new Intent(Splash.this, LoginSignUpActivity.class);
                        startActivity(i);
                    }
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();

                }
            }, 200);
        }
    }
}
