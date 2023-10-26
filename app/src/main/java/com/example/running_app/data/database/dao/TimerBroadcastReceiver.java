package com.example.running_app.data.database.dao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.running_app.ui.fragments.Timer_Fragment;


public class TimerBroadcastReceiver extends BroadcastReceiver {
    Timer_Fragment timer_fragment;

    double re_time = 0.0;

    public TimerBroadcastReceiver(Timer_Fragment timerFragment) {
        this.timer_fragment = timerFragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        re_time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0);
        timer_fragment.updateTime(re_time);
        Toast.makeText(context, "Time check", Toast.LENGTH_SHORT).show();
    }
}
