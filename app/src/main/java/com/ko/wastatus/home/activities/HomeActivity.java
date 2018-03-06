package com.ko.wastatus.home.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ko.wastatus.R;
import com.ko.wastatus.WAApp;
import com.ko.wastatus.base.BaseActivity;
import com.ko.wastatus.home.adapters.TabsPagerAdapter;
import com.ko.wastatus.home.fragments.ImageStoryFragment;
import com.ko.wastatus.home.fragments.SavedStoriesFragment;
import com.ko.wastatus.home.fragments.VideoStoryFragment;
import com.ko.wastatus.settings.SettingsActivity;
import com.ko.wastatus.utils.Constants;
import com.ko.wastatus.utils.FileUtils;

public class HomeActivity extends BaseActivity {
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ImageStoryFragment imageStoryFragment;
    private SavedStoriesFragment savedStoriesFragment;
    private VideoStoryFragment videoStoryFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        setupDefault();
        setupEvents();
    }

    private void setupEvents() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0 && imageStoryFragment != null) {
                    imageStoryFragment.refreshFragment();
                } else if(position == 1 && videoStoryFragment != null){
                    videoStoryFragment.refreshFragment();
                }else if (position == 2 && savedStoriesFragment != null) {
                    savedStoriesFragment.refreshFragment();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setupDefault() {
        setSupportActionBar(toolbar);
        imageStoryFragment = new ImageStoryFragment();
        videoStoryFragment = new VideoStoryFragment();
        savedStoriesFragment = new SavedStoriesFragment();
        toolbar.setTitle("WA Status Saver");
        mAdapter.addFragment(imageStoryFragment, "Images");
        mAdapter.addFragment(videoStoryFragment,"Videos");
        mAdapter.addFragment(savedStoriesFragment, "Saved");
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
        WAApp.getApp().getWaPreference().setSocialLogin(true);
        createDirectory();
    }

    private void createDirectory() {
        FileUtils.createDirIfNotExists(Constants.WA_SAVER_DIRECTORY_LOCATION);
        FileUtils.createDirIfNotExists(Constants.IMAGE_DIRECTORY_LOCATION);
        FileUtils.createDirIfNotExists(Constants.VIDEO_DIRECTORY_LOCATION);
    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                finish();
                break;
            case R.id.settings_exit:
//                Toast.makeText(this,"Under Construction ! Coming soon",Toast.LENGTH_LONG).show();
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                break;
            case R.id.about:
                Toast.makeText(this,"Under Construction ! Coming soon",Toast.LENGTH_LONG).show();
                break;
            case R.id.feedback:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL,new String[]{"kovartthan@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT,"Feedback about Whatsapp status saver");
                try{
                    startActivity(Intent.createChooser(i,"Send mail"));
                }catch (ActivityNotFoundException e){
                    Toast.makeText(this,"Error !You have no email client in your mobile",Toast.LENGTH_LONG).show();
                }
                break;
        }
        return false;
    }
}
