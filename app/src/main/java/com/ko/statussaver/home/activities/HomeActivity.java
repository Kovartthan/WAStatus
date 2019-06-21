package com.ko.statussaver.home.activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ko.statussaver.OnShowCaseListener;
import com.ko.statussaver.R;
import com.ko.statussaver.WAApp;
import com.ko.statussaver.about.AboutActivity;
import com.ko.statussaver.base.BaseActivity;
import com.ko.statussaver.home.adapters.ImageStoryListAdapter;
import com.ko.statussaver.home.adapters.TabsPagerAdapter;
import com.ko.statussaver.home.fragments.ImageStoryFragment;
import com.ko.statussaver.home.fragments.SavedStoriesFragment;
import com.ko.statussaver.home.fragments.VideoStoryFragment;
import com.ko.statussaver.notification.NetworkChangeReceiver;
import com.ko.statussaver.notification.NotificationJobSchedulerService;
import com.ko.statussaver.settings.SettingsActivity;
import com.ko.statussaver.utils.Constants;
import com.ko.statussaver.utils.FileUtils;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.shape.RectangleShape;

import static com.ko.statussaver.utils.Constants.RC_CHANGE_THEME;

public class HomeActivity extends BaseActivity implements OnShowCaseListener {
    private static final String SHOWCASE_ID = "101";
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ImageStoryFragment imageStoryFragment;
    private SavedStoriesFragment savedStoriesFragment;
    private VideoStoryFragment videoStoryFragment;
    public static boolean isThemeChanged = false;
    private View menu;
    public boolean isShowCaseClicked;
    private int position;
    private RecyclerView.ViewHolder viewHolder;
    private Context context;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isThemeChanged) {
            switch (requestCode) {
                case RC_CHANGE_THEME:
//                    reloadActivity();
                    overridePendingTransition(0, 0);
                    recreate();
                    overridePendingTransition(0, 0);
                    isThemeChanged = false;
                    break;
            }
        }
    }

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
                } else if (position == 1 && videoStoryFragment != null) {
                    videoStoryFragment.refreshFragment();
                } else if (position == 2 && savedStoriesFragment != null) {
                    savedStoriesFragment.refreshFragment();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setupDefault() {
        imageStoryFragment = new ImageStoryFragment();
        imageStoryFragment.setOnShowCaseListener(this);
        videoStoryFragment = new VideoStoryFragment();
        savedStoriesFragment = new SavedStoriesFragment();
        mAdapter.addFragment(imageStoryFragment, "Images");
        mAdapter.addFragment(videoStoryFragment, "Videos");
        mAdapter.addFragment(savedStoriesFragment, "Saved");
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
        WAApp.getApp().getWaPreference().setSocialLogin(true);
        if (!WAApp.getApp().getWaPreference().getAppFirstTimeOpen()) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ComponentName serviceComponent = new ComponentName(this, NotificationJobSchedulerService.class);
                JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
                builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
                JobScheduler jobScheduler = this.getSystemService(JobScheduler.class);
                jobScheduler.schedule(builder.build());
            } else {
                registerReceiver(new NetworkChangeReceiver(), intentFilter);
            }
        }
        WAApp.getApp().getWaPreference().setAppFirstTimeOpen(true);
        createDirectory();
    }

    private void createDirectory() {
//        FileUtils.createDirIfNotExists(Constants.WA_SAVER_DIRECTORY_LOCATION);
        FileUtils.createDirIfNotExists(Constants.IMAGE_DIRECTORY_LOCATION);
        FileUtils.createDirIfNotExists(Constants.VIDEO_DIRECTORY_LOCATION);
    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Stories");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (imageStoryFragment.fileDetailArrayList != null && imageStoryFragment.fileDetailArrayList.size() > 0) {
            showShowcaseView();
        }
        return true;
    }

    private void showShowcaseView() {
        if(viewHolder == null)
            return;
        try {
            ShowcaseConfig config = new ShowcaseConfig();
            config.setDelay(200);
            MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
            sequence.setConfig(config);
            sequence.addSequenceItem(toolbar.getChildAt(1),
                    "Explore Menu options", "Next");
            sequence.start();
            sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
                @Override
                public void onDismiss(MaterialShowcaseView materialShowcaseView, int i) {
                    ShowcaseConfig config = new ShowcaseConfig();
                    config.setDelay(500);
                    MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(((HomeActivity) context), "" + position);
                    sequence.setConfig(config);
                    sequence.addSequenceItem(((ImageStoryListAdapter.FileTypeHolder) viewHolder).imgUpload,
                            "Click this to save the photos", "Next");
                    sequence.addSequenceItem(((ImageStoryListAdapter.FileTypeHolder) viewHolder).imgShare,
                            "Click this to share the photos in whatsapp", "Next");
                    sequence.start();
                    sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
                        @Override
                        public void onDismiss(MaterialShowcaseView materialShowcaseView, int i) {
                            if (i == 1) {
                                ShowcaseConfig config = new ShowcaseConfig();
                                config.setDelay(500);
                                config.setShape(new RectangleShape(200, 200));
                                MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(((HomeActivity) context), "" + position + 1);
                                sequence.setConfig(config);
                                sequence.addSequenceItem(((ImageStoryListAdapter.FileTypeHolder) viewHolder).imgPhoto,
                                        "Click photo to view in full screen", "Done");
                                sequence.start();
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                finish();
                break;
            case R.id.settings_exit:
                startActivityForResult(new Intent(HomeActivity.this, SettingsActivity.class), RC_CHANGE_THEME);
                break;
            case R.id.about:
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
                break;
            case R.id.feedback:
                sendFeedBack();
                break;
        }
        return false;
    }

    private void sendFeedBack() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"kovartthan@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Feedback about Whatsapp status saver");
        try {
            startActivity(Intent.createChooser(i, "Send mail"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Error !You have no email client in your mobile", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void performShowCaseView(final Context context, final RecyclerView.ViewHolder viewHolder, final int position) {
        this.context = context;
        this.viewHolder = viewHolder;
        this.position = position;
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
