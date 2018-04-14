package com.ko.wastatus.home.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ko.wastatus.R;
import com.ko.wastatus.WAApp;
import com.ko.wastatus.home.OnActionListener;
import com.ko.wastatus.home.activities.ImageViewActivity;
import com.ko.wastatus.model.FileDetail;
import com.ko.wastatus.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageStoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<FileDetail> fileDetailArrayList;
    private Context context;
    private OnActionListener onActionListener;
    private boolean isSavedStories;

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    public ImageStoryListAdapter(Context context, ArrayList<FileDetail> fileDetailArrayList, boolean isSavedStories) {
        this.context = context;
        this.fileDetailArrayList = fileDetailArrayList;
        this.isSavedStories = isSavedStories;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_item, parent, false);
        return new FileTypeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final FileDetail fileDetail = fileDetailArrayList.get(position);
        Picasso.get().load(fileDetail.file).resize(300,300).into(((FileTypeHolder) holder).imgPhoto);
        ((FileTypeHolder) holder).imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ImageViewActivity.class).putExtra(Constants.PHOTO_PATH, fileDetail.file.getAbsolutePath()));
            }
        });
        ((FileTypeHolder) holder).imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActionListener.onShareClick(fileDetail);
            }
        });
        ((FileTypeHolder) holder).imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActionListener.onUploadClick(fileDetail, isSavedStories, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileDetailArrayList.size();
    }

    public class FileTypeHolder extends RecyclerView.ViewHolder {
        public ImageView imgPlay, imgPhoto;
        public TextView imgShare, imgUpload;

        public FileTypeHolder(View itemView) {
            super(itemView);
            imgShare = (TextView) itemView.findViewById(R.id.img_share);
            imgUpload = (TextView) itemView.findViewById(R.id.img_upload);
            imgPlay = (ImageView) itemView.findViewById(R.id.img_play);
            imgPhoto = (ImageView) itemView.findViewById(R.id.img_photo);
            imgPlay.setVisibility(View.INVISIBLE);
            checkAndChangeIcon();
        }

        private void checkAndChangeIcon() {
            if (WAApp.getApp().getWaPreference().getTheme() == Constants.THEME_BLUE) {
                imgShare.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_share_blue_24dp, 0, 0, 0);
                imgUpload.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_download_blue_24dp, 0, 0, 0);
            } else if (WAApp.getApp().getWaPreference().getTheme() == Constants.THEME_RED) {
                imgShare.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_share_red_500_24dp, 0, 0, 0);
                imgUpload.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_download_red_500_24dp, 0, 0, 0);
            } else if (WAApp.getApp().getWaPreference().getTheme() == Constants.THEME_GREEN) {
                imgShare.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_share_green_24dp, 0, 0, 0);
                imgUpload.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_download_green_24dp, 0, 0, 0);

            }
        }

    }

    public void removeItem(int position) {
        fileDetailArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, fileDetailArrayList.size());
    }

    public int getArraySize() {
        return fileDetailArrayList.size();
    }
}
