package com.example.running_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.running_app.R;

public class RunStartCountdownActivity extends AppCompatActivity {
    private TextView countdownText;
    private int countdownValue = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_start_countdown);

        countdownText = findViewById(R.id.countdown_text);
        startCountdown();
    }

    private void startCountdown() {
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (countdownValue >= 1) {
                    countdownText.setText(String.valueOf(countdownValue));
                    countdownValue--;
                    handler.postDelayed(this, 1000); 
                } else {
                    Intent intent = new Intent(RunStartCountdownActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        handler.post(runnable);
    }
}
