package com.example.running_app.ui.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.running_app.data.database.dao.RunDao;
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

    public RunViewModel(@NonNull Application application){
        super(application);
        runRepository = new RunRepository(application);

        runList.postValue(runRepository.getRunAll());
        gpsList.postValue(runRepository.getGpsAll());

        // todo: 로그 확인용 변수 (추후 삭제 예정)
        getFirstLocation = runRepository.getFirstLocation();
        getLastLocation = runRepository.getLastLocation();
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
    public List<TB_GPS> getGpsAllByRunId(int runId) {
        return runRepository.getGpsAllByRunId(runId);
    }


    //insert 함수
    public void setInsertRun(TB_Run tbRun) {
        runRepository.setInsertRun(tbRun);

        //바로 TimerViewModel 에서 작업
//        int runid = runRepository.getLatestRunId();
    }

    public void setInsertGps(TB_GPS tbGps) {
        runRepository.setInsertGps(tbGps);
    }


    //delete 함수
    //update 함수
}
