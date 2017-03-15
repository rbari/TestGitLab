package com.dhakariders.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dhakariders.R;
import com.dhakariders.utils.BlurView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        ((BlurView)findViewById(R.id.blurView)).setBlurredView(findViewById(R.id.backGround));
    }
}
