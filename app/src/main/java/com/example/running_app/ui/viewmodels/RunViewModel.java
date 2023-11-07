package com.example.running_app.ui.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.running_app.data.database.dao.RunDao;
import com.example.running_app.data.database.dao.RunDatabase;
import com.example.running_app.data.database.dao.TB_GPS;
import com.example.running_app.data.database.dao.TB_Run;
import com.example.running_app.data.model.RunRepository;
import com.example.running_app.data.model.StepCounter;

import java.util.List;

public class RunViewModel extends AndroidViewModel {
    RunRepository runRepository;
    LiveData<List<TB_Run>> getRunAll;
    LiveData<List<TB_GPS>> getGpsAll;
    TB_GPS getFirstLocation;
    TB_GPS getLastLocation;
    List<TB_Run> getLatestActiveOne;

    public RunViewModel(@NonNull Application application){
        super(application);
        runRepository = new RunRepository(application);
        getRunAll = runRepository.getRunAll();
        getGpsAll = runRepository.getGpsAll();

        getFirstLocation = runRepository.getFirstLocation();
        getLastLocation = runRepository.getLastLocation();
//        getLatestActiveOne = runRepository.getLatestActiveOne();
    }

    public LiveData<List<TB_Run>> getRunAll(){
        return getRunAll;
    }
    public LiveData<List<TB_GPS>> getGpsAll(){
        return getGpsAll;
    }

    public TB_GPS getFirstLocation(){
        return getFirstLocation;
    }

    public TB_GPS getLastLocation(){
        return getLastLocation;
    }

//    public List<TB_Run> getLatestActiveOne() {
//        getLatestActiveOne = runRepository.getLatestActiveOne();
//        return getLatestActiveOne;
//    }

    //insert 함수
    public void setInsertRun(TB_Run tbRun) {
        runRepository.setInsertRun(tbRun);
    }

    public void setInsertGps(TB_GPS tbGps) {
        runRepository.setInsertGps(tbGps);
    }


    //delete 함수
    //update 함수
}
