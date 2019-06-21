package com.ko.statussaver;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;

//import com.crashlytics.android.Crashlytics;
//import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ko.statussaver.database.DatabaseHandler;
import com.ko.statussaver.preference.WAPreference;

//import io.fabric.sdk.android.Fabric;

//import io.fabric.sdk.android.Fabric;


public class WAApp extends Application {
    private static WAApp mInstance;
    private WAPreference waPreference;
    private SQLiteDatabase database;
    private DatabaseHandler databaseHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
//        Fabric.with(this, new Crashlytics());
        waPreference = new WAPreference(this);
        FirebaseAnalytics.getInstance(this);
        databaseHandler = new DatabaseHandler(this);
        database = databaseHandler.getWritableDatabase();
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

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public WAPreference getWaPreference() {
        return waPreference;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
}
