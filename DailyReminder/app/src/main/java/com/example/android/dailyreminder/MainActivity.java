package com.example.android.dailyreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private NotificationManager mNotificationManager;

    // Initialize the preferences
    private SharedPreferences mPreferences;
    private static final String sharedPrefFile = "com.example.android.dailyreminder";

    // Keys for saving the checked states for the toggle buttons
    private static final String KEY_MORNING_CHECKED = "morning_toggle_checked";
    private static final String KEY_NOON_CHECKED = "noon_toggle_checked";
    private static final String KEY_NIGHT_CHECKED = "night_toggle_checked";

    // Toggle Button checked state
    boolean mMorningChecked = false;
    boolean mNoonChecked = false;
    boolean mNightChecked = false;

    private static final int MORNING_NOTIFICATION_ID = 0;
    private static final int NOON_NOTIFICATION_ID = 1;
    private static final int NIGHT_NOTIFICATION_ID = 2;
    private static final String KEY_NOTIFICATION_ID = "notification_id";

    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    // TODO: Maybe change here
    private static PendingIntent morningPendingIntent;
    private static PendingIntent noonPendingIntent;
    private static PendingIntent nightPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Toggle buttons
        ToggleButton morningToggle = findViewById(R.id.morningToggle);
        ToggleButton noonToggle = findViewById(R.id.noonToggle);
        ToggleButton nightToggle = findViewById(R.id.nightToggle);

        // Initialize the checked states
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        mMorningChecked = mPreferences.getBoolean(KEY_MORNING_CHECKED, false);
        mNoonChecked = mPreferences.getBoolean(KEY_NOON_CHECKED, false);
        mNightChecked = mPreferences.getBoolean(KEY_NIGHT_CHECKED, false);
        morningToggle.setChecked(mMorningChecked);
        noonToggle.setChecked(mNoonChecked);
        nightToggle.setChecked(mNightChecked);

        // Initialize the onCheckedChange Listener for the toggle buttons
        morningToggle.setOnCheckedChangeListener(this);
        noonToggle.setOnCheckedChangeListener(this);
        nightToggle.setOnCheckedChangeListener(this);


        // Create the notification
        createNotificationChannel();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        switch (buttonView.getId()) {
            case R.id.morningToggle:
                if (isChecked) {
                    mMorningChecked = true;
                    morningPendingIntent = createPendingIntent(MORNING_NOTIFICATION_ID);
                    Calendar morningCalendar = createCalender(MORNING_NOTIFICATION_ID);
                    if (alarmManager != null) {
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                                morningCalendar.getTimeInMillis(),
                                AlarmManager.INTERVAL_DAY, morningPendingIntent);
                    }

                    Toast.makeText(this, R.string.string_morning_on, Toast.LENGTH_SHORT).show();
                } else {
                    mMorningChecked = false;
                    // Cancel notification if the morning toggle is turned off.
                    mNotificationManager.cancel(MORNING_NOTIFICATION_ID);
                    // TODO: morningPendingIntent should not be null though...use final?
                    if (alarmManager != null && morningPendingIntent != null) {
                        alarmManager.cancel(morningPendingIntent);
                    }
                    Toast.makeText(this, R.string.string_morning_off, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.noonToggle:
                if (isChecked) {
                    mNoonChecked = true;
                    noonPendingIntent = createPendingIntent(NOON_NOTIFICATION_ID);
                    Calendar noonCalendar = createCalender(NOON_NOTIFICATION_ID);
                    if (alarmManager != null) {
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                                noonCalendar.getTimeInMillis(),
                                AlarmManager.INTERVAL_DAY, noonPendingIntent);
                    }
                    Toast.makeText(this, R.string.string_noon_on, Toast.LENGTH_SHORT).show();
                } else {
                    mNoonChecked = false;
                    // Cancel notification if the morning toggle is turned off.
                    mNotificationManager.cancel(NOON_NOTIFICATION_ID);
                    if (alarmManager != null && noonPendingIntent != null) {
                        alarmManager.cancel(noonPendingIntent);
                    }
                    Toast.makeText(this, R.string.string_noon_off, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nightToggle:
                if (isChecked) {
                    mNightChecked = true;
                    nightPendingIntent = createPendingIntent(NIGHT_NOTIFICATION_ID);
                    Calendar nightCalendar = createCalender(NIGHT_NOTIFICATION_ID);
                    if (alarmManager != null) {
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                                nightCalendar.getTimeInMillis(),
                                AlarmManager.INTERVAL_DAY, nightPendingIntent);
                    }
                    Toast.makeText(this, R.string.string_night_on, Toast.LENGTH_SHORT).show();
                } else {
                    mNightChecked = false;
                    if (alarmManager != null && nightPendingIntent != null) {
                        alarmManager.cancel(nightPendingIntent);
                    }
                    Toast.makeText(this, R.string.string_night_off, Toast.LENGTH_SHORT).show();
                }
        }
    }

    private PendingIntent createPendingIntent(int NOTIFICATION_ID) {
        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        notifyIntent.putExtra(KEY_NOTIFICATION_ID, NOTIFICATION_ID);
        return PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Creates a Notification channel, for OREO and higher.
     */
    public void createNotificationChannel() {

        // Create a notification manager object.
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID, "Daily Reminder Notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification every morning, noon, or night");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public Calendar createCalender(int NOTIFICATION_ID) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        switch (NOTIFICATION_ID) {
            case MORNING_NOTIFICATION_ID:
                // Set the alarm to start at approximately 9:00 AM
                calendar.set(Calendar.HOUR_OF_DAY, 9);
                break;
            case NOON_NOTIFICATION_ID:
                // Set the alarm to start at approximately 12:00 PM
                calendar.set(Calendar.HOUR_OF_DAY, 12);
                break;
            case NIGHT_NOTIFICATION_ID:
                // Set the alarm to start at approximately 11:00 PM
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                break;
        }
        return calendar;
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putBoolean(KEY_MORNING_CHECKED, mMorningChecked);
        preferencesEditor.putBoolean(KEY_NOON_CHECKED, mNoonChecked);
        preferencesEditor.putBoolean(KEY_NIGHT_CHECKED, mNightChecked);
        preferencesEditor.apply();
    }

}