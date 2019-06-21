package com.ko.statussaver.home.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ko.statussaver.R;
import com.ko.statussaver.WAApp;
import com.ko.statussaver.home.OnActionListener;
import com.ko.statussaver.model.FileDetail;
import com.ko.statussaver.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class VideoStoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<FileDetail> fileDetailArrayList;
    private Context context;
    private OnActionListener onActionListener;
    private boolean isSavedStories;
    private CheckBoxPreference checkBoxPreference;
    private RequestOptions options;
    private SimpleDateFormat simpleDateFormat;
    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    public VideoStoryListAdapter(Context context, ArrayList<FileDetail> fileDetailArrayList, boolean isSavedStories) {
        this.context = context;
        this.fileDetailArrayList = fileDetailArrayList;
        this.isSavedStories = isSavedStories;
        long interval = 3000 * 1000;
        options = new RequestOptions().frame(interval);
        simpleDateFormat= new SimpleDateFormat("dd-MMM-yyyy hh:mm aaa", Locale.ENGLISH);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_item, parent, false);
        return new FileTypeHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final FileDetail fileDetail = fileDetailArrayList.get(position);
        if (WAApp.getApp().getWaPreference().getVideoQualityOption()) {
            Glide.with(context).asBitmap().load(Uri.fromFile(fileDetail.file)).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    ((FileTypeHolder) holder).txtLoading.setVisibility(View.GONE);
                    ((FileTypeHolder) holder).imgPlay.setVisibility(View.VISIBLE);
                    return false;
                }
                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    ((FileTypeHolder) holder).txtLoading.setVisibility(View.INVISIBLE);
                    ((FileTypeHolder) holder).imgPlay.setVisibility(View.VISIBLE);
                    return false;
                }
            }).apply(options).into(((FileTypeHolder) holder).imgPhoto);

        } else {
            Glide.with(context).asBitmap().load(Uri.fromFile(fileDetail.file)).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    ((FileTypeHolder) holder).txtLoading.setVisibility(View.GONE);
                    ((FileTypeHolder) holder).imgPlay.setVisibility(View.VISIBLE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    ((FileTypeHolder) holder).txtLoading.setVisibility(View.INVISIBLE);
                    ((FileTypeHolder) holder).imgPlay.setVisibility(View.VISIBLE);
                    return false;
                }
            }).apply(options.override(400,400)).into(((FileTypeHolder) holder).imgPhoto);
        }
        ((FileTypeHolder) holder).txtPostedDate.setText(context.getString(R.string.saved_on)+" "+simpleDateFormat.format(new Date(fileDetail.file.lastModified())));
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
        public TextView imgShare, imgUpload, txtLoading,txtPostedDate;

        public FileTypeHolder(View itemView) {
            super(itemView);
            imgShare = (TextView) itemView.findViewById(R.id.img_share);
            imgUpload = (TextView) itemView.findViewById(R.id.img_upload);
            imgPlay = (ImageView) itemView.findViewById(R.id.img_play);
            imgPhoto = (ImageView) itemView.findViewById(R.id.img_photo);
            txtLoading = itemView.findViewById(R.id.txt_loading);
            txtPostedDate = itemView.findViewById(R.id.txt_date);
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