package com.example.running_app.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.Parcelable;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.running_app.R;
import com.example.running_app.data.database.dao.GpsDao;
import com.example.running_app.data.database.dao.RunDao;
import com.example.running_app.data.database.dao.TB_GPS;
import com.example.running_app.data.database.dao.TB_Run;
import com.example.running_app.data.model.OnRunHistoryItemClickListener;
import com.example.running_app.data.model.RunRepository;
import com.example.running_app.ui.MainActivity;
import com.example.running_app.ui.RunningAdapter;
import com.example.running_app.ui.viewmodels.RunViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RunHistoryFragment extends Fragment {
    private OnBackPressedCallback onBackPressedCallback;
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

        runningAdapter.setOnItemClickListener(new OnRunHistoryItemClickListener(){

            @Override
            public void onItemClickListener(TB_Run item, String distanceFormat, String speedFormat, String timeFormat, List<TB_GPS> allGps) {
                Toast.makeText(getContext(), "선택 : " + item.getRun_id(), Toast.LENGTH_SHORT).show();
                DetailMainHistoryFragment detailMainHistoryFragment = new DetailMainHistoryFragment();


                Bundle bundle = new Bundle();
                bundle.putSerializable("RUN_ITEM", item);    //객체로 전달하기 위해 TB_RUN 클래스 Serializable사용
                bundle.putString("distance", distanceFormat);
                bundle.putString("speed", speedFormat);
                bundle.putString("time", timeFormat);
                bundle.putParcelableArrayList("GPS_LIST", (ArrayList<? extends Parcelable>) allGps);
                detailMainHistoryFragment.setArguments(bundle);

                // 화면 전환
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.run_history, detailMainHistoryFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
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
                Toast.makeText(getActivity(), "뒤로가기 버튼 클릭", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

}
