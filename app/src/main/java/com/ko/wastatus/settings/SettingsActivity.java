package com.ko.wastatus.settings;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.ko.wastatus.R;
import com.ko.wastatus.WAApp;
import com.ko.wastatus.home.activities.HomeActivity;
import com.ko.wastatus.notification.NotificationReceiver;
import com.ko.wastatus.utils.Constants;

import java.util.Calendar;


public class SettingsActivity extends AppCompatPreferenceActivity implements MainPreferenceFragment.OnThemeChangedListener {
    private static final String TAG = SettingsActivity.class.getSimpleName();
    private MainPreferenceFragment mainPreferenceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateTheme();
        super.onCreate(savedInstanceState);
        // load settings fragment
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainPreferenceFragment = new MainPreferenceFragment();
        mainPreferenceFragment.setOnThemeChangedListener(this);
        getFragmentManager().beginTransaction().replace(android.R.id.content, mainPreferenceFragment).commit();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(spChanged);
    }


    public void updateTheme() {
        if (WAApp.getApp().getWaPreference().getTheme() == Constants.THEME_BLUE) {
            setTheme(R.style.AppTheme_SkyBlue);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
        } else if (WAApp.getApp().getWaPreference().getTheme() == Constants.THEME_RED) {
            setTheme(R.style.AppTheme_Settings);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
        } else if (WAApp.getApp().getWaPreference().getTheme() == Constants.THEME_GREEN) {
            setTheme(R.style.AppTheme_WhatsappGreen);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onThemeChange() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy called");
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(spChanged);
    }

    SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
            SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                      String key) {
                    if (key.equalsIgnoreCase("key_select_theme")) {
                        Log.e(TAG,"Recreate called ");
                        HomeActivity.isThemeChanged = true;
//                        reloadActivity();
                        Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                    } else if (key.equalsIgnoreCase(getString(R.string.notifications_new_message))) {
                        if (WAApp.getApp().getWaPreference().getDailyNotification()) {
                            sendSuccessNotification();
                            Intent myIntent = new Intent(SettingsActivity.this, NotificationReceiver.class);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, 8);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
                        } else {
                            Intent myIntent = new Intent(SettingsActivity.this, NotificationReceiver.class);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pendingIntent);
                        }
                    }
                }
            };

    private void sendSuccessNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Whatsapp Status Saver");
        builder.setContentText("Your daily notification activated.");
        builder.setSmallIcon(R.drawable.notification_icon);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String strRingtonePreference = sharedPreferences.getString("key_notifications_new_message_ringtone", "DEFAULT_SOUND");
        Uri defaultSoundUri = Uri.parse(strRingtonePreference);
        builder.setSound(defaultSoundUri);
        if (WAApp.getApp().getWaPreference().getVibrateNotification()) {
            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000});
        }
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(100, notificationCompat);
    }

    public void reloadActivity() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        intent.putExtra("isThemeChanged", true);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (HomeActivity.isThemeChanged) {
            setResult(AppCompatPreferenceActivity.RESULT_OK);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
