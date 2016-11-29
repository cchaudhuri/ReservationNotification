package com.beaconpoc.ReservationNotification;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beaconpoc.ReservationNotification.constant.AppBarType;
import com.beaconpoc.ReservationNotification.constant.ReservationNotificationConstants;
import com.beaconpoc.ReservationNotification.fragments.BaseFragment;
import com.beaconpoc.ReservationNotification.fragments.OfferListFragment;
import com.beaconpoc.ReservationNotification.fragments.ReservationDetailsFragment;
import com.beaconpoc.ReservationNotification.utils.LocationUpdateUtils;
import com.beaconpoc.ReservationNotification.webservice.ResponseCallBackHandler;
import com.beaconpoc.ReservationNotification.webservice.ServiceUtils;
import com.beaconpoc.ReservationNotification.webservice.model.DefaultResponse;
import com.beaconpoc.ReservationNotification.webservice.model.DeviceDetailsResponse;
import com.beaconpoc.ReservationNotification.webservice.model.EhiErrorInfo;
import com.beaconpoc.ReservationNotification.webservice.model.PushNotificationRequest;
import com.estimote.sdk.SystemRequirementsChecker;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    ProgressBar progressBar;
    private static String serviceIdentifier;
    private static String pushNotificationServiceIdentifier = "pushNotificationService";
    private static String retrieveDeviceInformationServiceIdentifier = "retrieveDeviceInformationService";
    ProgressDialog progressDialog;
    private boolean isFCMFlow;
    private boolean isBeaconFlow;

    ViewPager pager;
    ProfileInfoPagerAdapter adapter;
    private String beaconMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.design.widget.TabLayout tabs = (android.support.design.widget.TabLayout) findViewById(R.id.tabs);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setTabMode(TabLayout.MODE_FIXED);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new ProfileInfoPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setTabsFromPagerAdapter(adapter);
        tabs.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        pager.setCurrentItem(0);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        setupAppBar("Home Screen" , AppBarType.NAV_MENU);

        if (getIntent() != null) {
            isFCMFlow = getIntent().getBooleanExtra(ReservationNotificationConstants.FCM_FLOW, false);
            isBeaconFlow = getIntent().getBooleanExtra(ReservationNotificationConstants.BEACON_FLOW, false);
            beaconMessage = getIntent().getStringExtra(ReservationNotificationConstants.BEACON_PUSH_MESSAGE);
        }

        if(!isBeaconFlow && !isFCMFlow){
            Intent intent = new Intent(this, SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        if (isBeaconFlow) {
            sendPushNotificationRequest();
            isBeaconFlow = false;
        }

        if(isFCMFlow){
            ((FloatingActionButton)findViewById(R.id.getDirection)).setVisibility(View.VISIBLE);
        }else{
            ((FloatingActionButton)findViewById(R.id.getDirection)).setVisibility(View.GONE);
        }

    }

    /**
     * Custom adapter to help manage the Tabs & Fragments on the profile screen
     */
    public class ProfileInfoPagerAdapter extends FragmentPagerAdapter {
        private BaseFragment[] fragmentRef;
        private String[] titles;

        public ProfileInfoPagerAdapter(FragmentManager fm) {
            super(fm);
            titles = new String[]{
                    getString(R.string.details),
                    getString(R.string.offers)
            };
            fragmentRef = new BaseFragment[titles.length];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            if (fragmentRef != null && position >= 0 && position < fragmentRef.length) {
                fragmentRef[position] = null;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            if (fragmentRef != null && position >= 0 && position < fragmentRef.length) {
                fragmentRef[position] = (BaseFragment) fragment;
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment ret = null;
            switch (position) {
                case 0:
                    ret = ReservationDetailsFragment.newInstance(beaconMessage);
                    break;
                case 1:
                    ret = new OfferListFragment();
                    break;
            }
            return ret;
        }

        public
        @Nullable
        BaseFragment getFragmentRef(int index) {
            if (fragmentRef == null || fragmentRef.length <= index || index < 0) {
                return null;
            }
            return fragmentRef[index];
        }
    }

    private void sendPushNotificationRequest() {

        progressDialog.setMessage("Requesting Push Notification...");
//        progressDialog.show();

        ResponseCallBackHandler<DefaultResponse> callBackHandler = new ResponseCallBackHandler<DefaultResponse>() {
            @Override
            public void success(DefaultResponse response) {
                Log.d(TAG, "inside success callback");
//                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(EhiErrorInfo errorInfo) {
                progressDialog.dismiss();
                Log.d(TAG, "inside failure callback" + errorInfo.getMessage());
            }
        };

        Location location = LocationUpdateUtils.getInstance(this).getLocation();

        PushNotificationRequest request = new PushNotificationRequest.Builder()
                .deviceId("1234")
                .identifier("Car Rental")
                .latitude(location != null ? location.getLatitude() : 0)
                .longitude(location != null ? location.getLongitude() : 0)
                .memberId("1")
                .token(ReservationNotificationConstants.FCM_TOKEN).build();

        ((MyApplication) getApplication()).getEhiNotificationServiceApi().submitPushNotificationRequest(request, callBackHandler);
    }


    class ExecuteTask extends AsyncTask<String, Integer, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Sending...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            String[] paramIdentifier = params;
            serviceIdentifier = paramIdentifier[0];

            if (pushNotificationServiceIdentifier.equalsIgnoreCase(serviceIdentifier)) {
                return ServiceUtils.postPushNotificationData(ReservationNotificationConstants.FCM_TOKEN);
            } else if (retrieveDeviceInformationServiceIdentifier.equalsIgnoreCase(serviceIdentifier)) {
                return ServiceUtils.retrieveDeviceInformation();
            }

            return serviceIdentifier;

        }

        @SuppressLint("ShowToast")
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            System.out.println("Webservice Response:::::" + result);

        }

    }

    public void gotoDirections(View view){
        Intent launchPromoOffer = new Intent(MainActivity.this, PromoOfferDetailsActivity.class);
        startActivity(launchPromoOffer);
    }

    public static Intent intentMainActivity(SplashActivity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }
}
