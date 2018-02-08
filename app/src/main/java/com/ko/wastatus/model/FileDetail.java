package com.ko.wastatus.model;

import java.io.File;

/**
 * Created by admin on 2/8/2018.
 */

public class FileDetail {
    public static int TYPE_IMAGE = 3;
    public static int TYPE_VIDEO = 2;
    public static int TYPE_GIF = 1;
    public int fileType;
    public File file;

    public FileDetail(int fileType, File file) {
        this.fileType = fileType;
        this.file = file;
    }
}
