package com.wao.dogcat.controller.server;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

public class Pedometer implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mStepCount;
    private Sensor mStepDetector;
    public static int mCount;//步行总数
    private float mDetector;//步行探测器
    private Context context;
    private static final int sensorTypeD = Sensor.TYPE_STEP_DETECTOR;
    private static final int sensorTypeC = Sensor.TYPE_STEP_COUNTER;
    private boolean hasRecord = false;
    private int hasStepCount = 0;
    private int prviousStepCount = 0;

    public static int goDieCount = 0;
    public static int goDieAllcount = 0;

    public Pedometer() {

    }

    public Pedometer(Context context) {
        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mStepCount = mSensorManager.getDefaultSensor(sensorTypeC);
        mStepDetector = mSensorManager.getDefaultSensor(sensorTypeD);

        if (mStepCount != null && mStepDetector != null) {
            Log.v("Pedometer", " Success! There's a magnetometer.");
        } else {
            Log.v("Pedometer", " Failure! No magnetometer.");
        }

    }

    public void register() {
        register(mStepCount, SensorManager.SENSOR_DELAY_FASTEST);
        register(mStepDetector, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unRegister() {
        mSensorManager.unregisterListener(this);
    }

    private void register(Sensor sensor, int rateUs) {

        mSensorManager.registerListener(this, sensor, rateUs);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == sensorTypeC) {

            goDieAllcount = (int) event.values[0];
            int tempStep = (int) event.values[0];
            if (!hasRecord) {
                hasRecord = true;
                hasStepCount = tempStep;
            } else {
                int thisStepCount = tempStep - hasStepCount;
                mCount += (thisStepCount - prviousStepCount);
                prviousStepCount = thisStepCount;
            }
        }
        if (event.sensor.getType() == sensorTypeD) {
            if (event.values[0] == 1.0) {
                mDetector++;
            }
        }
    }


    public float getmDetector() {
        return mDetector;
    }

}
