package com.ko.wastatus.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by admin on 2/8/2018.
 */

public class PermissionUtils {
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(false);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Storage permission is necessary to read your whatsapp stories!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.RC_STORAGE_CODE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.RC_STORAGE_CODE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
