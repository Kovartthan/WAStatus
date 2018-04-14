package com.ko.wastatus.settings;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.text.TextUtils;

import com.ko.wastatus.R;
import com.ko.wastatus.WAApp;

public class MainPreferenceFragment extends PreferenceFragment {
    public OnThemeChangedListener onThemeChangedListener;

    public interface OnThemeChangedListener {
        void onThemeChange();
    }

    public void setOnThemeChangedListener(OnThemeChangedListener onThemeChangedListener) {
        this.onThemeChangedListener = onThemeChangedListener;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preferences);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_notifications_new_message_ringtone)));
        bindVideoQualityPreferenceSummaryToValue(findPreference(getString(R.string.key_video_thumbnail_quality)));
        bindListPreferenceToValue(findPreference(getString(R.string.key_select_theme)));
        bindDailyNotification(findPreference(getString(R.string.notifications_new_message)));
        bindVibrateNotification(findPreference(getString(R.string.key_vibrate)));
    }

    private void bindDailyNotification(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getBoolean(preference.getKey(), false));
    }

    private void bindVibrateNotification(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getBoolean(preference.getKey(), false));
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private void bindVideoQualityPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getBoolean(preference.getKey(), false));
    }

    private void bindListPreferenceToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }


    private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
                WAApp.getApp().getWaPreference().setTheme(index);
            } else if (preference instanceof RingtonePreference) {
                if (TextUtils.isEmpty(stringValue)) {
                    preference.setSummary(R.string.pref_ringtone_silent);
                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));
                    if (ringtone == null) {
                        preference.setSummary(R.string.summary_choose_ringtone);
                    } else {
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }
            } else if (preference instanceof CheckBoxPreference) {
                boolean isChecked = Boolean.valueOf(newValue.toString());
                WAApp.getApp().getWaPreference().setVideoQualityOption(isChecked);
            } else if (preference instanceof SwitchPreference) {
                if (preference.getKey().equalsIgnoreCase("notifications_new_message")) {
                    boolean isChecked = Boolean.valueOf(newValue.toString());
                    WAApp.getApp().getWaPreference().setValueForDailyNotification(isChecked);
                } else {
                    boolean isChecked = Boolean.valueOf(newValue.toString());
                    WAApp.getApp().getWaPreference().setVibrateNotification(isChecked);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };


}
