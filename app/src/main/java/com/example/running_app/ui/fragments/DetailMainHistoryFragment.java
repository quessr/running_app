package com.example.running_app.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.running_app.R;
import com.example.running_app.data.database.dao.TB_Run;
import com.example.running_app.databinding.ActivityMainBinding;
import com.example.running_app.databinding.FragmentDetailMainHistoryBinding;
import com.example.running_app.databinding.FragmentHistoryBinding;
import com.example.running_app.ui.MainActivity;

public class DetailMainHistoryFragment extends Fragment {

    private OnBackPressedCallback onBackPressedCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_main_history, container, false);

        DetailHistoryFragment detailHistoryFragment = new DetailHistoryFragment();
        DetailMapFragment detailMapFragment = new DetailMapFragment();

        // DetailMainHistoryFragment에서 받아온 Bundle 객체
        Bundle receivedBundle = getArguments();

        // 전달할 Bundle 객체 설정
        Bundle bundle = new Bundle();
        if (receivedBundle != null) {
            bundle.putAll(receivedBundle); // DetailMainHistoryFragment에서 받아온 Bundle 객체를 전달할 Bundle 객체에 추가
        }

        // DetailHistoryFragment에 전달할 Bundle 설정
        detailHistoryFragment.setArguments(bundle);
        detailMapFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapLayout, detailMapFragment);
        fragmentTransaction.add(R.id.detailLatout, detailHistoryFragment);
        fragmentTransaction.commit();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                Log.d("HSR", "DetailMainHistoryFragment" + " => handleOnBackPressed");

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.run_history, new MainHistoryFragment());
                fragmentTransaction.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }


}