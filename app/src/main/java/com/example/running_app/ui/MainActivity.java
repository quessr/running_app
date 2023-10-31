package com.example.running_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.example.running_app.BuildConfig;
import com.example.running_app.R;
import com.example.running_app.data.model.GpsTrackerService;
import com.example.running_app.databinding.ActivityMainBinding;
import com.example.running_app.ui.fragments.RunFragment;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    RunFragment runFragment = new RunFragment();
    public GpsTrackerService gpsTracker;

    private static final int NOTIFICATION_ID = 0;
    private NotificationManager notificationManager = null;
    String channelId = "gps_tacker_channel";

    private ActivityMainBinding binding;

    GpsTrackerService.updateMap listener = new GpsTrackerService.updateMap(){

        @Override
        public void updateMap(Location location) {
            Log.d("HSR", "MainActivity.updateMap : "+location);
            runFragment.updateMap(location);
        }
    };

    ServiceConnection serviceGpsTrackerConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            gpsTracker.setListener(null);
            gpsTracker = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("HSR", "onServiceConnected()");
            if(service != null){
                GpsTrackerService.LocalBinder mGpsTrackerServiceBinder = (GpsTrackerService.LocalBinder)service;
                gpsTracker = mGpsTrackerServiceBinder.getService();
                gpsTracker.startForeground();
                gpsTracker.setListener(listener);
//                gpsTracker.startLocationUpdate();

            }
        }
    };

    public Location getGpsTrackerLocation(){
        if (gpsTracker != null) {
            return gpsTracker.getLocation();
        } else {
            return null; // 또는 다른 적절한 값
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        fragmentTransaction.add(R.id.run_fragment_container, runFragment);
        fragmentTransaction.commit();

        Intent gpsTrackerService = new Intent(getApplicationContext(), GpsTrackerService.class);
        bindService(gpsTrackerService, serviceGpsTrackerConnection, Context.BIND_AUTO_CREATE);

//        Intent serviceIntent = new Intent(this, GpsTracker.class);
//        ContextCompat.startForegroundService(this, serviceIntent);


        Log.d("HSR", "" + BuildConfig.GOOGLE_MAP_API_KEY );

        binding.runStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RunStartCountdownActivity.class);
                startActivity(intent);

                gpsTracker.startLocationUpdate();


            }
        });

    }
}