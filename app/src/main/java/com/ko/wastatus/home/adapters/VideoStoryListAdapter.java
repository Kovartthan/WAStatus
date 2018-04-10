package com.ko.wastatus.home.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ko.wastatus.R;
import com.ko.wastatus.WAApp;
import com.ko.wastatus.home.OnActionListener;
import com.ko.wastatus.model.FileDetail;
import com.ko.wastatus.utils.Constants;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

public class VideoStoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<FileDetail> fileDetailArrayList;
    private Context context;
    private OnActionListener onActionListener;
    private boolean isSavedStories;
    private CheckBoxPreference checkBoxPreference;
    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    public VideoStoryListAdapter(Context context, ArrayList<FileDetail> fileDetailArrayList, boolean isSavedStories) {
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
        Bitmap bitmap = null;
        if(WAApp.getApp().getWaPreference().getVideoQualityOption()){
            Log.e("Settings","high");
            bitmap = ThumbnailUtils.createVideoThumbnail(fileDetail.file.getAbsolutePath(), MediaStore.Images.Thumbnails.MINI_KIND);
            ((FileTypeHolder) holder).imgPhoto.setImageBitmap(bitmap);
        }else{
            Log.e("Settings","low");
            bitmap = ThumbnailUtils.createVideoThumbnail(fileDetail.file.getAbsolutePath(), MediaStore.Images.Thumbnails.MICRO_KIND);
            Blurry.with(context).from(bitmap).into(((FileTypeHolder) holder).imgPhoto);
        }
        ((FileTypeHolder) holder).imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActionListener.onPlayClick(fileDetail);
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
            imgPlay.setVisibility(View.VISIBLE);
            checkAndChangeIcon();
        }

        private void checkAndChangeIcon() {
            if (WAApp.getApp().getWaPreference().getTheme() == Constants.THEME_BLUE) {
                imgPlay.setImageResource(R.drawable.ic_play_circle_outline_blue_48dp);
                imgShare.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_share_blue_24dp, 0, 0, 0);
                imgUpload.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_download_blue_24dp, 0, 0, 0);
            } else if (WAApp.getApp().getWaPreference().getTheme() == Constants.THEME_RED) {
                imgPlay.setImageResource(R.drawable.ic_play_circle_outline_red_500_48dp);
                imgShare.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_share_red_500_24dp, 0, 0, 0);
                imgUpload.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_download_red_500_24dp, 0, 0, 0);
            } else if (WAApp.getApp().getWaPreference().getTheme() == Constants.THEME_GREEN) {
                imgPlay.setImageResource(R.drawable.ic_play_circle_outline_green_48dp);
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