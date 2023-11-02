package com.example.running_app.data.model;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.example.running_app.ui.MainActivity;

public class StepCounter implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    private int mStepDetector = 0;
    private StepCountListener stepCountListener;

    public StepCounter(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        Toast.makeText(MainActivity.mContext, "StepCounter ", Toast.LENGTH_SHORT).show();

        if (stepCountSensor == null) {
            Toast.makeText(context, "No Step Sensor", Toast.LENGTH_SHORT).show();
        }
    }

    public void start() {
        if (stepCountSensor != null) {
            sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void stop() {
        sensorManager.unregisterListener(this, stepCountSensor);
    }

    public int getStepCount() {
        return mStepDetector;
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            mStepDetector = (int) event.values[0];
            Toast.makeText(MainActivity.mContext, "onSensorChanged : " + mStepDetector, Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.mContext, "onSensorChanged " + mStepDetector, Toast.LENGTH_SHORT).show();

        } else if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if(event.values[0] == 1.0f) {
                mStepDetector += event.values[0];
                Toast.makeText(MainActivity.mContext, "onSensorChanged : " + mStepDetector, Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.mContext, "onSensorChanged " + mStepDetector, Toast.LENGTH_SHORT).show();

            }
        }

        if (stepCountListener != null) {
            stepCountListener.onStepCountChanged(mStepDetector);
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
