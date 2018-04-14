package com.ko.wastatus.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import com.ko.wastatus.model.FileDetail;
import com.ko.wastatus.utils.Constants;
import com.ko.wastatus.utils.FileUtils;

import java.io.File;

public class SaveFilesToCardAsyncTask extends AsyncTask<FileDetail, Void, String> {
        private String result = "";
        private FileDetail fileDetail;
        int position;
        private Context context;
        private ProgressDialog progressDialog;
        public SaveFilesToCardAsyncTask(Context context, int position, ProgressDialog progressDialog){
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
            if (params[0].fileType == 1 || params[0].fileType == 3)
                try {
                    FileUtils.copyFile(params[0].file.getAbsolutePath(), params[0].file.getName(), Environment.getExternalStorageDirectory() + Constants.IMAGE_DIRECTORY_LOCATION);
                } catch (Exception e) {
                    result = "error";
                }
            if (params[0].fileType == 2)
                try {
                    FileUtils.copyFile(params[0].file.getAbsolutePath(), params[0].file.getName(), Environment.getExternalStorageDirectory() + Constants.VIDEO_DIRECTORY_LOCATION);
                } catch (Exception e) {
                    result = "error";
                }
            return "";
        }

        @Override
        protected void onPostExecute(final String result) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    if (result.equalsIgnoreCase("error")) {
                        Toast.makeText(context, "Error in saving file !", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                        {
                            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            File f = new File("file://"+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
                            Uri contentUri = Uri.fromFile(f);
                            mediaScanIntent.setData(contentUri);
                            context.sendBroadcast(mediaScanIntent);
                        }
                        else
                        {
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                        }
                        Toast.makeText(context, "Status saved successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 1500);
            this.fileDetail = null;
        }
    }
