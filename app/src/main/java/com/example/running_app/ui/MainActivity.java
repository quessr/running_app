package com.example.running_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.running_app.BuildConfig;
import com.example.running_app.R;
import com.example.running_app.data.model.GpsTracker;
import com.example.running_app.ui.fragments.RunFragment;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    RunFragment runFragment = new RunFragment();
    private GpsTracker gpsTracker;

    private static final int NOTIFICATION_ID = 0;
    private NotificationManager notificationManager = null;
    String channelId = "gps_tacker_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentTransaction.add(R.id.run_fragment_container, runFragment);
        fragmentTransaction.commit();

        Intent serviceIntent = new Intent(this, GpsTracker.class);
        ContextCompat.startForegroundService(this, serviceIntent);

//        createNotification();

//        gpsTracker = new GpsTracker(this, new GpsTracker.updateMap() {
//            @Override
//            public void updateMap(Location location) {
//                    Log.d("UPDATEMAP", String.valueOf(location.getLongitude()));
//
////                Notification builder = getNotificationBuilder();
////                notificationManager.notify(NOTIFICATION_ID, builder);
//            }
//        });

        Log.d("HSR", "" + BuildConfig.GOOGLE_MAP_API_KEY );
    }

    private void createNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "GPS Tracker Channel", NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification getNotificationBuilder(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("GPS Traker")
                .setContentText("Tracking your location...")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);
        return  builder.build();
    }


}