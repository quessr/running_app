package com.example.running_app.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.running_app.data.database.dao.TB_Run;
import com.example.running_app.data.model.RunRepository;

import java.util.List;

public class RunViewModel extends AndroidViewModel {
    RunRepository runRepository;
    LiveData<List<TB_Run>> getRunAll;

    public RunViewModel(@NonNull Application application){
        super(application);
        runRepository = new RunRepository(application);
        getRunAll = runRepository.getRunAll();
    }

    public LiveData<List<TB_Run>> getRunAll(){
        return getRunAll;
    }

    //insert 함수
    //delete 함수
    //update 함수
}
