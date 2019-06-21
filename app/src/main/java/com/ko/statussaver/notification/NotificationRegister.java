package com.ko.statussaver.notification;

import android.app.Notification;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;

import com.ko.statussaver.R;
import com.ko.statussaver.WAApp;

public class NotificationRegister {
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private ComponentName serviceComponent;
    private JobInfo.Builder builder;
    private JobScheduler jobScheduler;
    private Context context;

    public NotificationRegister(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        networkChangeReceiver = new NetworkChangeReceiver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            serviceComponent = new ComponentName(context, NotificationJobSchedulerService.class);
            builder = new JobInfo.Builder(0, serviceComponent);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            builder.setPersisted(true);
            jobScheduler = context.getSystemService(JobScheduler.class);
        }
    }

    public void registerNotificationService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jobScheduler.schedule(builder.build());
        } else {
            context.registerReceiver(networkChangeReceiver, intentFilter);
        }
    }

    public void unRegisterNotificationService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try{
                JobScheduler jobSchedulerOne;
                jobSchedulerOne = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE );
                jobSchedulerOne.cancelAll();
            }catch (Exception e) {
                e.printStackTrace();
                jobScheduler.cancelAll();
            };
        } else {
            unregisterReceiverFromManifest(NetworkChangeReceiver.class, context);
        }
    }


    public void unregisterReceiverFromManifest(Class<? extends BroadcastReceiver> clazz, final Context context) {
        final ComponentName component = new ComponentName(context, clazz);
        final int status = context.getPackageManager().getComponentEnabledSetting(component);
        if (status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            context.getPackageManager()
                    .setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
        }
    }

    public void sendSuccessNotification() {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Status Saver");
        builder.setContentText("Your notification activated.");
        builder.setSmallIcon(R.drawable.notification_icon);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String strRingtonePreference = sharedPreferences.getString("key_notifications_new_message_ringtone", "DEFAULT_SOUND");
        Uri defaultSoundUri = Uri.parse(strRingtonePreference);
        builder.setSound(defaultSoundUri);
        if (WAApp.getApp().getWaPreference().getVibrateNotification()) {
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        }
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(100, notificationCompat);
    }

}
