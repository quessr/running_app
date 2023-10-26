package com.example.running_app.ui.viewmodels;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.running_app.data.database.dao.TimerService;

import java.net.HttpCookie;


public class TimerViewmodel extends ViewModel {
    public MutableLiveData<String> time_count = new MutableLiveData<>("00:00:00");

}
