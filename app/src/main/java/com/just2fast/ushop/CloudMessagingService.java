package com.just2fast.ushop;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CloudMessagingService extends FirebaseMessagingService {
    String CHANNEL_ID;
    int NOTIFICATION_ID = 100;
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d("titleBody",message.getNotification().getTitle()+"");
        pushNotification(message.getNotification().getTitle(),message.getNotification().getBody(),message.getNotification().getImageUrl());
    }

    private void pushNotification(String Title, String Body,Uri ImageUri) {
        Bitmap bitmap;
        Intent iNotify = new Intent(this,search_instance.class);
        iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this,100,iNotify,PendingIntent.FLAG_IMMUTABLE);
//        try {
//            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), ImageUri);
//        } catch (IOException e) {
//            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.searchimg);
//            e.printStackTrace();
//
//        }
        bitmap = getBitmapFromUrl(ImageUri.toString());

        Notification notification;
        CHANNEL_ID = "100";
        NotificationManager nm =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification  = new Notification.Builder(this)
                    .setContentTitle(Title)
                    .setLargeIcon(bitmap)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.searchimg)
                    .setSubText(Body)
                    .setContentIntent(pi)
                    .setChannelId(CHANNEL_ID).build();
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"SALE",NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(notificationChannel);
        }
        else {
            notification  = new Notification.Builder(this)
                    .setContentTitle(Title)
                    .setLargeIcon(bitmap)
                    .setSubText(Body)
                    .setContentIntent(pi)
                    .setSmallIcon(R.drawable.searchimg)
                    .setAutoCancel(true)
                    .build();
        }
        nm.notify(NOTIFICATION_ID,notification);

    }

    private Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}