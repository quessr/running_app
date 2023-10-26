package com.example.running_app.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.running_app.R;
import com.example.running_app.data.database.dao.TimerBroadcastReceiver;
import com.example.running_app.data.database.dao.TimerService;
import com.example.running_app.databinding.FragmentTimerBinding;
import com.example.running_app.ui.viewmodels.TimerViewmodel;

public class Timer_Fragment extends Fragment {
    FragmentTimerBinding timerBinding;
    TimerViewmodel timerViewmodel;
    Intent serviceIntent;

    private boolean timerStarted = false;
    private double time = 0.0;

    private TimerBroadcastReceiver receiver;

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        timerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_timer, container, false);
        timerBinding.setLifecycleOwner(this);

        timerViewmodel = new ViewModelProvider(this).get(TimerViewmodel.class);
        timerBinding.setVm(timerViewmodel);


        serviceIntent = new Intent(requireContext(), TimerService.class);
//        requireActivity().registerReceiver(updateTime, new IntentFilter(TimerService.TIMER_UPDATED));₩
        receiver = new TimerBroadcastReceiver(this);
        requireActivity().registerReceiver(receiver, new IntentFilter(TimerService.TIMER_UPDATED));


        timerBinding.startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    if (timerStarted)
                        stopTimer();
                    else
                        startTimer();
                }

            }
        });
        timerBinding.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        return timerBinding.getRoot();
    }

    private void resetTimer() {
        stopTimer();
        time = 0.0;
        timerBinding.timeTV.setText(getTimeStringFromDouble(time));
    }

    private void startTimer() {
        time = Double.parseDouble(timerBinding.timeTV.getText().toString().replace(":", "")); // "hh:mm:ss" 형식을 숫자로 변환
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time);
        requireContext().startService(serviceIntent);
        timerBinding.startStopButton.setText("stop");
        timerStarted = true;
    }

    private void stopTimer() {
        requireContext().stopService(serviceIntent);
        timerBinding.startStopButton.setText("Start");
        timerStarted = false;
    }

//        private final BroadcastReceiver updateTime = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0);
//            timerBinding.timeTV.setText(getTimeStringFromDouble(time));
//
//            Toast.makeText(context, "Time check", Toast.LENGTH_SHORT).show();
//        }
//    };


    public void updateTime(double reTime) {
        timerBinding.timeTV.setText(getTimeStringFromDouble(reTime));
    }

    private String getTimeStringFromDouble(double time) {
        int resultInt = (int) Math.round(time);
        int hours = resultInt % 86400 / 3600;
        int minutes = resultInt % 86400 % 3600 / 60;
        int seconds = resultInt % 86400 % 3600 % 60;

        return makeTimeString(hours, minutes, seconds);
    }

    @SuppressLint("DefaultLocale")
    private String makeTimeString(int hours, int minutes, int seconds) {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


}