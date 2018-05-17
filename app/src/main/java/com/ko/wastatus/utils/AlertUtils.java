package com.ko.wastatus.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;


public class AlertUtils {


    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showCommonAlertDialogWithPositive(Context context, String title, String message, String positiveButtonText, DialogInterface.OnClickListener positiveClick) {
        if (context != null && !((Activity) context).isFinishing()) {
            new android.app.AlertDialog.Builder(context).setTitle(title).setMessage(message).
                    setPositiveButton(positiveButtonText, positiveClick).
                    setCancelable(false)
                    .show();
        }
    }
}
