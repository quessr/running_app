package com.example.running_app.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.running_app.R;
import com.example.running_app.data.database.dao.TB_Run;

public class DetailMainHistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_main_history, container, false);

        DetailHistoryFragment detailHistoryFragment = new DetailHistoryFragment();

        // DetailMainHistoryFragment에서 받아온 Bundle 객체
        Bundle receivedBundle = getArguments();

        // 전달할 Bundle 객체 설정
        Bundle bundle = new Bundle();
        if (receivedBundle != null) {
            bundle.putAll(receivedBundle); // DetailMainHistoryFragment에서 받아온 Bundle 객체를 전달할 Bundle 객체에 추가
        }

        // DetailHistoryFragment에 전달할 Bundle 설정
        detailHistoryFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapLayout, new DetailMapFragment());
        fragmentTransaction.add(R.id.detailLatout, detailHistoryFragment);
        fragmentTransaction.commit();



        return view;
    }

}