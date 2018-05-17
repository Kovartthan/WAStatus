package com.ko.wastatus.home.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ko.wastatus.OnShowCaseListener;
import com.ko.wastatus.R;
import com.ko.wastatus.WAApp;
import com.ko.wastatus.home.OnActionListener;
import com.ko.wastatus.home.adapters.ImageStoryListAdapter;
import com.ko.wastatus.model.FileDetail;
import com.ko.wastatus.tasks.SaveFilesToCardAsyncTask;
import com.ko.wastatus.utils.Constants;

import java.io.File;
import java.util.ArrayList;

import static com.ko.wastatus.utils.Constants.WHATSAPP_STATUSES_LOCATION;

public class ImageStoryFragment extends Fragment implements OnActionListener {
    public ArrayList<FileDetail> fileDetailArrayList;
    private RecyclerView recyclerView;
    private ImageStoryListAdapter imageStoryListAdapter;
    private ProgressDialog progressDialog;
    private LinearLayout errorMessage;
    private boolean isPause;
    private TextView errText;
    private OnShowCaseListener onShowCaseListener;

    public ImageStoryFragment() {
        // Required empty public constructor
    }

    public void setOnShowCaseListener(OnShowCaseListener onShowCaseListener) {
        this.onShowCaseListener = onShowCaseListener;
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

    private void init(View rootView) {
        fileDetailArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list_file);
        imgError = rootView.findViewById(R.id.img_err);
        errorMessage = (LinearLayout) rootView.findViewById(R.id.layout_error);
        errText = ((TextView) rootView.findViewById(R.id.error_txt));
        errText.setText(getString(R.string.err_msg_two));
    }

    private void setupDefault() {
        changeTheme();
        checkStorageAndGetFiles();
        imageStoryListAdapter = new ImageStoryListAdapter(getActivity(), fileDetailArrayList, false);
        imageStoryListAdapter.setOnActionListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(imageStoryListAdapter);
    }

    public void checkStorageAndGetFiles() {

        File directory = new File(Environment.getExternalStorageDirectory() + WHATSAPP_STATUSES_LOCATION);

        if (directory.exists()) {
            fileDetailArrayList = getListFiles(new File(Environment.getExternalStorageDirectory() + WHATSAPP_STATUSES_LOCATION));
            insertFilesToDatabase(new File(Environment.getExternalStorageDirectory() + WHATSAPP_STATUSES_LOCATION));
        }

        if (fileDetailArrayList.size() == 0) {
            recyclerView.setVisibility(View.INVISIBLE);
            errorMessage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            errorMessage.setVisibility(View.INVISIBLE);
        }
    }

    private void setupEvents() {

    }

    private ArrayList<FileDetail> getListFiles(File parentDir) {
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            recyclerView.setVisibility(View.VISIBLE);
            errorMessage.setVisibility(View.INVISIBLE);
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                addFilesToArrayList(file);
                if (files.length > 10) {
                    if (i == 3)
                        fileDetailArrayList.add(new FileDetail(1, null, null, FileDetail.TYPE_SUGGESTION));
                    else if (i == 7)
                        fileDetailArrayList.add(new FileDetail(2, null, null, FileDetail.TYPE_SUGGESTION));
//                Collections.shuffle(fileDetailArrayList);
                }
            }

        }
        return fileDetailArrayList;
    }

    private void addFilesToArrayList(File file) {
        if (!fileDetailArrayList.contains(file)) {
            if (file.getName().endsWith(".gif")) {
                fileDetailArrayList.add(new FileDetail(1, file, null, FileDetail.TYPE_ITEM));
            }
            if (file.getName().endsWith(".jpg")) {
                fileDetailArrayList.add(new FileDetail(3, file, null, FileDetail.TYPE_ITEM));
            }
        }
    }

    private void insertFilesToDatabase(File file) {
        File[] files;
        files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
              if(WAApp.getApp().getWaPreference().getDbValue()) {
                  if (!WAApp.getApp().getDatabaseHandler().checkIsInDBorNot(files[i].getName())) {
                      if (files[i].getName().endsWith(".gif")) {
                          WAApp.getApp().getDatabaseHandler().addFile(new FileDetail(1, files[i], null, FileDetail.TYPE_ITEM));
                      } else if (files[i].getName().endsWith(".jpg")) {
                          WAApp.getApp().getDatabaseHandler().addFile(new FileDetail(3, files[i], null, FileDetail.TYPE_ITEM));
                      } else if (files[i].getName().endsWith(".mp4")) {
                          WAApp.getApp().getDatabaseHandler().addFile(new FileDetail(2, files[i], null, FileDetail.TYPE_ITEM));
                      }
                  }
              }else{
                  if (files[i].getName().endsWith(".gif")) {
                      WAApp.getApp().getDatabaseHandler().addFile(new FileDetail(1, files[i], null, FileDetail.TYPE_ITEM));
                  } else if (files[i].getName().endsWith(".jpg")) {
                      WAApp.getApp().getDatabaseHandler().addFile(new FileDetail(3, files[i], null, FileDetail.TYPE_ITEM));
                  } else if (files[i].getName().endsWith(".mp4")) {
                      WAApp.getApp().getDatabaseHandler().addFile(new FileDetail(2, files[i], null, FileDetail.TYPE_ITEM));
                  }
              }
            }
            WAApp.getApp().getWaPreference().setDbValue(true);
            Log.e("Tag", "getFileCount " + WAApp.getApp().getDatabaseHandler().getFileCount());
        }
    }



    @Override
    public void onShareClick(FileDetail fileDetail) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory() + Constants.WHATSAPP_STATUSES_LOCATION + fileDetail.file.getName()));
        share.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(share, ""));
    }

    @Override
    public void onPlayClick(FileDetail fileDetail) {
        // No need
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
            imageStoryListAdapter.notifyDataSetChanged();
            isPause = false;
        }
    }


    public void refreshFragment() {
        if (fileDetailArrayList != null) {
            fileDetailArrayList.clear();
            checkStorageAndGetFiles();
            imageStoryListAdapter.notifyDataSetChanged();
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

    @Override
    public void onShowCaseView(Context context, RecyclerView.ViewHolder viewHolder, int position) {
        if (onShowCaseListener != null) {
            onShowCaseListener.performShowCaseView(context, viewHolder, position);
        }
    }
}