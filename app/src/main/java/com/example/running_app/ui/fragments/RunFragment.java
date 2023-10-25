package com.example.running_app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.running_app.databinding.FragmentRunBinding;

public class RunFragment extends Fragment {
    private FragmentRunBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRunBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }
}
