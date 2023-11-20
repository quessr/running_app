package com.example.running_app.ui.viewmodels;

import android.app.Application;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.running_app.data.database.dao.TB_GPS;
import com.example.running_app.data.database.dao.TB_Run;
import com.example.running_app.data.model.RunRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TimerViewModel extends AndroidViewModel {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    private long time = 0;

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private Timer timer;
    private TimerTask timerTask;
//    private boolean handler;

    //db가 동작할때 run_id를 한번만 쓰기 위해 activeRunId 변수 설정
    private int activeRunId = -1;

    //timer, room DB
//    private RunDao runDao;
    RunViewModel runViewModel;
    RunRepository runRepository;

    //stepCounter
    private int mStepDetector;

    TB_Run tbRun = new TB_Run();
    TB_GPS tbGps = new TB_GPS();
    private final MutableLiveData<String> timeTextLiveData = new MutableLiveData<>();

    public LiveData<String> getTimeTextLiveData() {
        return timeTextLiveData;
    }

    public TimerViewModel(@NonNull Application application) {
        super(application);
        runRepository = new RunRepository(application);
//        runDao = RunDatabase.INSTANCE.runDao();
    }

    public void setRunViewModel(RunViewModel runViewModel){
        this.runViewModel = runViewModel;
    }
    public void setStepCounter(int mStepDetector) {
        this.mStepDetector = mStepDetector;
    }

    public void setGpsLocation(Location location) {
        if(activeRunId == -1) {
            return;
        }

        //GPS 값일 경우만 데이터 저장
        if ("gps".equals(location.getProvider())){

            tbGps.setRun_id(activeRunId);
            tbGps.setLat(location.getLatitude());
            tbGps.setLon(location.getLongitude());
            runViewModel.setInsertGps(tbGps);
        }

    }
    public void startTimer() {
        start();
    }

    private void start() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    time++;
                    timeTextLiveData.postValue(getTimeStringFromLong(time));
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 1000);
        }, 3000);

        //시작시 테이블 생성
        runViewModel.setInsertRun(tbRun);
        activeRunId = runRepository.getLatestRunId();
    }

    public void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;

            Log.d("Stop Data", getTimeStringFromLong(time));

            //UpDate
            //update 함수 호출하면서 비어있던 데이터 저장
            activeRunId = runRepository.getLatestRunId();

            tbRun.setRun_id(activeRunId);
            tbRun.setWalk_count(mStepDetector);
            tbRun.setTimer(time);
            tbRun.setCreate_at(currentDate());
            runViewModel.setUpdateRun(tbRun);


            //stop 버튼 클릭시 바로 0.0 초로 리셋
            time = 0;
            timeTextLiveData.postValue(getTimeStringFromLong(time));
        }

        //종료 버튼 누를시 다시 -1 상태로 초기화
        activeRunId = -1;
    }

    private String currentDate() {
//        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }


    //SimpleDateFormat 함수 사용
    private String getTimeStringFromLong(long time) {

        int resultInt = Math.round(time);
        int hours = resultInt / 3600;
        int minutes = (resultInt % 3600) / 60;
        int seconds = resultInt % 60;

        Date date = new Date(0, 0, 0, hours, minutes, seconds);

        return timeFormat.format(date);
    }

}
