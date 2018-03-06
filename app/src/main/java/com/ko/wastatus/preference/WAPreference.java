package com.ko.wastatus.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.ko.wastatus.R;

public class WAPreference {
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    private Context mContext;

    private static String PREF_NAME = "WAPreference";
    private static String PREF_IS_SOCIAL_LOGIN = "pref_is_social_login";
    ;
    private static String PREF_USER_NAME = "pref_user_name";
    private static String PREF_THEME = "pref_theme";

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


    public void clearPrefernces() {
        mEditor.clear();
        mEditor.commit();
    }
}

