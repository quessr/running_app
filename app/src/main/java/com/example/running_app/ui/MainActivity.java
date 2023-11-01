package com.example.running_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.running_app.BuildConfig;
import com.example.running_app.R;
import com.example.running_app.data.model.GpsTrackerService;
import com.example.running_app.ui.fragments.RunFragment;
import com.example.running_app.ui.fragments.RunHistoryFragment;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager = getSupportFragmentManager();
//    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//    RunFragment runFragment = new RunFragment();
//    public GpsTrackerService gpsTracker;
//
//    private static final int NOTIFICATION_ID = 0;
//    private NotificationManager notificationManager = null;
//    String channelId = "gps_tacker_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new RunHistoryFragment()).commit();
//<<<<<<< HEAD
//        fragmentTransaction.add(R.id.run_fragment_container, runFragment);
//        fragmentTransaction.commit();
//=======
    }
//>>>>>>> feature/adapter
//    GpsTrackerService.updateMap listener = new GpsTrackerService.updateMap(){
//
//        @Override
//        public void updateMap(Location location) {
//            Log.d("HSR", "MainActivity.updateMap : "+location);
//            runFragment.updateMap(location);
//        }
//    };
//    ServiceConnection serviceGpsTrackerConnection = new ServiceConnection() {
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            gpsTracker.setListener(null);
//            gpsTracker = null;
//        }
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            Log.d("HSR", "onServiceConnected()");
//            if(service != null){
//                GpsTrackerService.LocalBinder mGpsTrackerServiceBinder = (GpsTrackerService.LocalBinder)service;
//                gpsTracker = mGpsTrackerServiceBinder.getService();
//                gpsTracker.startForeground();
//                gpsTracker.setListener(listener);
//            }
//        }
//    };

//    public Location getGpsTrackerLocation(){
//        return gpsTracker.getLocation();
//    }
//import com.example.running_app.ui.fragments.RunHistoryFragment;
//
//public class MainActivity extends AppCompatActivity {
//    FragmentManager fragmentManager = getSupportFragmentManager();
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////
////<<<<<<< HEAD
////        fragmentTransaction.add(R.id.run_fragment_container, runFragment);
////        fragmentTransaction.commit();
////=======
////        FragmentManager fragmentManager = getSupportFragmentManager();
////        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////        fragmentTransaction.replace(R.id.fragment_container, new RunHistoryFragment()).commit();
////    }
////>>>>>>> feature/adapter
//
//        Intent gpsTrackerService = new Intent(getApplicationContext(), GpsTrackerService.class);
//        bindService(gpsTrackerService, serviceGpsTrackerConnection, Context.BIND_AUTO_CREATE);
////        Intent serviceIntent = new Intent(this, GpsTracker.class);
////        ContextCompat.startForegroundService(this, serviceIntent);
//
//
//        Log.d("HSR", "" + BuildConfig.GOOGLE_MAP_API_KEY );
//    }
}