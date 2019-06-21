package com.ko.statussaver.home.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ko.statussaver.R;
import com.ko.statussaver.WAApp;
import com.ko.statussaver.home.OnActionListener;
import com.ko.statussaver.home.activities.HomeActivity;
import com.ko.statussaver.home.activities.ImageViewActivity;
import com.ko.statussaver.model.FileDetail;
import com.ko.statussaver.notification.NotificationRegister;
import com.ko.statussaver.settings.SettingsActivity;
import com.ko.statussaver.utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.ko.statussaver.WAApp.getApp;
import static com.ko.statussaver.utils.Constants.RC_CHANGE_THEME;

public class ImageStoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<FileDetail> fileDetailArrayList;
    private Context context;
    private OnActionListener onActionListener;
    private boolean isSavedStories;
    private final int VIEW_ITEM = 1;
    private final int VIEW_SUGGESTION = 2;
    private NotificationRegister notificationRegister;
    private RecyclerView recyclerView;
    private int holderPosition;
    private SimpleDateFormat simpleDateFormat;
    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    public ImageStoryListAdapter(Context context, ArrayList<FileDetail> fileDetailArrayList, boolean isSavedStories) {
        this.context = context;
        this.fileDetailArrayList = fileDetailArrayList;
        this.isSavedStories = isSavedStories;
        notificationRegister = new NotificationRegister(context);
        simpleDateFormat= new SimpleDateFormat("dd-MMM-yyyy hh:mm aaa", Locale.ENGLISH);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FileDetail.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_item, parent, false);
            return new FileTypeHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion, parent, false);
            return new SuggestionHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (fileDetailArrayList.get(position).viewType) {
            case VIEW_ITEM:
                return FileDetail.TYPE_ITEM;
            case VIEW_SUGGESTION:
                return FileDetail.TYPE_SUGGESTION;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FileTypeHolder) {
            final FileDetail fileDetail = fileDetailArrayList.get(position);
            if (getApp().getWaPreference().getVideoQualityOption()) {
                Picasso.get().load(fileDetail.file).into(((FileTypeHolder) holder).imgPhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        ((FileTypeHolder) holder).txtLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        ((FileTypeHolder) holder).txtLoading.setText(R.string.lding_err);
                    }
                });
            } else {
                Picasso.get().load(fileDetail.file).centerCrop().resize(400, 400).into(((FileTypeHolder) holder).imgPhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        ((FileTypeHolder) holder).txtLoading.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        ((FileTypeHolder) holder).txtLoading.setText(R.string.lding_err);
                    }
                });
            }
            ((FileTypeHolder) holder).txtPostedDate.setText(context.getString(R.string.saved_on)+" "+simpleDateFormat.format(new Date(fileDetail.file.lastModified())));
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
            if (position == 0) {
                holderPosition = position;
                onActionListener.onShowCaseView(((HomeActivity) context), holder, holderPosition);
            }
        } else {
            if (fileDetailArrayList.get(position).fileType == 1) {
                ((SuggestionHolder) holder).txtSuggestion.setText(R.string.try_dif_theme);
                ((SuggestionHolder) holder).btnOpen.setVisibility(View.VISIBLE);
                ((SuggestionHolder) holder).swtichNotification.setVisibility(View.GONE);
            } else if (fileDetailArrayList.get(position).fileType == 2) {
                ((SuggestionHolder) holder).btnOpen.setVisibility(View.GONE);
                ((SuggestionHolder) holder).swtichNotification.setVisibility(View.VISIBLE);
                if (WAApp.getApp().getWaPreference().getDailyNotification()) {
                    ((SuggestionHolder) holder).txtSuggestion.setText(R.string.turn_on);
                    ((SuggestionHolder) holder).swtichNotification.setChecked(true);
                } else {
                    ((SuggestionHolder) holder).txtSuggestion.setText(R.string.turn_onn);
                    ((SuggestionHolder) holder).swtichNotification.setChecked(false);
                }
            }
            ((SuggestionHolder) holder).btnOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fileDetailArrayList.get(position).fileType == 1) {
//                        showThemeDialog();
                        ((HomeActivity) context).startActivityForResult(new Intent(context, SettingsActivity.class).putExtra(Constants.OPEN_SETTINGS, true), RC_CHANGE_THEME);
                    }
                }
            });
            ((SuggestionHolder) holder).swtichNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isOn) {
                    if (isOn) {
//                        notificationRegister.sendSuccessNotification();
                        notificationRegister.registerNotificationService();
                        getApp().getWaPreference().setValueForDailyNotification(true);
                        ((SuggestionHolder) holder).txtSuggestion.setText(R.string.turn_on);
                        Toast.makeText(context, "Notification turned on successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        ((SuggestionHolder) holder).txtSuggestion.setText(R.string.turn_onn);
                        getApp().getWaPreference().setValueForDailyNotification(false);
                        notificationRegister.unRegisterNotificationService();
                        Toast.makeText(context, "Notification turned off successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
            imgPlay.setVisibility(View.INVISIBLE);
            checkAndChangeIcon();

        }

        private void checkAndChangeIcon() {
            if (getApp().getWaPreference().getTheme() == Constants.THEME_BLUE) {
                imgShare.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_share_blue_24dp, 0, 0, 0);
                imgUpload.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_download_blue_24dp, 0, 0, 0);
            } else if (getApp().getWaPreference().getTheme() == Constants.THEME_RED) {
                imgShare.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_share_red_500_24dp, 0, 0, 0);
                imgUpload.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_download_red_500_24dp, 0, 0, 0);
            } else if (getApp().getWaPreference().getTheme() == Constants.THEME_GREEN) {
                imgShare.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_share_green_24dp, 0, 0, 0);
                imgUpload.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_download_green_24dp, 0, 0, 0);
            }
        }
    }

    public class SuggestionHolder extends RecyclerView.ViewHolder {
        private TextView txtSuggestion;
        private Button btnOpen;
        public Switch swtichNotification;

        public SuggestionHolder(View view) {
            super(view);
            txtSuggestion = (TextView) view.findViewById(R.id.txt_suggestion);
            btnOpen = (Button) view.findViewById(R.id.btn_open);
            swtichNotification = itemView.findViewById(R.id.swt_open);
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
