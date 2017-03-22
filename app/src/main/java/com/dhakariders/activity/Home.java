package com.dhakariders.activity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dhakariders.R;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ((TextView) findViewById(R.id.splashTextView))
                .setTypeface(Typeface.createFromAsset(getAssets(), "fonts/splash_typeface.ttf"));

    }
}
