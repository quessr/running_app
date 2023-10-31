package com.example.running_app.data.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.running_app.data.database.dao.RunDatabase;
import com.example.running_app.data.database.dao.TB_Run;

import java.util.List;

public class RunRepository {
    RunDatabase runDatabase;
    LiveData<List<TB_Run>> getRunAll;

    public RunRepository(Application application){
        runDatabase = RunDatabase.getInstance(application);
        getRunAll = runDatabase.runDao().getRunAll();
    }

   public LiveData<List<TB_Run>> getRunAll(){
        return getRunAll;
    }

    //insert 문
    //delete 문
    //update 문
}
