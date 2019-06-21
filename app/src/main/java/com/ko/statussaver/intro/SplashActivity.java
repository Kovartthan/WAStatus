package com.ko.statussaver.intro;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ko.statussaver.R;
import com.ko.statussaver.WAApp;
import com.ko.statussaver.base.BaseActivity;
import com.ko.statussaver.home.activities.HomeActivity;
import com.ko.statussaver.utils.Constants;

public class SplashActivity extends BaseActivity {
    private static int SPLASH_TIME_OUT = 1500;
    private int currentTheme = 0;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTheme();
        setContentView(R.layout.activity_splash_sreen);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        setBackgroundTheme();
        setTransparentStatusBar(this);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (WAApp.getApp().getWaPreference().getSocialLogin()) {
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    SplashActivity.this.finish();
                    return;
                }
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                SplashActivity.this.finish();
            }
        }, (long) SPLASH_TIME_OUT);
    }

    public void updateTheme() {
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
        currentTheme = WAApp.getApp().getWaPreference().getTheme();
    }

    public void setBackgroundTheme() {
        if (currentTheme == Constants.THEME_BLUE) {
//            findViewById(R.id.layout_splash).setBackgroundColor(getResources().getColor(R.color.blue_colour));
            findViewById(R.id.layout_splash).setBackgroundResource(R.drawable.blue_gradient);
        } else if (currentTheme == Constants.THEME_GREEN) {
//            findViewById(R.id.layout_splash).setBackgroundColor(getResources().getColor(R.color.green_colour));
            findViewById(R.id.layout_splash).setBackgroundResource(R.drawable.green_gradient);
        } else {
//            findViewById(R.id.layout_splash).setBackgroundColor(getResources().getColor(R.color.red_500));
            findViewById(R.id.layout_splash).setBackgroundResource(R.drawable.red_gradient);
        }
    }

    public void setTransparentStatusBar(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
