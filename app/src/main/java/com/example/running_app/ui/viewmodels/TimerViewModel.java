package com.example.running_app.ui.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.running_app.data.database.dao.RunDao;
import com.example.running_app.data.database.dao.RunDatabase;
import com.example.running_app.data.database.dao.TB_GPS;
import com.example.running_app.data.database.dao.TB_Run;

import java.util.Timer;
import java.util.TimerTask;

public class TimerViewModel extends ViewModel {
    private double time = 0.0;
    private Timer timer;
    private TimerTask timerTask;
    private boolean handler;

    //timer, room DB
    private RunDao runDao;
    RunViewModel runViewModel;

    //stepCounter
    private int mStepDetector;

    private MutableLiveData<String> timeTextLiveData = new MutableLiveData<>();

    public LiveData<String> getTimeTextLiveData() {
        return timeTextLiveData;
    }

    public void setRunViewModel(RunViewModel runViewModel){
        this.runViewModel = runViewModel;
    }


    public void setStepCounter(int mStepDetector) {
        this.mStepDetector = mStepDetector;
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

//            runViewModel.insertRun(getTimeStringFromDouble(time));

            //timer -> insert(데이터 삽입)
            TB_Run tbRun = new TB_Run();    //객체 인스턴스 생성
            tbRun.setWalk_count(mStepDetector);
            tbRun.setTimer(getTimeStringFromDouble(time));
            tbRun.setCreate_at("2023/11/03");
//            tbRun.setIs_active(1);
            runViewModel.setInsertRun(tbRun);

            runDao = RunDatabase.INSTANCE.runDao();

//            TB_Run latest = runViewModel.getLatestActiveOne().get(tbRun.getRun_id());
//
//            //GPS 데이터 삽입
//            TB_GPS tbGps = new TB_GPS();    //객체 인스턴스 생성
//            tbGps.setRun_id(latest.getRun_id());
//            tbGps.setLat(latitude);
//            tbGps.setLon(longitude);
//            tbGps.setCreate_at("2023/11/02");
//            runViewModel.setInsertGps(tbGps);


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

//        System.currentTimeMillis();

        return makeTimeString(hours, minutes, seconds);
    }

    @SuppressLint("DefaultLocale")
    private String makeTimeString(int hours, int minutes, int seconds) {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
