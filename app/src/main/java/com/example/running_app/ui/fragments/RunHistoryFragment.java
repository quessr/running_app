package com.example.running_app.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.running_app.R;
import com.example.running_app.data.database.dao.GpsDao;
import com.example.running_app.data.database.dao.RunDao;
import com.example.running_app.data.database.dao.RunDatabase;
import com.example.running_app.data.database.dao.TB_GPS;
import com.example.running_app.data.database.dao.TB_Run;
import com.example.running_app.data.model.RunRepository;
import com.example.running_app.ui.RunningAdapter;
import com.example.running_app.ui.viewmodels.RunViewModel;

import java.util.List;
import java.util.Objects;

import com.example.running_app.R;
import com.example.running_app.ui.viewmodels.TimerViewModel;

public class RunHistoryFragment extends Fragment {

    //전역변수 설정
    private RunDao runDao;
    private GpsDao gpsDao;

    RunViewModel viewModel;
    RecyclerView recyclerView;
    RunningAdapter runningAdapter;
    RunRepository repository;
    private int activeRunId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_run_history, container, false);
        Log.d("onCreateView", "onCreateView 화면");
        findID(view);

        runningAdapter = new RunningAdapter(requireActivity().getApplication(), getActivity());
        recyclerView.setAdapter(runningAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        viewModel = new ViewModelProvider(this).get(RunViewModel.class);
        viewModel.getRunAll().observe(getViewLifecycleOwner(), new Observer<List<TB_Run>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<TB_Run> tbRuns) {
                runningAdapter.setRunItems(tbRuns);
                runningAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private double totalDistance(List<TB_GPS> allGps) {
          double totalDistance = 0;
          TB_GPS prevGps = null;

        for (TB_GPS currentGps : allGps) {
            if (prevGps != null){
                // 이전 GPS와 현재 GPS 사이의 거리 계산하여 누적
//                double segmentDistance = haversine(prevGps.getLatitude(), prevGps.getLongitude(), currentGps.getLatitude(), currentGps.getLongitude());
                double segmentDistance = haversine(prevGps.getLat(), prevGps.getLon(), currentGps.getLat(), currentGps.getLon());
                totalDistance += segmentDistance;
            }
            prevGps = currentGps;   // 현재 GPS를 이전 GPS로 설정하여 다음 순회에 사용
        }

        return totalDistance;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        // 지구의 반경 (단위: km)
        final double R = 6371.0;

        // 라디안으로 변환
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        // 위도와 경도의 차이 계산
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        // Haversine 공식 적용
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 거리 계산
        double distance = R * c;

        return distance;
    }


    private void findID(View view) {
        recyclerView = view.findViewById(R.id.recyclerview);
//        toolbar = view.findViewById(R.id.runToolbar);
    }

}
