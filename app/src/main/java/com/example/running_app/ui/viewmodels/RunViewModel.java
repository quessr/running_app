package com.example.running_app.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.running_app.data.database.dao.TB_GPS;
import com.example.running_app.data.database.dao.TB_Run;
import com.example.running_app.data.model.RunRepository;

import java.util.List;

public class RunViewModel extends AndroidViewModel {
    RunRepository runRepository;
    LiveData<List<TB_Run>> getRunAll;
    List<TB_Run> getLatestActiveOne;

    public RunViewModel(@NonNull Application application){
        super(application);
        runRepository = new RunRepository(application);
        getRunAll = runRepository.getRunAll();
//        getLatestActiveOne = runRepository.getLatestActiveOne();
    }

    public LiveData<List<TB_Run>> getRunAll(){
        return getRunAll;
    }

    public List<TB_Run> getLatestActiveOne() {
        getLatestActiveOne = runRepository.getLatestActiveOne();
        return getLatestActiveOne;
    }

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
