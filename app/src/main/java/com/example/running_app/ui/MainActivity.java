package com.example.running_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.running_app.BuildConfig;
import com.example.running_app.R;
import com.example.running_app.ui.fragments.RunFragment;
import com.example.running_app.ui.fragments.RunRecordsFragment;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    RunFragment runFragment = new RunFragment();
    RunRecordsFragment runRecordsFragment = new RunRecordsFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentTransaction.add(R.id.run_fragment_container, runFragment);
        fragmentTransaction.add(R.id.run_records_container, runRecordsFragment);
        fragmentTransaction.commit();

        Log.d("HSR", "" + BuildConfig.GOOGLE_MAP_API_KEY );
    }

}