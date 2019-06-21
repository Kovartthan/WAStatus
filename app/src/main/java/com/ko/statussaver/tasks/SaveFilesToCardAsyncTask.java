package com.ko.statussaver.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.ko.statussaver.model.FileDetail;
import com.ko.statussaver.utils.Constants;
import com.ko.statussaver.utils.FileUtils;

import java.io.File;

public class SaveFilesToCardAsyncTask extends AsyncTask<FileDetail, Void, String> {
    private String result = "";
    private FileDetail fileDetail;
    int position;
    private Context context;
    private ProgressDialog progressDialog;
    public SaveFilesToCardAsyncTask(Context context, int position, ProgressDialog progressDialog) {
        this.position = position;
        this.progressDialog = progressDialog;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(FileDetail... params) {
        this.fileDetail = params[0];
        if (params[0].fileType == 1 || params[0].fileType == 3) {
            try {
                FileUtils.copyFile(params[0].file.getAbsolutePath(), params[0].file.getName(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + Constants.IMAGE_DIRECTORY_LOCATION);
            } catch (Exception e) {
                result = "error";
            }
        }
        if (params[0].fileType == 2) {
            try {
                FileUtils.copyFile(params[0].file.getAbsolutePath(), params[0].file.getName(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + Constants.VIDEO_DIRECTORY_LOCATION);
            } catch (Exception e) {
                result = "error";
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(final String result) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                if (result.equalsIgnoreCase("error")) {
                    Toast.makeText(context, "Error in saving file !", Toast.LENGTH_LONG).show();
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "save_sd_card");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "error");
                    FirebaseAnalytics.getInstance(context).logEvent("sd_card", bundle);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        File f;
                        if (fileDetail.fileType != 2) {
                            f = new File("file://" + Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageDirectory() + Constants.IMAGE_DIRECTORY_LOCATION + fileDetail.file.getName()));
                        } else {
                            f = new File("file://" + Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageDirectory() + Constants.VIDEO_DIRECTORY_LOCATION));
                        }
                        try {
                            Uri contentUri = Uri.fromFile(f);
                            mediaScanIntent.setData(contentUri);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        context.sendBroadcast(mediaScanIntent);
                    } else {
                        if (fileDetail.fileType != 2) {
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + Constants.IMAGE_DIRECTORY_LOCATION)));
                        } else {
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + Constants.VIDEO_DIRECTORY_LOCATION)));
                        }
                    }
                   /* if (fileDetail.fileType != 2) {
                        try {
                            MediaScannerConnection.scanFile(context, new String[]{""+Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageDirectory() + Constants.IMAGE_DIRECTORY_LOCATION+fileDetail.file.getName())}, new String[]{"image/jpg"}, new MediaScannerConnection.OnScanCompletedListener() {

                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Toast.makeText(context, "Status saved to gallery", Toast.LENGTH_LONG).show();
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else{
                        try {
                            MediaScannerConnection.scanFile(context, new String[]{fileDetail.file.getPath()}, new String[]{"video/mp4"}, null);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }*/
                    Toast.makeText(context, "Status saved successfully", Toast.LENGTH_LONG).show();
                }
            }
        }, 1500);
    }
}
