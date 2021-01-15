package com.example.android.lightsensor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    // System sensor manager instance.
    private SensorManager mSensorManager;

    // Individual light and proximity sensors.
    private Sensor mSensorLight;

    // TextViews to display current sensor values
    private TextView mTextSensorLight;

    // Whether to change the layout to dark theme
    private boolean mChangeToDark = false;
    private static final String KEY_CHANGE_TO_DARK = "change_to_dark_theme";

    // Current light sensor value
    private float mCurrentValue;
    private static final String KEY_CURRENT_VALUE = "current_light_value";

    // When the current light sensor's value is lower than 10 lux,
    // change the layout to dark theme
    private static final int SWITCH_LUX = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mChangeToDark = savedInstanceState.getBoolean(KEY_CHANGE_TO_DARK);
            mCurrentValue = savedInstanceState.getFloat(KEY_CURRENT_VALUE);
        }
        // Change the app theme if mChangeToDark is true
        if (mChangeToDark) {
            setTheme(android.R.style.Theme_DeviceDefault_NoActionBar);
        }
        setContentView(R.layout.activity_main);

        // Initialize all view variables.
        mTextSensorLight = (TextView) findViewById(R.id.label_light);

        if (mCurrentValue >= 0) {
            mTextSensorLight.setText(getResources().getString(R.string.label_light, mCurrentValue));
        }

        // Get an instance of the sensor manager.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Get light sensor from the sensor manager.
        // The getDefaultSensor() method returns null if the sensor
        // is not available on the device.
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        // Get the error message from string resources.
        String sensor_error = getResources().getString(R.string.error_no_sensor);
        if (mSensorLight == null) { mTextSensorLight.setText(sensor_error); }
    }

    /**
     * Don't register your listeners in onCreate(), as that would
     * cause the sensors to be on and sending data (using device power)
     * even when your app was not in the foreground.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (mSensorLight != null) {
            mSensorManager.registerListener(this, mSensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_CHANGE_TO_DARK, mChangeToDark);
        outState.putFloat(KEY_CURRENT_VALUE, mCurrentValue);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        mCurrentValue = event.values[0];
        if (sensorType == Sensor.TYPE_LIGHT) {
            mTextSensorLight.setText(getResources().getString(R.string.label_light, mCurrentValue));
        }
        if (mCurrentValue < SWITCH_LUX && !mChangeToDark) {
            mChangeToDark = true;
            recreate();
        } else if (mCurrentValue >= SWITCH_LUX && mChangeToDark) {
            mChangeToDark = false;
            recreate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}