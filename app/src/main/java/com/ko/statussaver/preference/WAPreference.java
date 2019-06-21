package com.ko.statussaver.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.ko.statussaver.R;

public class WAPreference {
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    private Context mContext;

    private static String PREF_NAME = "WAPreference";
    private static String PREF_IS_SOCIAL_LOGIN = "pref_is_social_login";
    ;
    private static String PREF_USER_NAME = "pref_user_name";
    private static String PREF_THEME = "pref_theme";
    private static String PREF_DAILY_NOTIFICATION = "pref_daily_notification";
    private static String PREF_FIRST_TIME_OPEN = "pref_first_time_open";
    public static String PREF_NOTIFICATION_VALUE_CHANGED = "pref_notification_value_changed";
    public static String PREF_IMAGE_PREVIEW = "pref_image_preview";
    public WAPreference(Context context) {
        mContext = context;
        mPrefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }

    public void setSocialLogin(boolean value) {
        mEditor.putBoolean(PREF_IS_SOCIAL_LOGIN, value);
        mEditor.commit();
    }

    public boolean getSocialLogin() {
        return mPrefs.getBoolean(PREF_IS_SOCIAL_LOGIN, false);
    }

    public void setUserName(String name) {
        mEditor.putString(PREF_USER_NAME, name);
        mEditor.commit();
    }

    public String getUserName() {
        return mPrefs.getString(PREF_USER_NAME, "");
    }

    public void setVideoQualityOption(boolean value) {
        mEditor.putBoolean(mContext.getString(R.string.key_video_thumbnail_quality), value);
        mEditor.commit();
    }

    public boolean getVideoQualityOption() {
        return mPrefs.getBoolean(mContext.getString(R.string.key_video_thumbnail_quality), false);
    }

    public void setTheme( int theme) {
        mEditor.putInt(PREF_THEME, theme);
        mEditor.commit();
    }

    public int getTheme() {
        return mPrefs.getInt(PREF_THEME, 0);
    }

    public void setValueForDailyNotification(boolean value){
        mEditor.putBoolean(mContext.getString(R.string.notifications_new_message), value);
        mEditor.commit();
    }

    public boolean getDailyNotification(){
        return mPrefs.getBoolean(mContext.getString(R.string.notifications_new_message), true);
    }

    public void setVibrateNotification(boolean value){
        mEditor.putBoolean(mContext.getString(R.string.key_vibrate), value);
        mEditor.commit();
    }

    public boolean getVibrateNotification(){
        return mPrefs.getBoolean(mContext.getString(R.string.key_vibrate), false);
    }

    public void setAppFirstTimeOpen(boolean value){
        mEditor.putBoolean(PREF_FIRST_TIME_OPEN, value);
        mEditor.commit();
    }

    public boolean getAppFirstTimeOpen(){
        return mPrefs.getBoolean(PREF_FIRST_TIME_OPEN, false);
    }

    public void setDbValue(boolean value){
        mEditor.putBoolean(PREF_NOTIFICATION_VALUE_CHANGED, value);
        mEditor.commit();
    }

    public boolean getDbValue(){
        return mPrefs.getBoolean(PREF_NOTIFICATION_VALUE_CHANGED, false);
    }



    public void clearPrefernces() {
        mEditor.clear();
        mEditor.commit();
    }
}

