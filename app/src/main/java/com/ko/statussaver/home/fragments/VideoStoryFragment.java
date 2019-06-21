package com.ko.statussaver.home.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.ko.statussaver.R;
import com.ko.statussaver.WAApp;
import com.ko.statussaver.home.OnActionListener;
import com.ko.statussaver.home.adapters.VideoStoryListAdapter;
import com.ko.statussaver.model.FileDetail;
import com.ko.statussaver.tasks.SaveFilesToCardAsyncTask;
import com.ko.statussaver.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static com.ko.statussaver.utils.Constants.WHATSAPP_STATUSES_LOCATION;

public class VideoStoryFragment extends Fragment implements OnActionListener {
    private ArrayList<FileDetail> fileDetailArrayList;
    private RecyclerView recyclerView;
    private VideoStoryListAdapter videoStoryListAdapter;
    private ProgressDialog progressDialog;
    private LinearLayout errorMessage;
    private boolean isPause;
    private TextView errText;

    public VideoStoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_stories, container, false);
        init(rootView);
        setupDefault();
        setupEvents();
        return rootView;
    }

    @Override
    public void onShowCaseView(Context context, RecyclerView.ViewHolder viewHolder, int position) {

    }

    private void init(View rootView) {
        fileDetailArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list_file);
        errorMessage = (LinearLayout) rootView.findViewById(R.id.layout_error);
        errText = ((TextView) rootView.findViewById(R.id.error_txt));
        errText.setText(getString(R.string.err_msg_two));
        imgError = rootView.findViewById(R.id.img_err);
    }

    private void setupDefault() {
        changeTheme();
        checkStorageAndGetFiles();
        videoStoryListAdapter = new VideoStoryListAdapter(getActivity(), fileDetailArrayList, false);
        videoStoryListAdapter.setOnActionListener(VideoStoryFragment.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(videoStoryListAdapter);
    }

    public void checkStorageAndGetFiles() {
        File directory = new File(Environment.getExternalStorageDirectory() + WHATSAPP_STATUSES_LOCATION);
        if (directory.exists()) {
            getListFiles(directory);
        }
        if (fileDetailArrayList != null && fileDetailArrayList.size() == 0) {
            recyclerView.setVisibility(View.INVISIBLE);
            errorMessage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            errorMessage.setVisibility(View.INVISIBLE);
        }
    }

    private void setupEvents() {

    }

    private void getListFiles(File parentDir) {
        File[] files;
        files = parentDir.listFiles();
        if (files != null && files.length > 1) {
            Arrays.sort( files, new Comparator()
            {
                public int compare(Object o1, Object o2) {

                    if (((File)o1).lastModified() > ((File)o2).lastModified()) {
                        return -1;
                    } else if (((File)o1).lastModified() < ((File)o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }

            });
        }
        if (files != null) {
            for (File file : files) {
                if (!fileDetailArrayList.contains(file)) {
                    if (file.getName().endsWith(".mp4")) {
                        fileDetailArrayList.add(new FileDetail(2, file, null));
                    }
                }
            }
        }
    }

    @Override
    public void onShareClick(FileDetail fileDetail) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("video/*");
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory() + Constants.WHATSAPP_STATUSES_LOCATION + fileDetail.file.getName()));
        share.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(share, ""));
    }

    @Override
    public void onPlayClick(FileDetail fileDetail) {
        Intent mVideoWatch = new Intent(Intent.ACTION_VIEW);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "video_url");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, fileDetail.file.getAbsolutePath());
        FirebaseAnalytics.getInstance(getActivity()).logEvent("video", bundle);
        mVideoWatch.setDataAndType(Uri.parse(fileDetail.file.getAbsolutePath()), "video/*");
        startActivity(mVideoWatch);
    }

    @Override
    public void onUploadClick(FileDetail fileDetail, boolean isSavedStory, int position) {
        progressDialog = ProgressDialog.show(getActivity(), "", "Saving your status Please wait...", true);
        new SaveFilesToCardAsyncTask(getActivity(), position, progressDialog).execute(fileDetail);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.RC_STORAGE_CODE) {
            checkStorageAndGetFiles();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPause) {
            fileDetailArrayList.clear();
            checkStorageAndGetFiles();
            videoStoryListAdapter.notifyDataSetChanged();
            isPause = false;
        }
    }

    public void refreshFragment() {
        if (fileDetailArrayList != null) {
            fileDetailArrayList.clear();
            checkStorageAndGetFiles();
            videoStoryListAdapter.notifyDataSetChanged();
        }
    }

    private ImageView imgError;

    public void changeTheme() {
        if (WAApp.getApp().getWaPreference().getTheme() == Constants.THEME_BLUE) {
            imgError.setImageResource(R.drawable.ic_error_outline_blue_48dp);
            errText.setTextColor(getResources().getColor(R.color.blue_colour));
        } else if (WAApp.getApp().getWaPreference().getTheme() == Constants.THEME_RED) {
            imgError.setImageResource(R.drawable.ic_error_outline_red_500_48dp);
            errText.setTextColor(getResources().getColor(R.color.red_500));
        } else if (WAApp.getApp().getWaPreference().getTheme() == Constants.THEME_GREEN) {
            imgError.setImageResource(R.drawable.ic_error_outline_green_48dp);
            errText.setTextColor(getResources().getColor(R.color.green_colour));
        }
    }

    private class SetDataForVideo extends AsyncTask<Void, Void, Void> {
        File[] files;
        Context context;
        ProgressDialog progressDialog;

        public SetDataForVideo(File[] files, Context context) {
            this.files = files;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading your status");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            videoStoryListAdapter = new VideoStoryListAdapter(getActivity(), fileDetailArrayList, false);
            videoStoryListAdapter.setOnActionListener(VideoStoryFragment.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(videoStoryListAdapter);
        }
    }
}