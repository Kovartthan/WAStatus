package com.ko.statussaver.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.ko.statussaver.R;
import com.ko.statussaver.WAApp;
import com.ko.statussaver.intro.SplashActivity;
import com.ko.statussaver.utils.Constants;

import java.io.File;

import static com.ko.statussaver.utils.Constants.WHATSAPP_STATUSES_LOCATION;



public class NotificationJobSchedulerService extends JobService {
    private ServiceManager serviceManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
//        Log.e("NotificationReceive", "onStartJob");
        int notifyID = 1;
        serviceManager = new ServiceManager(this);
        try {
            if(serviceManager.isAppInForeground(this)){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "wa status saver";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(this);
        }
        builder.setContentTitle("Status Saver");
        builder.setSmallIcon(R.drawable.notification_icon);
        builder.setAutoCancel(true);

        Intent notifyIntent = new Intent(this, SplashActivity.class);
        notifyIntent.putExtra(Constants.CLEAR_NOTIFICATION, true);
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

        File file = new File(Environment.getExternalStorageDirectory() + WHATSAPP_STATUSES_LOCATION);

        if (file.listFiles() != null && file.listFiles().length > WAApp.getApp().getDatabaseHandler().getFileCount()) {
            int count = file.listFiles().length - WAApp.getApp().getDatabaseHandler().getFileCount();
            builder.setContentText(count + " new stories are available to download");
        } else if (file.listFiles() != null && file.listFiles().length > 0){
            builder.setContentText("Don't forgot to save your friend's Whatsapp status....");
        }

        Notification notificationCompat = builder.build();
        NotificationManager managerCompat = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // create channel in new versions of android
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            managerCompat.createNotificationChannel(notificationChannel);
        }
        managerCompat.notify(2, notificationCompat);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
//        Log.e("NotificationReceive", "onStopJob");
        return true;
    }
}
