package com.ko.wastatus.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationManagerCompat;

import com.ko.wastatus.R;
import com.ko.wastatus.WAApp;
import com.ko.wastatus.intro.SplashActivity;

public class NotificationJobIntentService extends JobIntentService {
    private static final int NOTIFICATION_ID = 3;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Whatsapp Status Saver");
        builder.setContentText("Don't forgot to save your friends Whatsapp status....");
        builder.setSmallIcon(R.drawable.notification_icon);
        Intent notifyIntent = new Intent(this, SplashActivity.class);
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String strRingtonePreference = sharedPreferences.getString("key_notifications_new_message_ringtone", "DEFAULT_SOUND");
        Uri defaultSoundUri = Uri.parse(strRingtonePreference);
        builder.setSound(defaultSoundUri);
        if (WAApp.getApp().getWaPreference().getVibrateNotification()) {
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        }
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }
}
