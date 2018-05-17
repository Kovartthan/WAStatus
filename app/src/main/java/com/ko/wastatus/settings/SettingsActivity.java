package com.ko.wastatus.settings;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.WindowManager;

import com.ko.wastatus.R;
import com.ko.wastatus.WAApp;
import com.ko.wastatus.home.activities.HomeActivity;
import com.ko.wastatus.notification.NotificationRegister;
import com.ko.wastatus.utils.Constants;


public class SettingsActivity extends AppCompatPreferenceActivity implements MainPreferenceFragment.OnThemeChangedListener {
    private static final String TAG = SettingsActivity.class.getSimpleName();
    private MainPreferenceFragment mainPreferenceFragment;
    private NotificationRegister notificationRegister;
    public boolean isToOpenThemes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateTheme();
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        notificationRegister = new NotificationRegister(this);
        if (getIntent() != null && getIntent().hasExtra(Constants.OPEN_SETTINGS)) {
            if (getIntent().getBooleanExtra(Constants.OPEN_SETTINGS, false)) {
                HomeActivity.isThemeChanged = true;
                isToOpenThemes = true;
            }
        }
        setPreferenceFragment();
    }

    private void setPreferenceFragment() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainPreferenceFragment = new MainPreferenceFragment();
        if (isToOpenThemes) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.OPEN_SETTINGS, isToOpenThemes);
            mainPreferenceFragment.setArguments(bundle);
        }
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
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(spChanged);
    }

    SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
            SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                      String key) {
                    if (key.equalsIgnoreCase("key_select_theme")) {
                        HomeActivity.isThemeChanged = true;
                        Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                    } else if (key.equalsIgnoreCase(getString(R.string.notifications_new_message))) {
                        if (WAApp.getApp().getWaPreference().getDailyNotification()) {
                            notificationRegister.registerNotificationService();
                        } else {
                            notificationRegister.unRegisterNotificationService();
                        }
                    }
                }
            };

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
