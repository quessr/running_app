package com.example.running_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

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
import android.widget.TextView;

import com.example.running_app.BuildConfig;
import com.example.running_app.R;
import com.example.running_app.data.model.GpsTrackerService;
import com.example.running_app.data.model.StepCounter;
import com.example.running_app.databinding.ActivityMainBinding;
import com.example.running_app.ui.fragments.RunFragment;
import com.example.running_app.ui.fragments.RunHistoryFragment;
import com.example.running_app.data.model.GpsTrackerService;
import com.example.running_app.ui.viewmodels.RunViewModel;
import com.example.running_app.ui.viewmodels.TimerViewModel;

public class MainActivity extends AppCompatActivity {
    public static Context mContext;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    RunFragment runFragment = new RunFragment();
    RunHistoryFragment runHistoryFragment = new RunHistoryFragment();
    public GpsTrackerService gpsTracker;

    private static final int NOTIFICATION_ID = 0;
    private NotificationManager notificationManager = null;
    String channelId = "gps_tacker_channel";

    public ActivityMainBinding binding;
    public boolean isStartButtonVisible = true;
    public boolean isEndButtonVisible = false;

    //timer, room DB
    private TimerViewModel timerViewModel;
    private RunViewModel runViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //timer - viewModel
        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);
        runViewModel = new ViewModelProvider(this).get(RunViewModel.class);
        timerViewModel.setRunViewModel(runViewModel);

        mContext = this;

//        binding.runEndBtn.setVisibility(isEndButtonVisible ? View.VISIBLE : View.GONE);
//        binding.runStartBtn.setVisibility(isStartButtonVisible ? View.VISIBLE : View.GONE);
        binding.runStartBtn.setVisibility(View.VISIBLE);
        binding.runEndBtn.setVisibility(View.GONE);
        binding.showRecordBtn.setVisibility(View.VISIBLE);
        binding.stepcountTimerContainer.setVisibility(View.GONE);

        fragmentTransaction.add(R.id.run_fragment_container, runFragment);
        fragmentTransaction.commit();


        Intent gpsTrackerService = new Intent(getApplicationContext(), GpsTrackerService.class);
        bindService(gpsTrackerService, serviceGpsTrackerConnection, Context.BIND_AUTO_CREATE);

        StepCounter stepCounter = new StepCounter(this);

//        Intent serviceIntent = new Intent(this, GpsTracker.class);
//        ContextCompat.startForegroundService(this, serviceIntent);

        Log.d("HSR", "" + BuildConfig.GOOGLE_MAP_API_KEY );

        binding.runStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RunStartCountdownActivity.class);
                startActivity(intent);

                int savedStepCount = stepCounter.loadStepCount(mContext); // 저장된 걸음수를 불러옴
//                int currentStepCount = stepCounter.getStepCount(); // 현재 측정된 걸음수
//                int stepCountDifference = currentStepCount - savedStepCount;

                gpsTracker.startLocationUpdate();
                stepCounter.start();

                binding.runStartBtn.setVisibility(View.GONE);
                binding.runEndBtn.setVisibility(View.VISIBLE);
                binding.showRecordBtn.setVisibility(View.GONE);
                binding.stepcountTimerContainer.setVisibility(View.VISIBLE);

                //timer
                timerViewModel.startTimer();
                stepCounter.setStepCountListener(new StepCounter.StepCountListener() {
                    @Override
                    public void onStepCountChanged(int stepCount) {
                        binding.tvStepCount.setText(String.valueOf(stepCounter.getStepCount() - savedStepCount));
                    }
                });
            }
        });

        //timer 관찰
        timerViewModel.getTimeTextLiveData().observe(this, s -> binding.tvTime.setText(s));

        binding.runEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsTracker.stopUsingGPS();
                gpsTracker.stopService(new Intent(MainActivity.this,GpsTrackerService.class));
                gpsTracker.stopNotification();
                stepCounter.stop();

                binding.stepcountTimerContainer.setVisibility(View.GONE);
                binding.runEndBtn.setVisibility(View.GONE);

                //timer
                timerViewModel.stopTimer();

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.run_history, runHistoryFragment);
                transaction.commit();
            }
        });

        binding.showRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.run_history, runHistoryFragment);
                transaction.commit();

                binding.runStartBtn.setVisibility(View.GONE);
                binding.showRecordBtn.setVisibility(View.GONE);
            }
        });
    }

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
            if(service != null){
                GpsTrackerService.LocalBinder mGpsTrackerServiceBinder = (GpsTrackerService.LocalBinder)service;
                gpsTracker = mGpsTrackerServiceBinder.getService();
                gpsTracker.startForeground();
                gpsTracker.setListener(listener);

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
}