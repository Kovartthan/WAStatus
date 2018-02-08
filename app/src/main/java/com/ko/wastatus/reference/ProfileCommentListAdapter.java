//package com.ko.wastatus.reference;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Rect;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Handler;
//import android.support.annotation.RequiresApi;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.TextureView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
//import com.bumptech.glide.request.RequestListener;
//import com.bumptech.glide.request.target.Target;
//import com.ciceroneme.android.R;
//import com.ciceroneme.android.logger.Log;
//import com.ciceroneme.android.profile.OnMediaInstanceListener;
//import com.ciceroneme.android.profile.location.profile_location_details.ProfileLocationListDetailActivity;
//import com.ciceroneme.android.profile.location.profile_location_details.ProfileLocationViewParser;
//import com.ciceroneme.android.profile.location.profile_location_details.ProfileTipCommentAdapter;
//import com.ciceroneme.android.profile.location.profile_location_details.ProfileTipsAdapter;
//import com.ciceroneme.android.profile.location.profile_location_view_photos.ProfileLocationPhotoDetailActivity;
//import com.ciceroneme.android.profile.maps.ViewMoreAdapter;
//import com.ciceroneme.android.ui.FullScreenVideoView;
//import com.ciceroneme.android.utils.DateConversion;
//import com.ciceroneme.android.utils.TextUtils;
//import com.facebook.drawee.view.SimpleDraweeView;
//import com.squareup.picasso.Picasso;
//
//import java.lang.ref.WeakReference;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//import static com.ciceroneme.android.constants.AppConstants.COMMENT;
//import static com.ciceroneme.android.constants.AppConstants.LOCATION_NAME;
//import static com.ciceroneme.android.constants.AppConstants.MAP_NAME;
//import static com.ciceroneme.android.constants.AppConstants.MEDIA;
//import static com.ciceroneme.android.constants.AppConstants.TIME_AGO;
//import static com.ciceroneme.android.constants.AppConstants.USER_NAME;
//import static com.ciceroneme.android.constants.AppConstants.USER_PIC;
//
//
//public class ProfileCommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private Context mContext;
//    private int lastVisibleItem, totalItemCount, visibleThreshold = 2;
//    private final int VIEW_ITEM = 1;
//    private final int VIEW_PROG = 0;
//    private boolean loading;
//    private int currentPostion = 0;
//    private RecyclerView rvCommentList;
//    private OnLoadMoreListener onLoadMoreListener;
//    private List<ProfileCommentListParser.MapLocationCommentList> tipArrayList;
//    private WeakReference<Activity> activityWeakReference;
//    private ViewHolder currentVideoViewHolder;
//    private int userId = 0;
//    private OnDeleteTipListener onDeleteTipListener;
//    private OnMediaInstanceListener onMediaInstanceListener;
//    private String locName,mapName;
//
//    public void setOnDeleteTipListener(OnDeleteTipListener onDeleteTipListener) {
//        this.onDeleteTipListener = onDeleteTipListener;
//    }
//
//    public void setOnMediaInstanceListener(OnMediaInstanceListener onMediaInstanceListener){
//        this.onMediaInstanceListener = onMediaInstanceListener;
//    }
//
//    public ProfileCommentListAdapter(Activity activity, Context context, List<ProfileCommentListParser.MapLocationCommentList> tipArrayList, RecyclerView rvCommentList, int userId) {
//        this.mContext = context;
//        this.tipArrayList = tipArrayList;
//        this.activityWeakReference = new WeakReference<>(activity);
//        this.userId = userId;
//        if (rvCommentList.getLayoutManager() instanceof LinearLayoutManager) {
//            Log.d("OnScrolled called", "instance true");
//            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvCommentList.getLayoutManager();
//            rvCommentList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    Log.d("OnScrolled called", "loading" + loading + " totalItemCount:" + totalItemCount + " lastVisibleItem:" + lastVisibleItem + " visibleThreshold:" + visibleThreshold);
//                    totalItemCount = linearLayoutManager.getItemCount();
//                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//                        if (onLoadMoreListener != null) {
//                            onLoadMoreListener.onLoadMore();
//                        }
//                        loading = true;
//                    }
//                }
//
//                @Override
//                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING || newState == RecyclerView.SCROLL_STATE_IDLE) {
//                        if (currentVideoViewHolder != null) {
//                            currentVideoViewHolder.onScrolled(recyclerView);
//                        }
//                    }
//                }
//            });
//        }
//    }
//
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.e("onViewType", "" + viewType);
//        if (viewType == ProfileCommentListParser.TEXT_TYPE) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_text_type_item, parent, false);
//            return new TextTypeHolder(view);
//        } else if (viewType == ProfileCommentListParser.IMAGE_TYPE) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_image_type_item, parent, false);
//            return new ImageTypeHolder(view);
//        } else if (viewType == ProfileCommentListParser.VIDEO_TYPE) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recent_tip_item, parent, false);
//            return new ViewHolder(view);
//        } else if (viewType == VIEW_PROG) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_custom_progress_footer, parent, false);
//            return new MyProgressHolder(view);
//        }
//        return null;
//
//    }
//
//    @Override
//    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
//        Log.e("onPostion", "pos-" + position);
//        final ProfileCommentListParser.MapLocationCommentList commentListParser = tipArrayList.get(position);
//        if (commentListParser != null) {
//            switch (commentListParser.type) {
//                case "1":
//                    if (holder instanceof ImageTypeHolder) {
//                 /*       if (position == tipArrayList.size() - 1) {
//                            ((ImageTypeHolder) holder).bootomLine.setVisibility(View.GONE);
//                        } else {
//                            ((ImageTypeHolder) holder).bootomLine.setVisibility(View.VISIBLE);
//                        }*/
////                        Picasso.with(mContext).load(commentListParser.user.displayPicture).placeholder(R.drawable.d_user).into(((ImageTypeHolder) holder).imgProfile);
//                        ((ImageTypeHolder) holder).imgProfile.setImageURI(commentListParser.user.displayPicture);
//                        ((ImageTypeHolder) holder).txtProfileName.setText(TextUtils.capitalizeFirstLetter(commentListParser.user.userName));
//                        ((ImageTypeHolder) holder).txtPostedTip.setText(TextUtils.decodeBase64(commentListParser.comments));
//                        ((ImageTypeHolder) holder).txtPostedDate.setText(formatDateString(commentListParser.dateCreated));
//                      //  Glide.with(mContext).load(commentListParser.media).into(((ImageTypeHolder) holder).imgTipPic);
////                        ((ImageTypeHolder) holder).imgProgress.setVisibility(View.VISIBLE);
//                     /*   Glide.with(mContext).load(commentListParser.media).placeholder(R.drawable.media).listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                ((ImageTypeHolder) holder).imgProgress.setVisibility(View.GONE);
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                ((ImageTypeHolder) holder).imgProgress.setVisibility(View.GONE);
//                                return false;
//                            }
//                        }).into(((ImageTypeHolder) holder).imgTipPic);*/
//
//                        ((ImageTypeHolder)holder).imgTipPic.setImageURI(Uri.parse(commentListParser.media_thumb_image));
//
//                        if (Integer.parseInt(commentListParser.user.userId) == userId) {
//                            ((ImageTypeHolder) holder).deleteButton.setVisibility(View.VISIBLE);
//                            ((ImageTypeHolder) holder).deleteButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    onDeleteTipListener.onViewAllTip(Integer.parseInt(commentListParser.commentId), holder.getAdapterPosition(),commentListParser);
//                                }
//                            });
//                        } else {
//                            ((ImageTypeHolder) holder).deleteButton.setVisibility(View.GONE);
//                        }
//                    }
//                    break;
//                case "2":
//                    if (holder instanceof ViewHolder) {
//                   /*     if (position == tipArrayList.size() - 1) {
//                            ((ViewHolder) holder).bootomLine.setVisibility(View.GONE);
//                        } else {
//                            ((ViewHolder) holder).bootomLine.setVisibility(View.VISIBLE);
//                        }*/
////                        Glide.with(mContext).load(commentListParser.user.displayPicture).placeholder(R.drawable.d_user).into(((ViewHolder) holder).imgProfile);
//                        ((ViewHolder) holder).imgProfile.setImageURI(commentListParser.user.displayPicture);
//                        ((ViewHolder) holder).txtProfileName.setText(TextUtils.capitalizeFirstLetter(commentListParser.user.userName));
//                        ((ViewHolder) holder).txtPostedTip.setText(TextUtils.decodeBase64(commentListParser.comments));
//                        ((ViewHolder) holder).txtPostedDate.setText(formatDateString(commentListParser.dateCreated));
//                        ((ViewHolder) holder).videoUrl = commentListParser.media;
////                        ((ViewHolder) holder).videoProgress.setVisibility(View.VISIBLE);
//                 /*       Glide.with(mContext).load(commentListParser.media_thumb_image).placeholder(R.drawable.media).listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                ((ViewHolder) holder).videoProgress.setVisibility(View.GONE);
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                ((ViewHolder) holder).videoProgress.setVisibility(View.GONE);
//                                return false;
//                            }
//                        }).into(((ViewHolder) holder).imgTipPic);*/
////                        ((ViewHolder) holder).imgTipPic.setVisibility(View.VISIBLE);
//                        ((ViewHolder)holder).imgTipPic.setImageURI(Uri.parse(commentListParser.media_thumb_image));
//                        if (Integer.parseInt(commentListParser.user.userId) == userId) {
//                            ((ViewHolder) holder).deleteButton.setVisibility(View.VISIBLE);
//                            ((ViewHolder) holder).deleteButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    onDeleteTipListener.onViewAllTip(Integer.parseInt(commentListParser.commentId),  holder.getAdapterPosition(),commentListParser);
//                                }
//                            });
//                        } else {
//                            ((ViewHolder) holder).deleteButton.setVisibility(View.GONE);
//                        }
//                    }
//                    break;
//                case "3":
//                    if (holder instanceof TextTypeHolder) {
//                  /*          if (position == tipArrayList.size() - 1) {
//                            ((TextTypeHolder) holder).bootomLine.setVisibility(View.GONE);
//                        } else {
//                            ((TextTypeHolder) holder).bootomLine.setVisibility(View.VISIBLE);
//                        }*/
////                        Glide.with(mContext).load(commentListParser.user.displayPicture).placeholder(R.drawable.d_user).into(((TextTypeHolder) holder).imgProfile);
//                        ((TextTypeHolder) holder).imgProfile.setImageURI(commentListParser.user.displayPicture);
//                        ((TextTypeHolder) holder).txtProfileName.setText(TextUtils.capitalizeFirstLetter(commentListParser.user.userName));
//                        ((TextTypeHolder) holder).txtPostedTip.setText(TextUtils.decodeBase64(commentListParser.comments));
//                        ((TextTypeHolder) holder).txtPostedDate.setText(formatDateString(commentListParser.dateCreated));
//                        if (Integer.parseInt(commentListParser.user.userId) == userId) {
//                            ((TextTypeHolder) holder).deleteButton.setVisibility(View.VISIBLE);
//                            ((TextTypeHolder) holder).deleteButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    onDeleteTipListener.onViewAllTip(Integer.parseInt(commentListParser.commentId),  holder.getAdapterPosition(),commentListParser);
//                                }
//                            });
//                        } else {
//                            ((TextTypeHolder) holder).deleteButton.setVisibility(View.GONE);
//                        }
//                    }
//                    break;
//            }
//        } else {
//            ((MyProgressHolder) holder).rlProgressBar.setVisibility(View.VISIBLE);
//            ((MyProgressHolder) holder).rlProgressBar.setClickable(false);
//        }
//    }
//
//  /*  @Override
//    public void onViewRecycled(RecyclerView.ViewHolder holder) {
//        final int position = holder.getAdapterPosition();
//        if (position != RecyclerView.NO_POSITION) {
//            switch (tipArrayList.get(holder.getAdapterPosition()).type) {
//                case "2":
//                    if (holder == currentVideoViewHolder) {
//                        currentVideoViewHolder = null;
//                        ((ViewHolder) holder).stopVideo();
//                    }
//                    ((ViewHolder) holder).videoTip.stopPlayback();
//                    break;
//            }
//        }
//        super.onViewRecycled(holder);
//    }
//*/
//    @Override
//    public int getItemViewType(int position) {
//        return tipArrayList.get(position) != null ? Integer.parseInt(tipArrayList.get(position).type) : VIEW_PROG;
//    }
//
//
//    private String formatDateString(String dateCreated) {
//      /*  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//        Date myDate = null;
//        try {
//            myDate = dateFormat.parse(dateCreated);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(myDate);
//        DateFormat df = new SimpleDateFormat("yy", Locale.ENGLISH);
//        String formattedDate = df.format(Calendar.getInstance().getTime());
//        Log.e("Date", "" + myDate);
//        SimpleDateFormat month_date = new SimpleDateFormat("MMM", Locale.ENGLISH);
//        String month = month_date.format(cal.getTime());
//        String date = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
//        String year = String.valueOf(cal.get(Calendar.YEAR));*/
//
//       // return DateConversion.getDateAsFormat(dateCreated,true);
//        String givenDateSTrin = dateCreated;
//        long millis = DateConversion.getMilliSecondFromString(givenDateSTrin);
//        return DateConversion.converToTimeForRecentActivity(mContext,millis,givenDateSTrin);
//    }
//
//    @Override
//    public int getItemCount() {
//        return tipArrayList.size();
//    }
//
//    public void addList() {
//        this.tipArrayList.add(null);
//    }
//
//    public void removeItem(int position) {
//        ProfileCommentListParser.MapLocationCommentList commentListParser = tipArrayList.get(position);
//        Log.e("Before Remove", "size " + tipArrayList.size() + " getAdapterCount " + getItemCount() + "position " + position + "Comment " + TextUtils.decodeBase64(commentListParser.comments));
//        tipArrayList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemChanged(position);
//        Log.e("After Remove", "size " + tipArrayList.size() + " getAdapterCount " + getItemCount() + "position " + position);
//    }
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        private ImageView  imgPlay;
//        private SimpleDraweeView imgProfile,imgTipPic;
//        private FullScreenVideoView videoTip;
//        private TextView txtProfileName, txtPostedDate, txtPostedTip;
//        private RelativeLayout rlPictureOrVideo;
//        private ProgressBar videoProgress;
//        private View bootomLine;
//        private ImageView deleteButton;
//        private String videoUrl;
//        private boolean isVideoPlaying = false;
//
//        public String getVideoUrl() {
//            return videoUrl;
//        }
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            imgProfile = (SimpleDraweeView) itemView.findViewById(R.id.img_tip_profile_pic);
//            videoTip = (FullScreenVideoView) itemView.findViewById(R.id.videoview);
//            imgTipPic = (SimpleDraweeView) itemView.findViewById(R.id.img_tip);
//            txtProfileName = (TextView) itemView.findViewById(R.id.txt_tip_name);
//            txtPostedDate = (TextView) itemView.findViewById(R.id.txt_tip_posted_date);
//            txtPostedTip = (TextView) itemView.findViewById(R.id.txt_tip_posted_tip);
//            rlPictureOrVideo = (RelativeLayout) itemView.findViewById(R.id.layout_img_video);
//            imgPlay = (ImageView) itemView.findViewById(R.id.img_play_icon);
//            videoProgress = (ProgressBar) itemView.findViewById(R.id.progress_bar);
//            deleteButton = (ImageView) itemView.findViewById(R.id.iv_delete);
//            bootomLine = itemView.findViewById(R.id.bottom_line);
//            videoTip.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(final MediaPlayer mp) {
//                    videoTip.setIsPrepared(true);
//                    onMediaInstanceListener.onMediaValue(mp);
//                    if (currentVideoViewHolder == ViewHolder.this) {
//                        videoProgress.setVisibility(View.INVISIBLE);
//                        videoTip.setVisibility(View.VISIBLE);
////                        videoTip.seekTo(0);
//                        videoTip.start();
//                        imgTipPic.setVisibility(View.GONE);
//                        isVideoPlaying = true;
//                    }
//                }
//            });
//
//            videoTip.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == MotionEvent.ACTION_DOWN && isVideoPlaying) {
////                        currentPostion = videoTip.getCurrentPosition();
//                        videoTip.pause();
//                        videoProgress.setVisibility(View.GONE);
//                        imgPlay.setVisibility(View.VISIBLE);
//                        return true;
//                    }
//                    return false;
//                }
//            });
//            videoTip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    Log.v("Video", "onFocusChange" + hasFocus);
//                    if (!hasFocus && currentVideoViewHolder == ViewHolder.this) {
////                        currentPostion = 0;
//                        isVideoPlaying = false;
//                        stopVideo();
//                    }
//
//                }
//            });
//            videoTip.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    Log.v("Video", "onCompletion");
////                    currentPostion = 0;
//                    imgTipPic.setVisibility(View.VISIBLE);
//                    imgPlay.setVisibility(View.VISIBLE);
//                    isVideoPlaying = false;
//                    if (videoTip.getVisibility() == View.VISIBLE)
//                        videoTip.setVisibility(View.INVISIBLE);
//
//                    videoProgress.setVisibility(View.INVISIBLE);
//                    currentVideoViewHolder = null;
//                }
//            });
//            imgPlay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(videoTip.isPlaying()){
//                        Log.e("videoTip ", "isPlaying");
//                    }
//                    if (currentVideoViewHolder != null && currentVideoViewHolder != ViewHolder.this) {
//                        currentVideoViewHolder.videoTip.pause();
////                        currentPostion = videoTip.getCurrentPosition();
//                        currentVideoViewHolder.imgTipPic.setVisibility(View.GONE);
//                        currentVideoViewHolder.imgPlay.setVisibility(View.VISIBLE);
//                        currentVideoViewHolder.videoProgress.setVisibility(View.VISIBLE);
//                        if (currentVideoViewHolder.videoTip.getVisibility() == View.VISIBLE)
//                            currentVideoViewHolder.videoTip.setVisibility(View.GONE);
//                        currentVideoViewHolder = null;
//                    }
//                    currentVideoViewHolder = ViewHolder.this;
//
//                    imgPlay.setVisibility(View.GONE);
//                    if (!isVideoPlaying) {
//                        videoProgress.setVisibility(View.VISIBLE);
//                    }
//                    videoTip.setVisibility(View.VISIBLE);
//                    imgTipPic.setVisibility(View.GONE);
//                    if (!getVideoUrl().equals(videoTip.getVideoPath())) {
//                        videoTip.setIsPrepared(false);
//                        videoTip.setVideoPath(getVideoUrl());
//                        videoTip.requestFocus();
//                    } else {
//                       /* if(currentPostion == 0) {
//                            if (videoTip.isPrepared()) {
//                                videoProgress.setVisibility(View.VISIBLE);
//                            } else {
//                                videoProgress.setVisibility(View.VISIBLE);
//                            }
//                        }*/
//                        videoTip.requestFocus();
////                        videoTip.seekTo(currentPostion);
//                        videoTip.start();
//                    }
//                }
//            });
//
//        }
//
//        private void stopVideo() {
//            videoTip.pause();
//            if (videoTip.getVisibility() == View.VISIBLE) {
//                videoTip.setVisibility(View.INVISIBLE);
//            }
//            imgTipPic.setVisibility(View.VISIBLE);
//            imgPlay.setVisibility(View.VISIBLE);
//            videoProgress.setVisibility(View.INVISIBLE);
//            currentVideoViewHolder = null;
//        }
//
//        public void onScrolled(RecyclerView recyclerView) {
//            if (isViewNotVisible(imgPlay, recyclerView) || isViewNotVisible(videoProgress, recyclerView)) {
//                stopVideo();
//            }
//        }
//
//        public boolean isViewNotVisible(View view, RecyclerView recyclerView) {
//            Rect scrollBounds = new Rect();
//            recyclerView.getHitRect(scrollBounds);
//            return view.getVisibility() == View.VISIBLE && !view.getLocalVisibleRect(scrollBounds);
//        }
//    }
//
//
//    public class TextTypeHolder extends RecyclerView.ViewHolder {
//        private ImageView  deleteButton;
//        private SimpleDraweeView imgProfile;
//        private TextView txtProfileName, txtPostedDate, txtPostedTip;
//        private View bootomLine;
//
//        public TextTypeHolder(View itemView) {
//            super(itemView);
//            imgProfile = (SimpleDraweeView) itemView.findViewById(R.id.img_tip_profile_pic);
//            txtProfileName = (TextView) itemView.findViewById(R.id.txt_tip_name);
//            txtPostedDate = (TextView) itemView.findViewById(R.id.txt_tip_posted_date);
//            txtPostedTip = (TextView) itemView.findViewById(R.id.txt_tip_posted_tip);
//            bootomLine = (View) itemView.findViewById(R.id.bottom_line);
//            deleteButton = (ImageView) itemView.findViewById(R.id.iv_delete);
//        }
//    }
//
//    public class ImageTypeHolder extends RecyclerView.ViewHolder {
//        private ImageView   deleteButton;
//        private TextView txtProfileName, txtPostedDate, txtPostedTip;
//        private View bootomLine;
//        private SimpleDraweeView imgProfile,imgTipPic;
//        private ProgressBar imgProgress;
//
//        public ImageTypeHolder(View itemView) {
//            super(itemView);
//            imgProfile = (SimpleDraweeView) itemView.findViewById(R.id.img_tip_profile_pic);
//            txtProfileName = (TextView) itemView.findViewById(R.id.txt_tip_name);
//            txtPostedDate = (TextView) itemView.findViewById(R.id.txt_tip_posted_date);
//            txtPostedTip = (TextView) itemView.findViewById(R.id.txt_tip_posted_tip);
//            imgTipPic = (SimpleDraweeView) itemView.findViewById(R.id.img_tip);
//            bootomLine = (View) itemView.findViewById(R.id.bottom_line);
//            imgProgress =(ProgressBar)itemView.findViewById(R.id.image_progress);
//            deleteButton = (ImageView) itemView.findViewById(R.id.iv_delete);
//        }
//    }
//
//    public void setLoaded() {
//        loading = false;
//    }
//
//    void removeProgress(int i) {
//        this.tipArrayList.remove(i);
//        notifyItemRemoved(i);
//    }
//
//    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
//        this.onLoadMoreListener = onLoadMoreListener;
//    }
//
//    private class MyProgressHolder extends RecyclerView.ViewHolder {
//        RelativeLayout rlProgressBar;
//
//        MyProgressHolder(View view) {
//            super(view);
//            rlProgressBar = (RelativeLayout) view.findViewById(R.id.progress_bar);
//        }
//    }
//
//    public interface OnLoadMoreListener {
//        void onLoadMore();
//    }
//
//    public void updateList(ArrayList<ProfileCommentListParser.MapLocationCommentList> tipArrayList) {
//        this.tipArrayList = tipArrayList;
//        notifyDataSetChanged();
//    }
//
//}
