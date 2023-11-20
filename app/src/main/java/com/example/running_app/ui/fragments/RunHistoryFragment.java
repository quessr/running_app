package com.example.running_app.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.Parcelable;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.running_app.R;



import com.example.running_app.databinding.FragmentRunHistoryBinding;
import com.example.running_app.ui.MainActivity;
import com.example.running_app.ui.RunningAdapter;
import com.example.running_app.ui.viewmodels.RunViewModel;


import java.util.ArrayList;


public class RunHistoryFragment extends Fragment {
    private OnBackPressedCallback onBackPressedCallback;
    RunViewModel viewModel;
    RecyclerView recyclerView;
    RunningAdapter runningAdapter;

    public FragmentRunHistoryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRunHistoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        Log.d("onCreateView", "onCreateView 화면");
        findID(view);

        runningAdapter = new RunningAdapter(requireActivity().getApplication(), getActivity());
        recyclerView.setAdapter(runningAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        viewModel = new ViewModelProvider(this).get(RunViewModel.class);
        viewModel.getRunAll().observe(getViewLifecycleOwner(), tbRuns -> {
            runningAdapter.setRunItems(tbRuns);
            runningAdapter.notifyDataSetChanged();
        });

        runningAdapter.setOnItemClickListener((item, distanceFormat, speedFormat, timeFormat, allGps) -> {
            DetailMainHistoryFragment detailMainHistoryFragment = new DetailMainHistoryFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable("RUN_ITEM", item);    //객체로 전달하기 위해 TB_RUN 클래스 Serializable사용
            bundle.putString("distance", distanceFormat);
            bundle.putString("speed", speedFormat);
            bundle.putString("time", timeFormat);
            bundle.putParcelableArrayList("GPS_LIST", (ArrayList<? extends Parcelable>) allGps);    //용량제한?
            detailMainHistoryFragment.setArguments(bundle);

            // 화면 전환
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.run_history, detailMainHistoryFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            binding.recyclerview.setVisibility(View.GONE);
        });

        return view;
    }


    private void findID(View view) {
        recyclerView = view.findViewById(R.id.recyclerview);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

}
