package com.ko.statussaver.base;


import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.ko.statussaver.R;
import com.ko.statussaver.WAApp;
import com.ko.statussaver.utils.Constants;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTheme();
        setImageOptimisationByRam();
    }

    private void setImageOptimisationByRam() {
        if(!WAApp.getApp().getWaPreference().getAppFirstTimeOpen()) {
            try {
                ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                activityManager.getMemoryInfo(mi);
                if (mi.totalMem > 1677721600) {
                    WAApp.getApp().getWaPreference().setVideoQualityOption(true);
                } else {
                    WAApp.getApp().getWaPreference().setVideoQualityOption(false);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void updateTheme() {
        try {
            if (WAApp.getApp().getWaPreference().getTheme() == Constants.THEME_BLUE) {
                setTheme(R.style.AppTheme_SkyBlue_Home);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                }
            } else if (WAApp.getApp().getWaPreference().getTheme() == Constants.THEME_RED) {
                setTheme(R.style.AppTheme_Home);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                }
            } else if (WAApp.getApp().getWaPreference().getTheme() == Constants.THEME_GREEN) {
                setTheme(R.style.AppTheme_WhatsappGreen_Home);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}