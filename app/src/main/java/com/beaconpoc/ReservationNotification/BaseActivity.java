package com.beaconpoc.ReservationNotification;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beaconpoc.ReservationNotification.constant.AppBarType;
import com.beaconpoc.ReservationNotification.constant.ReservationNotificationConstants;

/**
 * Created by Aparupa on 11/17/2016.
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    AppBarType appBarType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);

        int navMenuRes = R.menu.nav_menu;
        navView.inflateMenu(navMenuRes);
        navView.setNavigationItemSelectedListener(this);

        ViewGroup frame = (ViewGroup) findViewById(R.id.frame);
        try {
            View contentView = getLayoutInflater().inflate(layoutResID, frame, false);
            if(contentView.findViewById(R.id.toolbar) != null) {
                LinearLayout toolbarParent = (LinearLayout)findViewById(R.id.toolbar).getParent();
                toolbarParent.removeView(findViewById(R.id.toolbar));
            }
            frame.addView(contentView);
        } catch (InflateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //navigation icon selected
                if (appBarType == AppBarType.NAV_MENU) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    onBackPressed();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()){

            case R.id.home :

                intent = new Intent(this, MainActivity.class);
                intent.putExtra(ReservationNotificationConstants.FCM_FLOW, true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                break;

            case R.id.poi :

                intent = new Intent(this, POIActivity.class);
                startActivity(intent);
                finish();

                break;

            case R.id.about :

                break;

            case R.id.settings :

                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public void setupAppBar(@Nullable String title, @Nullable AppBarType appBarMode) {
        appBarType = appBarMode;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.hamburger_menu);
        toolbar.setTitleTextAppearance(this, R.style.toolbarTitleAppearance);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (appBarType == AppBarType.BACK_ARROW) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
            if (upArrow != null) {
//                upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                actionBar.setHomeAsUpIndicator(upArrow);
            }
        } else if (appBarType == AppBarType.NONE) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        if (!TextUtils.isEmpty(title)) {
            actionBar.setTitle(title);
        } else {
            actionBar.setDisplayShowTitleEnabled(false);
//            actionBar.setLogo(ContextCompat.getDrawable(this, R.mipmap.search));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
