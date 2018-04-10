package com.ko.wastatus.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.JobIntentService;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    private int FIXED_JOB_ID = 121;

    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("NotificationReceiver","onReceive");
        Intent jobIntent = new Intent(context, NotificationJobIntentService.class);
        JobIntentService.enqueueWork(context, NotificationJobIntentService.class, FIXED_JOB_ID,new Intent());
//        context.startService(jobIntent);
    }
}