package com.ko.wastatus.home;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.ko.wastatus.R;
import com.ko.wastatus.model.FileDetail;

import java.io.File;
import java.util.ArrayList;

import static com.ko.wastatus.utils.Constants.WHATSAPP_STATUSES_LOCATION;

public class HomeActivity extends AppCompatActivity {
    private ArrayList<FileDetail> fileDetailArrayList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        setupDefault();
        setupEvents();
    }

    private void init() {
        fileDetailArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_list_file);
    }

    private void setupDefault() {
        checkStorageAndGetFiles();
    }

    private void checkStorageAndGetFiles() {
        File directory = new File(Environment.getDataDirectory().toString() + WHATSAPP_STATUSES_LOCATION);
        if(directory.exists()){
            fileDetailArrayList = getListFiles(new File(Environment.getDataDirectory().toString() + WHATSAPP_STATUSES_LOCATION));
            return;
        }
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();
        if (isSDSupportedDevice && isSDPresent) {
            fileDetailArrayList = getListFiles(new File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_STATUSES_LOCATION));
        }else{
            Toast.makeText(this,"Unable to get your status",Toast.LENGTH_SHORT).show();
        }
    }

    private void setupEvents() {

    }

    private ArrayList<FileDetail> getListFiles(File parentDir) {
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!fileDetailArrayList.contains(file)) {
                    if (file.getName().endsWith(".gif")) {
                        fileDetailArrayList.add(new FileDetail(1, file));
                    }
                    if (file.getName().endsWith(".mp4")) {
                        fileDetailArrayList.add(new FileDetail(2, file));
                    }
                    if (file.getName().endsWith(".jpg")) {
                        fileDetailArrayList.add(new FileDetail(3, file));
                    }
                }
            }
        }
        return fileDetailArrayList;
    }
}
