package com.ko.wastatus.home.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ko.wastatus.R;
import com.ko.wastatus.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {
    private ImageView imgClose, imgPhoto;
    private String photoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        Toast.makeText(this,"Pinch to zoom",Toast.LENGTH_SHORT).show();
    }

    private void setupDefaults() {
        if (getIntent().hasExtra(Constants.PHOTO_PATH)) {
            photoPath = getIntent().getStringExtra(Constants.PHOTO_PATH);
        }
        Picasso.with(this).load(new File(photoPath)).into(imgPhoto);
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
