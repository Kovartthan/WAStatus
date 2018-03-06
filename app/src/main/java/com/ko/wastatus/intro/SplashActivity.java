package com.ko.wastatus.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ko.wastatus.R;
import com.ko.wastatus.WAApp;
import com.ko.wastatus.home.activities.HomeActivity;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sreen);
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
}
