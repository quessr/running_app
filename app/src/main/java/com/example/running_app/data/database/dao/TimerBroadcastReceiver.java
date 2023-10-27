package com.example.running_app.data.database.dao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.running_app.ui.fragments.Timer_Fragment;


public class TimerBroadcastReceiver extends BroadcastReceiver {
    Timer_Fragment timer_fragment;

    public TimerBroadcastReceiver(Timer_Fragment timerFragment) {
        this.timer_fragment = timerFragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        timer_fragment.updateTime(intent);  //receive로 호출된 intent 값 fragment의 updateTime 함수로 전달
        Toast.makeText(context, "Time check", Toast.LENGTH_SHORT).show();
    }
}
