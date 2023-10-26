package com.example.running_app.ui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class TimerViewmodel extends ViewModel {
    public MutableLiveData<String> time_count = new MutableLiveData<>("00:00:00");

}
