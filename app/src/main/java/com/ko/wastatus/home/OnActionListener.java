package com.ko.wastatus.home;

import com.ko.wastatus.model.FileDetail;

/**
 * Created by admin on 2/8/2018.
 */

public  interface OnActionListener {
    void onShareClick(FileDetail fileDetail);
    void onPlayClick(FileDetail  fileDetail);
    void onUploadClick(FileDetail fileDetail,boolean isSavedStory,int positon);
}
