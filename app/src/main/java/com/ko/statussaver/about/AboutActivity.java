package com.ko.statussaver.about;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.ko.statussaver.BuildConfig;
import com.ko.statussaver.R;
import com.ko.statussaver.base.BaseActivity;

public class AboutActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView txtVersionName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_menu_white_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setVersionName();
    }

    private void setVersionName() {
        txtVersionName = findViewById(R.id.txt_version_name);
        String versionName = BuildConfig.VERSION_NAME;
        txtVersionName.setText(versionName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
