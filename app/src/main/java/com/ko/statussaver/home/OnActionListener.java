package com.ko.statussaver.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.ko.statussaver.model.FileDetail;

/**
 * Created by admin on 2/8/2018.
 */

public  interface OnActionListener {
    void onShareClick(FileDetail fileDetail);
    void onPlayClick(FileDetail  fileDetail);
    void onUploadClick(FileDetail fileDetail,boolean isSavedStory,int positon);
    void onShowCaseView(Context context, RecyclerView.ViewHolder viewHolder,int position);
}
