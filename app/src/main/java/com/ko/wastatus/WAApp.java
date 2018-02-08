package com.ko.wastatus;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.ko.wastatus.preference.WAPreference;


public class WAApp extends Application {
    private static WAApp mInstance;
    private WAPreference waPreference;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        waPreference = new WAPreference(this);
    }

    public static WAApp getApp() {
        if (mInstance != null && mInstance instanceof WAApp) {
            return mInstance;
        } else {
            mInstance = new WAApp();
            mInstance.onCreate();
            return mInstance;
        }
    }

    public WAPreference getWaPreference(){
        return waPreference;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
}
