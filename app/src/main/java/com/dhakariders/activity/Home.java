package com.dhakariders.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dhakariders.R;

public class Home extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ((TextView) findViewById(R.id.splashTextView))
                .setTypeface(Typeface.createFromAsset(getAssets(), "fonts/splash_typeface.ttf"));
        findViewById(R.id.hire).setOnClickListener(this);
        findViewById(R.id.settings).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hire:{
                Intent intent  =  new Intent(this, PickUpAndDropOff.class);
                startActivity(intent);
                break;
            }
            case R.id.settings:{
                Intent intent  =  new Intent(this, Settings.class);
                startActivity(intent);
                break;
            }
        }
    }
}
