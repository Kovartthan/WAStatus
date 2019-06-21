package com.ko.statussaver.home.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ko.statussaver.R;
import com.ko.statussaver.base.BaseActivity;
import com.ko.statussaver.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageViewActivity extends BaseActivity {
    private ImageView imgClose, imgPhoto;
    private String photoPath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        init();
        setupDefaults();
        setupEvents();
    }

    private void init() {
        imgClose = (ImageView) findViewById(R.id.img_close);
        imgPhoto = (ImageView) findViewById(R.id.img_full_view);
    }

    private void setupDefaults() {
        if (getIntent().hasExtra(Constants.PHOTO_PATH)) {
            photoPath = getIntent().getStringExtra(Constants.PHOTO_PATH);
        }
        Picasso.get().load(new File(photoPath)).into(imgPhoto);
    }

    private void setupEvents() {
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
