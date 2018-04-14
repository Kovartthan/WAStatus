package com.ko.wastatus.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;

import com.ko.wastatus.R;
import com.ko.wastatus.WAApp;
import com.ko.wastatus.intro.SplashActivity;

import java.util.Calendar;
import java.util.Date;

public class NotificationReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 951;
    private int FIXED_JOB_ID = 121;

    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour > 8) {
            return;
        }
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Whatsapp Status Saver");
        builder.setContentText("Don't forgot to save your friends Whatsapp status....");
        builder.setSmallIcon(R.drawable.notification_icon);
        Intent notifyIntent = new Intent(context, SplashActivity.class);
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String strRingtonePreference = sharedPreferences.getString("key_notifications_new_message_ringtone", "DEFAULT_SOUND");
        Uri defaultSoundUri = Uri.parse(strRingtonePreference);
        builder.setSound(defaultSoundUri);
        if (WAApp.getApp().getWaPreference().getVibrateNotification()) {
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        }
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
//        context.startService(jobIntent);
    }
}