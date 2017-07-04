package com.dhakariders.customer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhakariders.R;
import com.dhakariders.customer.fragment.FreeRides;
import com.dhakariders.customer.fragment.Promotions;
import com.dhakariders.customer.utils.SharedPref;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView preview;
    private int[] images = {R.drawable.preview_01, R.drawable.preview_02, R.drawable.preview_03, R.drawable.preview_04, R.drawable.preview_05};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.foreground_color));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dhaka Riders");



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navigationView.getMenu();

        MenuItem tools= menu.findItem(R.id.Communicate);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.NavMenuTitle), 0, s.length(), 0);
        tools.setTitle(s);

        navigationView.setNavigationItemSelectedListener(this);
        findViewById(R.id.hireRide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent(Home.this, PickUpAndDropOff.class);
                startActivity(intent);
            }
        });
        preview = (ImageView)findViewById(R.id.previewImage);
        findViewById(R.id.homePromoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPromoFragment();
            }
        });
        findViewById(R.id.homeUserProBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent(Home.this, Settings.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.homeSupportBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent(v.getContext(), Support.class);
                startActivity(intent);
            }
        });


        View headerLayout = navigationView.getHeaderView(0);

        ((TextView)headerLayout.findViewById(R.id.drawerUserName)).setText(SharedPref.getUserName(this));
        ((TextView)headerLayout.findViewById(R.id.drawerUserNumber)).setText(SharedPref.getUserPhoneNumber(this));
    }

    Handler pictureChanger;
    boolean isActive;
    int index = 0;

    @Override
    protected void onResume() {
        super.onResume();
        if(SharedPref.isOrderActive(this)){
            Intent intent =  new Intent(this, OrderDetails.class);
            startActivity(intent);
            finish();
            return;
        }
        isActive = true;
        if(pictureChanger == null){
            pictureChanger = new Handler();
        }
        pictureChanger.postDelayed(runnable, 5000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(isActive && preview != null){
                        index++;
                        if(index == 5){
                            index = 0;
                        }
                        preview.setImageResource(images[index]);
                        pictureChanger.postDelayed(this, 5000);
                    }
                }
            });
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
        if(pictureChanger != null){
            pictureChanger.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            Intent intent  =  new Intent(this, Notification.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_promo) {
           showPromoFragment();
        } else if (id == R.id.nav_free_ride) {
            showFreeRideFragment();
        } else if (id == R.id.nav_history) {
            Intent intent  =  new Intent(this, History.class);
            startActivity(intent);
        } else if (id == R.id.nav_support) {
            Intent intent  =  new Intent(this, Support.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent  =  new Intent(this, About.class);
            startActivity(intent);
        } else if(id == R.id.nav_logOut){
            SharedPref.setIsLoggedIn(this, false);
            Intent intent = new Intent(this, LoginSignUpActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFreeRideFragment() {
        FreeRides freeRides = new FreeRides();
        freeRides.show(getSupportFragmentManager(),"Promotions");
    }

    private void showPromoFragment() {
        Promotions promotions = new Promotions();
        promotions.show(getSupportFragmentManager(),"Promotions");
    }
}
