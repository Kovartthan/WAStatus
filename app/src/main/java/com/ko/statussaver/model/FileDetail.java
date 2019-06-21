package com.ko.statussaver.model;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by admin on 2/8/2018.
 */

public class FileDetail {
    public static int TYPE_SUGGESTION = 2;
    public static int TYPE_ITEM = 1;
    public int fileType;
    public File file;
    public Bitmap bitmap;
    public int viewType;
    public String fileName;
    public FileDetail(int fileType, File file, Bitmap bitmap) {
        this.fileType = fileType;
        this.file = file;
        this.bitmap = bitmap;
    }

    public FileDetail(int fileType, File file, Bitmap bitmap, int viewType) {
        this.fileType = fileType;
        this.file = file;
        this.bitmap = bitmap;
        this.viewType = viewType;
    }

    public FileDetail() {

    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
