package com.example.running_app.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.ComponentName;
import android.content.Context;

import android.content.Intent;
import android.content.ServiceConnection;

import android.location.Location;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;


import com.example.running_app.R;

import com.example.running_app.data.model.GpsTrackerService;
import com.example.running_app.data.model.StepCounter;
import com.example.running_app.databinding.ActivityMainBinding;
import com.example.running_app.ui.fragments.MainHistoryFragment;
import com.example.running_app.ui.fragments.RunFragment;
import com.example.running_app.ui.viewmodels.RunViewModel;
import com.example.running_app.ui.viewmodels.TimerViewModel;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    RunFragment runFragment = new RunFragment();
    public GpsTrackerService gpsTracker;


    public ActivityMainBinding binding;

    //timer, room DB
    private TimerViewModel timerViewModel;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //timer - viewModel
        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);
        RunViewModel runViewModel = new ViewModelProvider(this).get(RunViewModel.class);
        timerViewModel.setRunViewModel(runViewModel);

        binding.runStartBtn.setVisibility(View.VISIBLE);
        binding.runEndBtn.setVisibility(View.GONE);
        binding.showRecordBtn.setVisibility(View.VISIBLE);
        binding.stepcountTimerContainer.setVisibility(View.GONE);

        fragmentTransaction.add(R.id.run_fragment_container, runFragment);
        fragmentTransaction.commit();


        Intent gpsTrackerService = new Intent(getApplicationContext(), GpsTrackerService.class);
        bindService(gpsTrackerService, serviceGpsTrackerConnection, Context.BIND_AUTO_CREATE);
        Log.d("HSR", "MainActivity onCreate");


        StepCounter stepCounter = new StepCounter(this, timerViewModel);


//        Log.d("HSR", "" + BuildConfig.GOOGLE_MAP_API_KEY );

        binding.runStartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RunStartCountdownActivity.class);
            startActivity(intent);

            gpsTracker.startLocationUpdate();
            stepCounter.start();

            binding.runStartBtn.setVisibility(View.GONE);
            binding.runEndBtn.setVisibility(View.VISIBLE);
            binding.showRecordBtn.setVisibility(View.GONE);
            binding.stepcountTimerContainer.setVisibility(View.VISIBLE);

            //timer
            timerViewModel.startTimer();
            stepCounter.setStepCountListener(stepCount -> binding.tvStepCount.setText(String.valueOf(stepCount)));
        });

        //timer 관찰
        timerViewModel.getTimeTextLiveData().observe(this, s -> binding.tvTime.setText(s));

        binding.runEndBtn.setOnClickListener(v -> {
            gpsTracker.stopUsingGPS();
            gpsTracker.stopService(new Intent(MainActivity.this, GpsTrackerService.class));
            gpsTracker.stopNotification();
            stepCounter.stop();

            binding.stepcountTimerContainer.setVisibility(View.GONE);
            binding.runEndBtn.setVisibility(View.GONE);

            //timer
            timerViewModel.stopTimer();

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.run_history, new MainHistoryFragment());
            transaction.addToBackStack(null);   //transaction 단위 저장
            transaction.commit();

            binding.mainConstraintLayout.setVisibility(View.GONE);

        });

        binding.showRecordBtn.setOnClickListener(v -> {

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.run_history, new MainHistoryFragment());
            transaction.addToBackStack(null);   //transaction 단위 저장
            transaction.commit();

            binding.runStartBtn.setVisibility(View.GONE);
            binding.showRecordBtn.setVisibility(View.GONE);

            binding.mainConstraintLayout.setVisibility(View.GONE);
        });
    }

    GpsTrackerService.updateMap listener = new GpsTrackerService.updateMap() {

        @Override
        public void drawMap(Location location) {
            Log.d("HSR", "MainActivity.updateMap : " + location);

            timerViewModel.setGpsLocation(location);

            runFragment.drawMap(location);
        }

    };
    ServiceConnection serviceGpsTrackerConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            gpsTracker.setListener(null);
            gpsTracker = null;
            Log.d("HSR", "MainActivity onServiceDisconnected");

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service != null) {
                GpsTrackerService.LocalBinder mGpsTrackerServiceBinder = (GpsTrackerService.LocalBinder) service;
                gpsTracker = mGpsTrackerServiceBinder.getService();
                gpsTracker.startForeground();
                gpsTracker.setListener(listener);
                Log.d("HSR", "MainActivity onServiceConnected");

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceGpsTrackerConnection);

        Log.d("HSR", "MainActivity onDestroy");

    }
}