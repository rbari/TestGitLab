package com.dhakariders.user.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dhakariders.R;
import com.dhakariders.user.fragment.NameChange;
import com.dhakariders.user.fragment.Order;
import com.dhakariders.user.fragment.PasswordChange;
import com.dhakariders.user.utils.SharedPref;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.foreground_color));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");
        updateField();
        findViewById(R.id.changeNameBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NameChange nameChange = new NameChange();
                NameChange.settings = Settings.this;
                nameChange.show(getSupportFragmentManager(),"NameChange");
            }
        });
        findViewById(R.id.changePassBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordChange passwordChange = new PasswordChange();
                passwordChange.show(getSupportFragmentManager(),"PasswordChange");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void updateField(){
        ((TextView)findViewById(R.id.settingNameTV)).setText("Name - "+SharedPref.getUserName(this));
        ((TextView)findViewById(R.id.settingPhoneTV)).setText("Phone - "+SharedPref.getUserPhoneNumber(this));
    }
}
