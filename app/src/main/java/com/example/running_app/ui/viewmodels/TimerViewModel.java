package com.example.running_app.ui.viewmodels;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class TimerViewModel extends ViewModel {
    private double time = 0.0;
    private Timer timer;
    private TimerTask timerTask;
    private boolean handler;

    private MutableLiveData<String> timeTextLiveData = new MutableLiveData<>();

    public LiveData<String> getTimeTextLiveData() {
        return timeTextLiveData;
    }


    public void startTimer() {
        start();
    }

    private void start() {
        handler = new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        time++;
                        timeTextLiveData.postValue(getTimeStringFromDouble(time));
                    }
                };
                timer.scheduleAtFixedRate(timerTask, 0, 1000);
            }
        }, 3000);
    }
    public void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;

            Log.d("Stop Data", getTimeStringFromDouble(time));

            //stop 버튼 클릭시 바로 0.0 초로 리셋
            time = 0.0;
            timeTextLiveData.postValue(getTimeStringFromDouble(time));
        }
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
