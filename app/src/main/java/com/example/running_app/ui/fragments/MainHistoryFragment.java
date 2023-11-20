package com.example.running_app.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.running_app.R;
import com.example.running_app.databinding.ActivityMainBinding;
import com.example.running_app.ui.MainActivity;

public class MainHistoryFragment extends Fragment {

    Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        toolbar = view.findViewById(R.id.runToolbar);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.listFrameLayout, new RunHistoryFragment());
        fragmentTransaction.commit();

        //Toolbar
        toolbar.setTitle("운동기록");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        //뒤로 가기
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        return view;
    }
}