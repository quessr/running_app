package com.example.running_app.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.running_app.data.database.dao.TB_GPS;
import com.example.running_app.data.database.dao.TB_Run;
import com.example.running_app.data.model.RunRepository;

import java.util.List;

public class RunViewModel extends AndroidViewModel {
    RunRepository runRepository;
    MutableLiveData<List<TB_Run>> runList = new MutableLiveData<>();
    MutableLiveData<List<TB_GPS>> gpsList = new MutableLiveData<>();;
    TB_GPS getFirstLocation;
    TB_GPS getLastLocation;
    List<TB_Run> getLatestActiveOne;

    public RunViewModel(@NonNull Application application){
        super(application);
        runRepository = new RunRepository(application);

        runList.postValue(runRepository.getRunAll());
        gpsList.postValue(runRepository.getGpsAll());

        // todo: 로그 확인용 변수 (추후 삭제 예정)
        getFirstLocation = runRepository.getFirstLocation();
        getLastLocation = runRepository.getLastLocation();
//        getLatestActiveOne = runRepository.getLatestActiveOne();
    }

    public MutableLiveData<List<TB_Run>> getRunAll(){
        return runList;
    }
    public List<TB_GPS> getGpsAll(){
        return gpsList.getValue();
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
