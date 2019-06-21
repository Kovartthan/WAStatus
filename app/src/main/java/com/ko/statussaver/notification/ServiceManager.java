package com.ko.statussaver.notification;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

public class ServiceManager {

    Context context;

    public ServiceManager(Context base) {
        context = base;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public boolean isAppInForeground(Context context) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
            String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
            return foregroundTaskPackageName.toLowerCase().equals(context.getPackageName().toLowerCase());
        }
        else {
            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);
            if (appProcessInfo.importance ==  ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND || appProcessInfo.importance ==  ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE)
            {
                return true;
            }
            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            return km.inKeyguardRestrictedInputMode();
        }
    }
}