package com.example.running_app.data.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.running_app.ui.MainActivity;
import com.example.running_app.ui.viewmodels.RunViewModel;
import com.example.running_app.ui.viewmodels.TimerViewModel;

public class StepCounter implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    private int mStepDetector = 0;  //현재 걸음수
    private StepCountListener stepCountListener;

    //stepCount
    TimerViewModel timerViewModel;
    RunViewModel runViewModel;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public StepCounter(Context context) {

        // 활동 퍼미션 체크
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        // 걸음 센서 연결
        // * 옵션
        // - TYPE_STEP_DETECTOR:  리턴 값이 무조건 1, 앱이 종료되면 다시 0부터 시작
        // - TYPE_STEP_COUNTER : 앱 종료와 관계없이 계속 기존의 값을 가지고 있다가 1씩 증가한 값을 리턴
        //
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        // 디바이스에 걸음 센서의 존재 여부 체크
        if (stepCountSensor == null) {
            Toast.makeText(context, "No Step Sensor", Toast.LENGTH_SHORT).show();
        }
    }


//    public void saveStepCount(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("StepCounter", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("stepCount", mStepDetector);
//        editor.apply();
//    }

//    public int loadStepCount(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("StepCounter", Context.MODE_PRIVATE);
//
//        int result = sharedPreferences.getInt("stepCount", 0);
//        timerViewModel.setStepCount(result);
////        return sharedPreferences.getInt("stepCount", 0);
//        return result;
//    }


    public void start() {
        if(stepCountSensor !=null) {
            // 센서 속도 설정
            // * 옵션
            // - SENSOR_DELAY_NORMAL: 20,000 초 딜레이
            // - SENSOR_DELAY_UI: 6,000 초 딜레이
            // - SENSOR_DELAY_GAME: 20,000 초 딜레이
            // - SENSOR_DELAY_FASTEST: 딜레이 없음
            //
            sensorManager.registerListener(this,stepCountSensor,SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    public void stop() {
        sensorManager.unregisterListener(this, stepCountSensor);
        mStepDetector = 0;
        stepCountListener.onStepCountChanged(mStepDetector);
//        saveStepCount(MainActivity.mContext);
    }

//    public int getStepCount() {
//        return mStepDetector;
//    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
//            mStepDetector = (int) event.values[0];
//
//        } else if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
//            if(event.values[0] == 1.0f) {
//                mStepDetector += event.values[0];
//
//            }
//        }
//
//        if (stepCountListener != null) {
//            stepCountListener.onStepCountChanged(mStepDetector);
//        }

        // 걸음 센서 이벤트 발생시
        if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){

            if(event.values[0]==1.0f){
                // 센서 이벤트가 발생할때 마다 걸음수 증가
                mStepDetector++;
//                stepCountView.setText(String.valueOf(mStepDetector));
                stepCountListener.onStepCountChanged(mStepDetector);
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface StepCountListener {
        void onStepCountChanged(int stepCount);
    }

    public void setStepCountListener(StepCountListener listener) {
        this.stepCountListener = listener;
    }
}
