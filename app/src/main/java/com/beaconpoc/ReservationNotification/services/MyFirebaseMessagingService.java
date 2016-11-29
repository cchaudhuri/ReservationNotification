package com.beaconpoc.ReservationNotification.services;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.beaconpoc.ReservationNotification.ArrivedVehicleDetailsActivity;
import com.beaconpoc.ReservationNotification.MainActivity;
import com.beaconpoc.ReservationNotification.MyApplication;
import com.beaconpoc.ReservationNotification.R;
import com.beaconpoc.ReservationNotification.constant.ReservationNotificationConstants;
import com.beaconpoc.ReservationNotification.webservice.model.PointOfInterestModel;
import com.beaconpoc.ReservationNotification.webservice.model.PushNotificationFcmModel;
import com.beaconpoc.ReservationNotification.webservice.model.ReservationModel;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Aparupa on 11/11/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMMsgService";

    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.d(TAG, "FCM Data Message String: " + remoteMessage.getData().toString());
        Bitmap car_largeicon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.mipmap.car_for_push);
        parseFcmJson(remoteMessage);

    }

    private void sendNotification(PushNotificationFcmModel pushNotificationFcmModel, Class activity) {

        Intent intent = new Intent(this, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ReservationNotificationConstants.FCM_FLOW, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String contentTitle = "";
        String contentText = "";

        if(activity == MainActivity.class){
            contentTitle = pushNotificationFcmModel.getReservations().get(0).getCompanyName() + " - "
                    + pushNotificationFcmModel.getReservations().get(0).getCategory();
            contentText = "Reservation Number : " + pushNotificationFcmModel.getReservations().get(0).getReservationId();
        }else{
            contentTitle = "Your vehicle has arrived";
            contentText = "Vehicle Number : " + pushNotificationFcmModel.getReservations().get(0).getVehicleNo();
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        /*.setLargeIcon(image)*/
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                        /*.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image))Notification with Image*/
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    private Bitmap getBitmapFromUrl() {
        InputStream in;
        try {
            URL url = new URL("http://api.androidhive.info/images/sample.jpg");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseFcmJson(RemoteMessage remoteMessage) {
        try {
            Gson gson = new GsonBuilder().create();
            PushNotificationFcmModel pushNotificationFcmModel = new PushNotificationFcmModel();
            pushNotificationFcmModel.setMemberId(remoteMessage.getData().get("memberId"));

            String poi = remoteMessage.getData().get("poi");
            Type poiListType = new TypeToken<List<PointOfInterestModel>>() {
            }.getType();
            pushNotificationFcmModel.setPoi((List<PointOfInterestModel>) gson.fromJson(poi, poiListType));

            String reservations = remoteMessage.getData().get("reservations");
            Type reservationsListType = new TypeToken<List<ReservationModel>>() {
            }.getType();
            pushNotificationFcmModel.setReservations((List<ReservationModel>) gson.fromJson(reservations, reservationsListType));
            ((MyApplication) getApplication()).setPushNotificationFcmModel(pushNotificationFcmModel);
            if(remoteMessage.getData().get("callingIdentity").equalsIgnoreCase("BM")){
                sendNotification(pushNotificationFcmModel, ArrivedVehicleDetailsActivity.class);
            }else{
                sendNotification(pushNotificationFcmModel, MainActivity.class);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

