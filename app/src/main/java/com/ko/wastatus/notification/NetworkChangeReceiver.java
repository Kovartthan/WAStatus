package com.ko.wastatus.notification;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.ko.wastatus.R;
import com.ko.wastatus.WAApp;
import com.ko.wastatus.intro.SplashActivity;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.content.Context.ACTIVITY_SERVICE;
import static com.ko.wastatus.utils.Constants.WHATSAPP_STATUSES_LOCATION;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 3;
    ServiceManager serviceManager;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.e("NotificationService", "onReceive");
        if (checkInternet(context)) {
            sendNotificationToLocatDevice(context);
        }
    }

    boolean checkInternet(Context context) {
        serviceManager = new ServiceManager(context);
        if (serviceManager.isNetworkAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    public void sendNotificationToLocatDevice(Context context) {
        Log.e("NotificationService", "onHandleIntent");
        if(serviceManager.isAppInForeground(context)){
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Whatsapp Status Saver");
        builder.setSmallIcon(R.drawable.notification_icon);
        builder.setAutoCancel(true);
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

        File file = new File(Environment.getExternalStorageDirectory() + WHATSAPP_STATUSES_LOCATION);

        if( file.listFiles() != null && file.listFiles().length > WAApp.getApp().getDatabaseHandler().getFileCount()){
            int count = file.listFiles().length - WAApp.getApp().getDatabaseHandler().getFileCount();
            builder.setContentText(count+" new stories are available to save");
        } else if (file.listFiles() != null && file.listFiles().length > 0){
            builder.setContentText("Don't forgot to save your friend's Whatsapp status....");
        }

        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }

}