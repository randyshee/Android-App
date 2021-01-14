package com.example.android.dailyreminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManager mNotificationManager;

    private static final int MORNING_NOTIFICATION_ID = 0;
    private static final int NOON_NOTIFICATION_ID = 1;
    private static final int NIGHT_NOTIFICATION_ID = 2;
    private static final String KEY_NOTIFICATION_ID = "notification_id";

    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        switch (intent.getIntExtra(KEY_NOTIFICATION_ID, 0)) {
            case MORNING_NOTIFICATION_ID:
                deliverMorningCall(context);
                break;
            case NOON_NOTIFICATION_ID:
                deliverNoonCall(context);
                break;
            case NIGHT_NOTIFICATION_ID:
                deliverNightCall(context);
        }
    }

    private void deliverMorningCall(Context context) {
        Intent morningContentIntent = new Intent(context, MainActivity.class);
        PendingIntent morningContentPendingIntent = PendingIntent.getActivity
                (context, MORNING_NOTIFICATION_ID, morningContentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.string_morning_notification_title))
                .setContentText(context.getString(R.string.string_morning_notification_text))
                .setContentIntent(morningContentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        mNotificationManager.notify(MORNING_NOTIFICATION_ID, builder.build());
    }

    private void deliverNoonCall(Context context) {
        Intent noonContentIntent = new Intent(context, MainActivity.class);
        PendingIntent noonContentPendingIntent = PendingIntent.getActivity
                (context, NOON_NOTIFICATION_ID, noonContentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.string_noon_notification_title))
                .setContentText(context.getString(R.string.string_noon_notification_text))
                .setContentIntent(noonContentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        mNotificationManager.notify(NOON_NOTIFICATION_ID, builder.build());
    }

    private void deliverNightCall(Context context) {
        Intent nightContentIntent = new Intent(context, MainActivity.class);
        PendingIntent nightContentPendingIntent = PendingIntent.getActivity
                (context, NIGHT_NOTIFICATION_ID, nightContentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.string_night_notification_title))
                .setContentText(context.getString(R.string.string_night_notification_text))
                .setContentIntent(nightContentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        mNotificationManager.notify(NIGHT_NOTIFICATION_ID, builder.build());
    }
}
