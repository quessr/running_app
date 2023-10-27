package com.example.running_app.data.database.dao;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.lifecycle.ViewModelProvider;

import com.example.running_app.ui.viewmodels.TimerViewmodel;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {
    private final Timer timer = new Timer();

    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        double time = intent.getDoubleExtra(TIME_EXTRA, 0.0);
        timer.scheduleAtFixedRate(new TimeTask(time), 0, 1000); //delay(일정 시간) 이 지난 후에 period(시간 간격) 으로 task(작업)을 수행한다.
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private class TimeTask extends TimerTask {
        private double time;

        public TimeTask(double time) {
            this.time = time;
        }

        @Override
        public void run() {
            Intent intent = new Intent(TIMER_UPDATED);
            time++;
            intent.putExtra(TIME_EXTRA, time);
            sendBroadcast(intent);
        }
    }

    public static final String TIMER_UPDATED = "timerUpdated";
    public static final String TIME_EXTRA = "timeExtra";
}