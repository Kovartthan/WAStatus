//package com.ko.wastatus.reference;
//
//import android.Manifest;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.content.pm.ResolveInfo;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.media.MediaPlayer;
//import android.media.ThumbnailUtils;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.provider.Settings;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.content.FileProvider;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.InputFilter;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.Surface;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
//import com.bumptech.glide.request.RequestListener;
//import com.bumptech.glide.request.target.Target;
//import com.ciceroneme.android.BuildConfig;
//import com.ciceroneme.android.R;
//import com.ciceroneme.android.base.BaseActivity;
//import com.ciceroneme.android.constants.AppConstants;
//import com.ciceroneme.android.logger.Log;
//import com.ciceroneme.android.player.DemoPlayer;
//import com.ciceroneme.android.player.ExtractorRendererBuilder;
//import com.ciceroneme.android.player.VideoSurfaceView;
//import com.ciceroneme.android.profile.OnMediaInstanceListener;
//import com.ciceroneme.android.profile.location.profile_location_details.ProfileLocationViewParser;
//import com.ciceroneme.android.profile.squarevideo.BTRecorderActivity;
//import com.ciceroneme.android.ui.CeProgressDialog;
//import com.ciceroneme.android.utils.AlertUtils;
//import com.ciceroneme.android.utils.BDevice;
//import com.ciceroneme.android.utils.FileUtils;
//import com.ciceroneme.android.utils.MultipartUtility;
//import com.ciceroneme.android.utils.TextUtils;
//import com.ciceroneme.android.video.TrimVideo;
//import com.ciceroneme.android.webservice.CeceronmeUrl;
//import com.ciceroneme.android.webservice.RetrofitCallback;
//import com.facebook.drawee.view.SimpleDraweeView;
//import com.google.android.exoplayer.ExoPlayer;
//import com.google.android.exoplayer.drm.UnsupportedDrmException;
//import com.google.android.exoplayer.util.Util;
//import com.google.gson.Gson;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//
//import static com.ciceroneme.android.utils.AlertUtils.getBuilder;
//
//public class ImageStoryListAdapter extends BaseActivity implements OnDeleteTipListener,OnMediaInstanceListener {
//    private RecyclerView rvCommentList;
//    private ProfileCommentListExoAdapter profileCommentListAdapter;
//    private ArrayList<ProfileCommentListParser.MapLocationCommentList> commentList;
//    private int mapId, locationId;
//    private int page = 1, listedCount = 0;
//    private int mCurrentCount, mTotalCount;
//    private SwipeRefreshLayout swipeRefreshLayout;
//    private boolean isWebserviceRunning = false;
//    private String locationName = "";
//    public boolean isDelete = false;
//    private MediaPlayer mediaPlayer = null;
//    private boolean isPermissionDefaults = false;
//    private int REQUEST_PHOTO = 1;
//    private int REQUEST_VIDEO = 2;
//    public static final int REQ_CODE_TRIM_VIDEO = 204;
//    private String currentlyCliked = null;
//    private boolean isLeaveDialog = false;
//    private DemoPlayer leaveDialogExoPlayer;
//    private boolean isLeaveDialogVideoStarted = false;
//    private Uri uri = null;
//    private  ImageView imgDelete;
//    private static String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
//    private ImageView imgHeaderImage;
//    private RelativeLayout imagevideoLayout;
//    private ProgressBar videoProgress;
//    private VideoSurfaceView videoView;
//    private ImageView imagePlay;
//    private ImageView imgEditPhoto;
//    private int selectedAttachment = 0;
//    private boolean isEdit = false;
//    private boolean isChangesDoneForEdit = false;
//    private int editCommentAttachment = 0;
//    private String checkComment = "";
//    private int isDeleteForEdit = 0;
//    Bitmap bmp = null;
//    private VideoSurfaceView surfaceView;
//    private boolean isImageOrVideoPlaced = false;
//    private String phone ,address,mapUrl;
//    private Uri picUri = null;
//    private boolean flag = false;
//    private String mapName;
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.e(TAG, "Changes from save Result Code --> " + resultCode + " Intent --> " + data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_PHOTO) {
//                isImageOrVideoPlaced = true;
//                isChangesDoneForEdit = true;
//                editCommentAttachment = 1;
//                selectedAttachment = 1;
//                isDeleteForEdit = 0;
//                String savedImageFile = FileUtils.createMapImageOrVideoFile(this, "photo").getAbsolutePath();
//                bmp = BitmapFactory.decodeFile(savedImageFile);
//                ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
//                bmp.compress(Bitmap.CompressFormat.JPEG, 60, bos2);
//                imgHeaderImage.setImageBitmap(bmp);
//                imagevideoLayout.setVisibility(View.VISIBLE);
//                videoView.setVisibility(View.GONE);
//                imgHeaderImage.setVisibility(View.VISIBLE);
//                imagePlay.setVisibility(View.GONE);
//                imgEditPhoto.setVisibility(View.VISIBLE);
//                videoProgress.setVisibility(View.GONE);
//                imgDelete.setVisibility(View.VISIBLE);
////                checkDeleteIconVisibility();
//            } else if (requestCode == RC_GALLERY) {
//                Uri inputStreamUri = data.getData();
//                if (inputStreamUri.toString().contains("images")) {
//                    String outputSteamLocation = FileUtils.createMapImageOrVideoFile(this, "photo").getAbsolutePath();
//                    new CopyStream(inputStreamUri, outputSteamLocation, ceProgressDialog).execute();
//                    //handle image
//                } else if (inputStreamUri.toString().contains("video")) {
//                    //handle video
//                    String outputSteamLocation = FileUtils.createMapImageOrVideoFile(this, "video").getAbsolutePath();
//                    File file = new File(FileUtils.getPath(this, inputStreamUri));
//                    try {
//                        if (!TextUtils.isNullOrEmpty(outputSteamLocation)) {
//                            MediaPlayer mp = MediaPlayer.create(this, inputStreamUri);
//                            int duration = mp.getDuration();
//                            mp.release();
//
//                            if ((duration / 1000) > 10) {
//                                // Show Your Messages
//                                android.util.Log.d(TAG, "onActivityResult: video Length is greater ");
//                                callTrimActivity(file);
//                            } else {
//                               new CopyStream(inputStreamUri, outputSteamLocation, ceProgressDialog).execute();
//
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }  else if (requestCode == REQ_CODE_TRIM_VIDEO) {
//                String path = data.getStringExtra(TrimVideo.KEY_MEDIA_ITEM_PATH);
//                File file = new File(path);
//
//                Uri inputStreamUri = FileUtils.getUri(file);
//                if (inputStreamUri.toString().contains("video")) {
//                    //handle video
//                    String outputSteamLocation = FileUtils.createMapImageOrVideoFile(this, "video").getAbsolutePath();
//                    try {
//                        if (!TextUtils.isNullOrEmpty(outputSteamLocation)) {
//                            MediaPlayer mp = MediaPlayer.create(this, inputStreamUri);
//                            int duration = mp.getDuration();
//                            mp.release();
//                            if ((duration / 1000) > 10) {
//                                // Show Your Messages
//                                android.util.Log.d(TAG, "onActivityResult: video Length is greater ");
//                                callTrimActivity(file);
//                            } else {
//                                new CopyStream(inputStreamUri, outputSteamLocation, ceProgressDialog).execute();
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            } else if (requestCode == REQUEST_VIDEO) {
//                String inputStreamLocation = data.getStringExtra(BTRecorderActivity.VIDEO_PATH);
//                String imagePath = data.getStringExtra(BTRecorderActivity.THUMB_PATH);
//                String outputSteamLocation = FileUtils.createMapImageOrVideoFile(this, "video").getAbsolutePath();
//                new CopyStream(inputStreamLocation, outputSteamLocation, ceProgressDialog).execute();
//            }
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile_comment_list);
//        init();
//        setupDefaults();
//        setupEvents();
//    }
//
//    private void init() {
//        commentList = new ArrayList<>();
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh);
//        rvCommentList = (RecyclerView) findViewById(R.id.rv_comment_list);
//        surfaceView = (VideoSurfaceView) findViewById(R.id.surface_view);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ImageStoryListAdapter.this);
//        rvCommentList.setLayoutManager(linearLayoutManager);
//
//    }
//
//    private void setupDefaults() {
//
//        if (getIntent().hasExtra(AppConstants.MAP_ID) & getIntent().hasExtra(LOCATION_ID) & getIntent().hasExtra(LOCATION_NAME) & getIntent().hasExtra(MAP_NAME)) {
//            mapId = Integer.parseInt(getIntent().getStringExtra(AppConstants.MAP_ID));
//            locationId = Integer.parseInt(getIntent().getStringExtra(AppConstants.LOCATION_ID));
//            locationName = getIntent().getStringExtra(AppConstants.LOCATION_NAME);
//            mapName = getIntent().getStringExtra(AppConstants.MAP_NAME);
//        }
//
//        phone = getIntent().getStringExtra(AppConstants.PHONE);
//        address = getIntent().getStringExtra(AppConstants.ADDRESS);
//        mapUrl = getIntent().getStringExtra(AppConstants.LOCATION_IMAGE);
//
//        profileCommentListAdapter = new ProfileCommentListExoAdapter(this, ImageStoryListAdapter.this, commentList, rvCommentList, getApp().getCnPrefernce().getUserId(),mapName,locationName);
//        profileCommentListAdapter.setOnDeleteTipListener(this);
//        rvCommentList.setAdapter(profileCommentListAdapter);
//
//        setPageTitle(locationName,false);
//        setLeftDrawable(R.drawable.back_arrow);
//        getCommentListWebService(true, false, false);
//
//    }
//
//    @Override
//    public void onLeftActionClick(View v) {
//        super.onLeftActionClick(v);
//        onBackPressed();
//    }
//
//    private void getCommentListWebService(final boolean isShowProgress, final boolean isLoading, final boolean isDelete) {
//
//        if (!BDevice.isInternetConnected(ImageStoryListAdapter.this)) {
//            AlertUtils.showSnack(ImageStoryListAdapter.this, rvCommentList, getString(R.string.check_your_internet_connection));
//            return;
//        }
//
//        isWebserviceRunning = true;
//
//
//        if (ceProgressDialog != null && !isFinishing() && isShowProgress) {
//            ceProgressDialog.show();
//        }
//
//        getApp().getWebServiceInterface().getLocationCommentDetail(mapId, locationId, page).enqueue(new RetrofitCallback<ResponseBody>() {
//            @Override
//            public void onSuccessCallback(Call<ResponseBody> call, String content) {
//                super.onSuccessCallback(call, content);
//
//                if (ceProgressDialog != null && !isFinishing() && isShowProgress) {
//                    ceProgressDialog.dismiss();
//                }
//                if (isDelete || swipeRefreshLayout.isRefreshing()) {
//                    commentList.clear();
//                }
//
//                if (isLoading) {
//                    profileCommentListAdapter.removeProgress(profileCommentListAdapter.getItemCount() - 1);
//                    profileCommentListAdapter.notifyDataSetChanged();
//                }
//
//                ProfileCommentListParser profileCommentListParser = new Gson().fromJson(content, ProfileCommentListParser.class);
//                mTotalCount = Integer.parseInt(profileCommentListParser.totalCount);
//                mCurrentCount = profileCommentListParser.listedCount + listedCount;
//                listedCount = mCurrentCount;
//
//                for (int i = 0; i < profileCommentListParser.mapLocationCommentList.size(); i++) {
//                    commentList.add(profileCommentListParser.mapLocationCommentList.get(i));
//                }
//
//                profileCommentListAdapter.notifyDataSetChanged();
//                profileCommentListAdapter.setOnMediaInstanceListener(ImageStoryListAdapter.this);
//                swipeRefreshLayout.setRefreshing(false);
//                profileCommentListAdapter.setLoaded();
//                isWebserviceRunning = false;
//            }
//
//            @Override
//            public void onFailureCallback(Call<ResponseBody> call, Throwable t, String message, int code) {
//                super.onFailureCallback(call, t, message, code);
//
//                if (ceProgressDialog != null && !isFinishing() && isShowProgress) {
//                    ceProgressDialog.dismiss();
//                }
//
//                if (swipeRefreshLayout.isRefreshing()) {
//                    swipeRefreshLayout.setRefreshing(false);
//                }
//                isWebserviceRunning = false;
//                AlertUtils.showAlert(ImageStoryListAdapter.this, getString(R.string.app_name), message);
//            }
//        });
//    }
//
//    private void setupEvents() {
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                page = 1;
//                listedCount = 0;
//                getCommentListWebService(false, false, false);
//            }
//        });
//
//        profileCommentListAdapter.setOnLoadMoreListener(new ProfileCommentListExoAdapter.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                Log.e("Current count", "---" + mCurrentCount + " Total count---" + mTotalCount);
//                if (mCurrentCount < mTotalCount && !isWebserviceRunning) {
//                    Log.e("load more", "starts");
//                    profileCommentListAdapter.addList();
//                    profileCommentListAdapter.notifyItemInserted(profileCommentListAdapter.getItemCount() + 1);
//                    if (BDevice.isInternetConnected(ImageStoryListAdapter.this)) {
//                        page++;
//                        getCommentListWebService(false, true, false);
//                    } else {
//                        AlertUtils.showAlert(ImageStoryListAdapter.this, getString(R.string.app_name), getString(R.string.check_your_internet_connection));
//                    }
//                }
//
//            }
//        });
//    }
//
//    @Override
//    public void onDeleteTip(int userId, int position, ProfileLocationViewParser.LocationDetail.Tip tip) {
//
//    }
//
//    @Override
//    public void onViewAllTip(final int userId, final int position, final ProfileCommentListParser.MapLocationCommentList tip) {
//        AlertDialog.Builder builder = getBuilder(ImageStoryListAdapter.this);
//        String[] listItem = {getString(R.string.edit_tip), getString(R.string.delete_tip)};
//        builder.setTitle(getResources().getString(R.string.select)).setItems(listItem, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (which == 0) {
//                    dialog.dismiss();
//                    showLeaveTipDialog(tip);
//                } else {
//                    dialog.dismiss();
//                    AlertUtils.showConfirmationDialog(ImageStoryListAdapter.this, R.string.app_name, R.string.alert_delete_confirm_comment, R.string.yes_small, R.string.no_small, false, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            deleteTipWebService(userId, position);
//                            dialog.dismiss();
//                        }
//                    }, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                }
//            }
//        }).create().show();
//    }
//
//
//    private void deleteTipWebService(int id, final int position) {
//        if (!BDevice.isInternetConnected(ImageStoryListAdapter.this)) {
//            AlertUtils.showSnack(ImageStoryListAdapter.this, rvCommentList, getString(R.string.check_your_internet_connection));
//            return;
//        }
//
//        if (!isFinishing() && ceProgressDialog != null) {
//            ceProgressDialog.show();
//        }
//        getApp().getWebServiceInterface().deleteComment(id).enqueue(new RetrofitCallback<ResponseBody>() {
//            @Override
//            public void onSuccessCallback(Call<ResponseBody> call, String content) {
//                super.onSuccessCallback(call, content);
//                if (ceProgressDialog != null && ceProgressDialog.isShowing()) {
//                    ceProgressDialog.dismiss();
//                }
//                JSONObject obj = null;
//                try {
//                    obj = new JSONObject(content);
//                    JSONObject meta = obj.getJSONObject("meta");
//                    String notification = obj.optString("notifications");
//                    isDelete = true;
//                    AlertUtils.showAlert(ImageStoryListAdapter.this, getString(R.string.app_name), notification, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            profileCommentListAdapter.removeItem(position);
//                            if(profileCommentListAdapter.getItemCount() == 0){
//                                setResult(RESULT_OK);
//                                finish();
//                            }
////                            page = 1;
////                            listedCount = 0;
////                            getCommentListWebService(true, false, true);
//                        }
//                    }, false);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailureCallback(Call<ResponseBody> call, Throwable t, String message, int code) {
//                super.onFailureCallback(call, t, message, code);
//                if (ceProgressDialog != null && ceProgressDialog.isShowing()) {
//                    ceProgressDialog.dismiss();
//                }
//                AlertUtils.showAlert(ImageStoryListAdapter.this, getString(R.string.app_name), message);
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        mediaPlayer = null;
//        if(isDelete){
//            setResult(RESULT_OK);
//        }else{
//            setResult(RESULT_CANCELED);
//        }
//        finish();
//        super.onBackPressed();
//    }
//
//    @Override
//    public void onMediaValue(MediaPlayer mediaPlayer) {
//        this.mediaPlayer = mediaPlayer;
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mediaPlayer != null){
//            profileCommentListAdapter.notifyDataSetChanged();
//        }
//    }
//
//    private void checkDeleteIconVisibility(){
//        if(imgDelete.getVisibility() == View.GONE ){
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)imgEditPhoto.getLayoutParams();
//            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            imgEditPhoto.setLayoutParams(params);
//        }
//    }
//
//    private void showLeaveTipDialog(final ProfileCommentListParser.MapLocationCommentList tip) {
//
//        isLeaveDialog = true;
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.dialog_leave_a_tip, null);
//        dialogBuilder.setView(dialogView);
//        final AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.setCancelable(false);
//        final ProgressBar tipImageProgress = (ProgressBar) dialogView.findViewById(R.id.progress_bar);
//        final TextView txtTitle = (TextView) dialogView.findViewById(R.id.txt_title);
//        final TextView txtSubmit = (TextView) dialogView.findViewById(R.id.txt_submit);
//        final TextView txtCancel = (TextView) dialogView.findViewById(R.id.txt_cancel);
//        final ImageView imgCamera = (ImageView) dialogView.findViewById(R.id.img_camera);
//        final RelativeLayout rlAddPhoto = (RelativeLayout) dialogView.findViewById(R.id.layout_add_photo);
//        final EditText edtTip = (EditText) dialogView.findViewById(R.id.edt_abt);
//        final TextView txtPlace = (TextView) dialogView.findViewById(R.id.txt_map_name);
//        final TextView txtAddress = (TextView) dialogView.findViewById(R.id.txt_address);
////        final TextView txtCountry = (TextView) dialogView.findViewById(R.id.txt_country);
//        final TextView txtPhone = (TextView) dialogView.findViewById(R.id.txt_phone);
//        imgDelete = (ImageView) dialogView.findViewById(R.id.delete_icon);
//        final SimpleDraweeView imgAbout = (SimpleDraweeView) dialogView.findViewById(R.id.img_about);
//        if (TextUtils.isNullOrEmpty(locationName)) {
//            txtPlace.setVisibility(View.GONE);
//        } else {
//            txtPlace.setText(TextUtils.capitalizeFirstLetter(locationName));
//        }
//        if (TextUtils.isNullOrEmpty(address)) {
//            txtAddress.setVisibility(View.GONE);
//        } else {
//            txtAddress.setText(address);
//        }
//        if (TextUtils.isNullOrEmpty(phone)) {
//            txtPhone.setVisibility(View.GONE);
//        } else {
//            txtPhone.setText("Phone: " + phone);
//        }
//
//        imgAbout.setImageURI(Uri.parse(mapUrl));
//        imgHeaderImage = (ImageView) dialogView.findViewById(R.id.image);
//        imagevideoLayout = (RelativeLayout) dialogView.findViewById(R.id.imagevideoLayout);
//        videoProgress = (ProgressBar) dialogView.findViewById(R.id.videoProgress);
//        videoView = (VideoSurfaceView) dialogView.findViewById(R.id.videoview);
//        imagePlay = (ImageView) dialogView.findViewById(R.id.play_icon);
//        imgEditPhoto = (ImageView) dialogView.findViewById(R.id.edit_icon);
////        imgCamera.setTextSize(12);
//        alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.alert_dialog_round_corner));
//        imagePlay.setImageResource(R.drawable.play);
//        txtSubmit.setSelected(true);
//
//        txtCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectedAttachment = 0;
//                isLeaveDialog = false;
//                isChangesDoneForEdit = false;
//                editCommentAttachment = 0;
//                checkComment = "";
//                txtCancel.setSelected(true);
//                txtSubmit.setSelected(false);
//                alertDialog.dismiss();
//            }
//        });
//
//
//
//        setEditTextMaxLength(edtTip, 160);
//
//        txtSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (TextUtils.isNullOrEmpty(edtTip.getText().toString().trim())) {
//                    edtTip.setHint(R.string.enter_a_tip);
//                    edtTip.setText("");
//                    edtTip.setHintTextColor(getResources().getColor(R.color.error_text_red));
//                } else {
//                    if (isEdit) {
//                        if (!checkComment.equalsIgnoreCase(edtTip.getText().toString().trim())) {
//                            isChangesDoneForEdit = true;
//                        }
//                        if (isChangesDoneForEdit) {
//                            new EditDataWithImage(tip.commentId, edtTip.getText().toString().trim(), editCommentAttachment, isDeleteForEdit, alertDialog).execute(CeceronmeUrl.BASE_URL + "api/map/location/edit/tip/" + tip.commentId);
//                        } else {
//                            alertDialog.dismiss();
//                        }
//                    }
//                }
//            }
//        });
//
//        imgDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isDeleteForEdit = 1;
//                isChangesDoneForEdit = true;
//                editCommentAttachment = 3;
//                imgCamera.setVisibility(View.VISIBLE);
//                imgEditPhoto.setVisibility(View.GONE);
//                imagevideoLayout.setVisibility(View.GONE);
//                imgHeaderImage.setVisibility(View.GONE);
//                imgEditPhoto.setVisibility(View.GONE);
//                imgDelete.setVisibility(View.GONE);
//            }
//        });
//
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, BDevice.getPixelFromDp(ImageStoryListAdapter.this, 200));
//        rlAddPhoto.setLayoutParams(layoutParams);
//
//        if (tip != null) {
//
//            isEdit = true;
//            txtTitle.setText("Edit a tip");
//            checkComment = TextUtils.decodeBase64(tip.comments);
//            editCommentAttachment = 3;
//            if (!TextUtils.isNullOrEmpty(tip.comments)) {
//                edtTip.setText(TextUtils.decodeBase64(tip.comments));
//            }
//            switch (tip.type) {
//                case "1":
//                    editCommentAttachment = 1;
//                    imgCamera.setVisibility(View.GONE);
//                    imgDelete.setVisibility(View.VISIBLE);
//                    imgEditPhoto.setVisibility(View.VISIBLE);
//                    imagevideoLayout.setVisibility(View.VISIBLE);
//                    imgHeaderImage.setVisibility(View.VISIBLE);
//                    videoProgress.setVisibility(View.VISIBLE);
//                    Glide.with(ImageStoryListAdapter.this).load(tip.media).placeholder(R.drawable.media).listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            videoProgress.setVisibility(View.GONE);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            videoProgress.setVisibility(View.GONE);
//                            return false;
//                        }
//                    }).into(imgHeaderImage);
//                    break;
//                case "2":
//                    editCommentAttachment = 2;
//                    uri = Uri.parse(tip.media);
//                    imgCamera.setVisibility(View.GONE);
//                    imgEditPhoto.setVisibility(View.VISIBLE);
//                    imgDelete.setVisibility(View.VISIBLE);
//                    imagevideoLayout.setVisibility(View.VISIBLE);
//                    videoProgress.setVisibility(View.VISIBLE);
//                    imgHeaderImage.setVisibility(View.VISIBLE);
//                    Glide.with(ImageStoryListAdapter.this).load(tip.media_thumb_image).placeholder(R.drawable.media).listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            imagePlay.setVisibility(View.VISIBLE);
//                            videoProgress.setVisibility(View.GONE);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            imagePlay.setVisibility(View.VISIBLE);
//                            videoProgress.setVisibility(View.GONE);
//                            return false;
//                        }
//                    }).into(imgHeaderImage);
//                    break;
//            }
//        }
//
//        videoView.addCallback(new VideoSurfaceView.Callback() {
//            @Override
//            public void surfaceCreated(Surface surface) {
//                Log.e("ExoPlayer", "surfaceCreated");
//                if (leaveDialogExoPlayer != null) {
//                    leaveDialogExoPlayer.setSurface(surface);
//                }
//            }
//
//            @Override
//            public void surfaceChanged(int width, int height) {
//                Log.e("ExoPlayer", "surfaceChanged");
//            }
//
//            @Override
//            public void surfaceDestroyed() {
//                Log.e("ExoPlayer", "surfaceDestroyed");
//                if (leaveDialogExoPlayer != null) {
//                    leaveDialogExoPlayer.release();
//                    imgHeaderImage.setVisibility(View.VISIBLE);
//                    imagePlay.setVisibility(View.VISIBLE);
//                    imagePlay.setImageResource(R.drawable.play);
//                    imgEditPhoto.setVisibility(View.VISIBLE);
//                    isLeaveDialogVideoStarted = false;
//                    leaveDialogExoPlayer = null;
//                }
//            }
//        });
//
//        final String userAgent = Util.getUserAgent(ImageStoryListAdapter.this, "CICERONEMeApp");
//        imagePlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                videoProgress.setVisibility(View.VISIBLE);
//                if (!isLeaveDialogVideoStarted) {
//                    leaveDialogExoPlayer = new DemoPlayer(new ExtractorRendererBuilder(ImageStoryListAdapter.this, userAgent, uri));
//                    leaveDialogExoPlayer.prepare();
//                    leaveDialogExoPlayer.setSurface(videoView.getSurface());
//                    isLeaveDialogVideoStarted = true;
//                    leaveDialogExoPlayer.addListener(new DemoPlayer.Listener() {
//                        @Override
//                        public void onStateChanged(boolean playWhenReady, int playbackState) {
//                            switch (playbackState) {
//                                case ExoPlayer.STATE_BUFFERING:
//                                    videoProgress.setVisibility(View.VISIBLE);
//                                    break;
//                                case ExoPlayer.STATE_ENDED:
//                                    imgHeaderImage.setVisibility(View.VISIBLE);
//                                    imagePlay.setVisibility(View.VISIBLE);
//                                    imagePlay.setImageResource(R.drawable.play);
//                                    imgEditPhoto.setVisibility(View.VISIBLE);
//                                    isLeaveDialogVideoStarted = false;
//                                    leaveDialogExoPlayer.release();
//                                    leaveDialogExoPlayer = null;
//                                    break;
//                                case ExoPlayer.STATE_READY:
//                                    videoProgress.setVisibility(View.GONE);
//                                    imgHeaderImage.setVisibility(View.GONE);
//                                    break;
//
//                            }
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                            if (e instanceof UnsupportedDrmException) {
//                                // Special case DRM failures.
//                                UnsupportedDrmException unsupportedDrmException = (UnsupportedDrmException) e;
//                                int stringId = Util.SDK_INT < 18 ? R.string.drm_error_not_supported
//                                        : unsupportedDrmException.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
//                                        ? R.string.drm_error_unsupported_scheme : R.string.drm_error_unknown;
//                                Toast.makeText(ImageStoryListAdapter.this, stringId, Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//                        @Override
//                        public void onVideoSizeChanged(int width, int height, float pixelWidthHeightRatio, int rotateDegree) {
//                            setVideoSize(width,height);
//                            if (rotateDegree == 0) {
//                             /*The video is in the landscape mode , so no need to rotate.....*/
//                            } else if (rotateDegree == 90) {
//                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                                    surfaceView.setRotationEx(90);
//                                }
//                            }
//                        }
//                    });
//                }
//                    imagePlay.setVisibility(View.GONE);
//                    leaveDialogExoPlayer.setPlayWhenReady(true);
//           }
//        });
//
//        videoView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (leaveDialogExoPlayer.getPlayWhenReady()) {
//                    leaveDialogExoPlayer.setPlayWhenReady(false);
//                    imagePlay.setVisibility(View.VISIBLE);
//                }
//                return true;
//            }
//        });
//
//        imgEditPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onCreateDialogForAddPhoto();
//            }
//        });
//
//        dialogView.findViewById(R.id.img_camera).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onCreateDialogForAddPhoto();
//            }
//        });
//     /*   int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.60);
//        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.60);
//        alertDialog.getWindow().setLayout(width, height);*/
////        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        alertDialog.show();
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 1:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (isPermissionDefaults) {
//                        launchGallery();
//                    }
//                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(ImageStoryListAdapter.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    } else {
//                        if (isPermissionDefaults) {
//                            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                                promptSettings("sdCard");
//                            } else {
//                                promptSettings("sdCard");
//                            }
//                        }
//                    }
//                }
//                break;
//            case 2:
//
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (isPermissionDefaults) {
//                        launchCamera();
//                    }
//                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(ImageStoryListAdapter.this, Manifest.permission.CAMERA)) {
//                    } else {
//                        if (isPermissionDefaults) {
//                            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//                                promptSettings("Camera");
//                            } else {
//                                promptSettings("Camera");
//                            }
//                        }
//                    }
//                }
//
//                break;
//            case 3:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (isPermissionDefaults) {
//                        launchCameraForVideo();
//                    }
//                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(ImageStoryListAdapter.this, Manifest.permission.RECORD_AUDIO)) {
//                    } else {
//                        if (isPermissionDefaults) {
//                            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
//                                promptSettings("Record Audio");
//                            } else {
//                                promptSettings("Record Audio");
//                            }
//                        }
//                    }
//                }
//                break;
//            case 4:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (isPermissionDefaults) {
//                        checkMicroPhonePermision();
//                    }
//                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(ImageStoryListAdapter.this, Manifest.permission.CAMERA)) {
//                    } else {
//                        if (isPermissionDefaults) {
//                            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//                                promptSettings("Camera");
//                            } else {
//                                promptSettings("Camera");
//                            }
//                        }
//                    }
//                }
//
//                break;
//
//        }
//    }
//
//
//    private void setVideoSize(int width , int height) {
//        // Get the width of the screen
//        int screenWidth = BDevice.getScreenWidth(ImageStoryListAdapter.this);
//        int screenHeight = BDevice.getScreenHeight(ImageStoryListAdapter.this);
//        // // Get the dimensions of the video
//        int videoWidth = width;
//        int videoHeight = height;//screenHeight * 80 / 100;
//        float videoProportion = (float) videoWidth / (float) videoHeight;
//
//        float screenProportion = (float) screenWidth / (float) screenHeight;
//
//        // Get the SurfaceView layout parameters
//        ViewGroup.LayoutParams lp = videoView.getLayoutParams();
//        if (videoProportion > screenProportion) {
//            lp.width = screenWidth;
//            lp.height = (int) ((float) screenWidth / videoProportion);
//        } else {
//            lp.width = (int) (videoProportion * (float) screenHeight);
//            lp.height = screenHeight;
//        }
//
//        videoView.setLayoutParams(lp);
//    }
//
//    private void onCreateDialogForAddPhoto() {
//        mediaPlayer = null;
//        AlertDialog.Builder builder = getBuilder(ImageStoryListAdapter.this);
//        String[] listItem = {getString(R.string.take_photo), getString(R.string.take_video), getString(R.string.gallery)};
//        builder.setTitle(getResources().getString(R.string.select)).setItems(listItem, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (which == 0) {
//                    isPermissionDefaults = true;
//                    currentlyCliked = "camera";
//                    String permission = Manifest.permission.CAMERA;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (ContextCompat.checkSelfPermission(ImageStoryListAdapter.this, permission) == PackageManager.PERMISSION_GRANTED) {
//                            launchCamera();
//                        } else {
//                            checkCameraPermision();
//                        }
//                    } else {
//                        launchCamera();
//                    }
//
//                } else if (which == 1) {
//                    isPermissionDefaults = true;
//                    currentlyCliked = "video";
//                    String permission = Manifest.permission.CAMERA;
//                    String permissionAudio = Manifest.permission.RECORD_AUDIO;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                        if ((ContextCompat.checkSelfPermission(ImageStoryListAdapter.this, permission) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(ImageStoryListAdapter.this, permissionAudio) == PackageManager.PERMISSION_GRANTED)) {
//                            Log.d("PermiscnCheck", "in already given");
//
//                            launchCameraForVideo();
//                        } else {
//                            Log.d("PermiscnCheck", "in not already given");
//                            Log.d("PermiscnCheck", "in check perm");
//                            checkCameraForVideoPermision();
//                        }
//// else if ((ContextCompat.checkSelfPermission(AddMapActivity.this, permissionMIc) != PackageManager.PERMISSION_GRANTED)){
////                            checkMicroPhonePermision();
////                        }
//                    } else {
//                        launchCameraForVideo();
//                    }
//
//                } else if (which == 2) {
//                    isPermissionDefaults = true;
//                    currentlyCliked = "gallery";
//                    String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (ContextCompat.checkSelfPermission(ImageStoryListAdapter.this, permission) == PackageManager.PERMISSION_GRANTED) {
//                            launchGallery();
//                        } else {
//                            checkSDCardPermission();
//                        }
//                    } else {
//                        launchGallery();
//                    }
//                }
//                dialog.dismiss();
//            }
//        }).create().show();
//    }
//
//    private void launchGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/* video/*");
//        startActivityForResult(intent, RC_GALLERY);
//    }
//
//
//    private void launchCamera() {
//
//        String file = FileUtils.createMapImageOrVideoFile(this, "photo").getAbsolutePath();
//        if (file == null)
//            return;
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, (1024 * 1024));
//        // intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, 0);
//
//        picUri = FileProvider.getUriForFile(ImageStoryListAdapter.this, BuildConfig.APPLICATION_ID + ".provider", FileUtils.createMapImageOrVideoFile(this, "photo"));
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
//        List<ResolveInfo> resInfoList = getApplicationContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//        for (ResolveInfo resolveInfo : resInfoList) {
//            String packageName = resolveInfo.activityInfo.packageName;
//            getApplicationContext().grantUriPermission(packageName, picUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        }
//        startActivityForResult(intent, REQUEST_PHOTO);
//
//
//    }
//
//    private void launchCameraForVideo() {
//        Intent intent = new Intent(ImageStoryListAdapter.this, BTRecorderActivity.class);
//        startActivityForResult(intent, REQUEST_VIDEO);
//
//    }
//
//
//    public void checkMicroPhonePermision() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if ((checkSelfPermission(PERMISSIONS[2])) != PackageManager.PERMISSION_GRANTED) {
//                if (PERMISSIONS[2].equalsIgnoreCase(Manifest.permission.RECORD_AUDIO)) {
////                    requestPermissions(new String[]{PERMISSIONS[2]}, 3);
//                    requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 3);
//                }
//            }
//        }
//    }
//
//    public void checkCameraPermision() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if ((checkSelfPermission(PERMISSIONS[0])) != 0) {
//                //  requestPermissions(PERMISSIONS, 2);
//                if (isPermissionDefaults)
//                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 2);
//                else
//                    requestPermissions(PERMISSIONS, 2);
//            }
//        }
//    }
//
//    public void checkCameraForVideoPermision() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if ((checkSelfPermission(PERMISSIONS[0])) != 0) {
//                // requestPermissions(PERMISSIONS, 4);
//                requestPermissions(new String[]{Manifest.permission.CAMERA}, 4);
//            } else if ((checkSelfPermission(PERMISSIONS[2])) != 0) {
//                checkMicroPhonePermision();
//            }
//        }
//
//    }
//
//    private void checkSDCardPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//        }
//    }
//
//
//    private void callTrimActivity(File file) {
//        Intent intent = new Intent(this, TrimVideo.class);
//        intent.putExtra(TrimVideo.KEY_MEDIA_ITEM_PATH, file.getAbsolutePath());
//        startActivityForResult(intent, REQ_CODE_TRIM_VIDEO);
//    }
//
//    public class EditDataWithImage extends AsyncTask<String, Void, String> {
//        private String charset = "\"UTF-8\"";
//        private String commentId, comment;
//        private int type, isDeleteForWb;
//        private AlertDialog alertDialog;
//
//
//        public EditDataWithImage(String commentId, String comment, int type, int isDeleteForWb, AlertDialog alertDialog) {
//            this.commentId = commentId;
//            this.comment = comment;
//            this.type = type;
//            this.isDeleteForWb = isDeleteForWb;
//            this.alertDialog = alertDialog;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            if (ceProgressDialog != null && !isFinishing()) {
//                ceProgressDialog.show();
//            }
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String response = null;
//            try {
//
//                MultipartUtility multipart = new MultipartUtility(params[0], charset, getApp().getCnPrefernce());
//                multipart.addFormField("comments", TextUtils.encodeToBase64(comment));
//                multipart.addFormField("isdelete", String.valueOf(isDeleteForWb));
//                multipart.addFormField("type", String.valueOf(type));
//                if(isImageOrVideoPlaced) {
//                    if (editCommentAttachment == 1 || editCommentAttachment == 2) {
//                        File pictureFolder = new File(ImageStoryListAdapter.this.getExternalCacheDir(), "create_map/photo/map_photo.jpg");
//                        multipart.addFilePart("picture", pictureFolder);
//                    }
//                    if (editCommentAttachment == 2) {
//                        File videoFolder = new File(ImageStoryListAdapter.this.getExternalCacheDir(), "create_map/video/map_video.mp4");
//                        multipart.addFilePart("video", videoFolder);
//                    }
//                }
//                String responses = multipart.finish();
//                Log.e(TAG, "Response --> " + responses);
//            } catch (final MalformedURLException e) {
//                Log.e(TAG, "MalformedURLException: " + e.getMessage());
//            } catch (final ProtocolException e) {
//                Log.e(TAG, "ProtocolException: " + e.getMessage());
//            } catch (final IOException e) {
//                Log.e(TAG, "IOException: " + e.getMessage());
//            } catch (final Exception e) {
//                Log.e(TAG, "Exception: " + e.getMessage());
//            }
//            return response;
//
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            if (ceProgressDialog != null && ceProgressDialog.isShowing()) {
//                ceProgressDialog.dismiss();
//            }
//            isDelete = true;
//            selectedAttachment = 0;
//            isChangesDoneForEdit = false;
//            editCommentAttachment = 0;
//            checkComment = "";
//            isDeleteForEdit = 0;
//            alertDialog.dismiss();
//            commentList.clear();
//            getCommentListWebService(true, false, false);
//        }
//    }
//
//    public void promptSettings(String type) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(String.format(getResources().getString(R.string.denied_never_ask_title), type));
//        builder.setMessage(String.format(getString(R.string.denied_never_ask_msg), type));
//        builder.setPositiveButton("go to Settings", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                goToSettings();
//            }
//        });
//        // builder.setNegativeButton("Cancel", null);
//        builder.setCancelable(false);
//        builder.show();
//    }
//
//    public void goToSettings() {
//        mediaPlayer = null;
//        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + this.getPackageName()));
//        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
//        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        this.startActivity(myAppSettings);
//        flag = true;
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (flag) {
//            if (currentlyCliked != null) {
//                if (currentlyCliked.equalsIgnoreCase("camera")) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if ((checkSelfPermission(PERMISSIONS[0])) != 0) {
////                            requestPermissions(PERMISSIONS, 2);
//                            requestPermissions(new String[]{Manifest.permission.CAMERA}, 2);
//                        } else {
//                            launchCamera();
//                        }
//                    }
//
//                } else if (currentlyCliked.equalsIgnoreCase("video")) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (((checkSelfPermission(PERMISSIONS[0])) != 0) || ((checkSelfPermission(PERMISSIONS[2]))) != 0) {
//                            if (((checkSelfPermission(PERMISSIONS[1])) != 0)) {
////                                requestPermissions(PERMISSIONS, 4);
//                                requestPermissions(new String[]{Manifest.permission.CAMERA}, 4);
//                            } else if (((checkSelfPermission(PERMISSIONS[2])) != 0)) {
////                                requestPermissions(PERMISSIONS, 3);
//                                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 3);
//                            }
//                        } else {
//                            launchCameraForVideo();
//                        }
//                    }
//
//                } else if (currentlyCliked.equalsIgnoreCase("gallery")) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if ((checkSelfPermission(PERMISSIONS[1])) != 0) {
//                            // requestPermissions(PERMISSIONS, 1);
//                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                        } else {
//                            launchGallery();
//                        }
//                    }
//
//                }
//            }
//        }
//        flag = false;
//    }
//
//    private void setEditTextMaxLength(EditText edtTip, int length) {
//        InputFilter[] filterArray = new InputFilter[1];
//        filterArray[0] = new InputFilter.LengthFilter(length);
//        edtTip.setFilters(filterArray);
//    }
//
//    public class CopyStream extends AsyncTask<String, Void, String> {
//
//        Uri inputSteamUri;
//        String outputStreamLocation;
//        String inputStreamLocation;
//        CeProgressDialog ceProgressDialog;
//
//        CopyStream(Uri inputSteamUri, String outputStreamLocation, CeProgressDialog ceProgressDialog) {
//            this.inputSteamUri = inputSteamUri;
//            this.ceProgressDialog = ceProgressDialog;
//            this.inputStreamLocation = "";
//            this.outputStreamLocation = outputStreamLocation;
//        }
//
//        CopyStream(String inputStreamLocation, String outputStreamLocation, CeProgressDialog ceProgressDialog) {
//            this.inputSteamUri = null;
//            this.ceProgressDialog = ceProgressDialog;
//            this.inputStreamLocation = inputStreamLocation;
//            this.outputStreamLocation = outputStreamLocation;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            ceProgressDialog.show();
//            videoProgress.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                InputStream inputStream;
//                if (TextUtils.isEmpty(inputStreamLocation)) {
//                    inputStream = ImageStoryListAdapter.this.getContentResolver().openInputStream(inputSteamUri);
//                } else {
//                    inputStream = new FileInputStream(inputStreamLocation);
//                }
//                FileOutputStream fileOutputStream = new FileOutputStream(outputStreamLocation);
//                FileUtils.copyStream(inputStream, fileOutputStream);
//                fileOutputStream.close();
//                inputStream.close();
//            } catch (Exception e) {
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            ceProgressDialog.dismiss();
//            if (ImageStoryListAdapter.this != null) {
//                try {
//                    mediaPlayer = null;
//                    if (!TextUtils.isNullOrEmpty(outputStreamLocation)) {
//
//                        if (outputStreamLocation.endsWith(".mp4")) {
//                            selectedAttachment = 2;
//                            isImageOrVideoPlaced = true;
//                            isChangesDoneForEdit = true;
//                            editCommentAttachment = 2;
//                            isDeleteForEdit = 0;
//                            imgDelete.setVisibility(View.VISIBLE);
//                            File videoDirectory = new File(getApplicationContext().getExternalCacheDir(), "create_map/video");
//                            String latestVideoPath = FileUtils.getLatestFileFromDir(videoDirectory.getPath());
//                            Log.d("latestVideoPath", "" + latestVideoPath.toString());
//                            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(latestVideoPath,
//                                    MediaStore.Images.Thumbnails.MINI_KIND);
//                            imgHeaderImage.setImageBitmap(thumb);
//                            imagevideoLayout.setVisibility(View.VISIBLE);
//                            uri = Uri.parse(latestVideoPath);
//                            imgHeaderImage.setVisibility(View.VISIBLE);
//                            imagePlay.setVisibility(View.VISIBLE);
//                            imgEditPhoto.setVisibility(View.VISIBLE);
//                            videoProgress.setVisibility(View.GONE);
////                            checkDeleteIconVisibility();
//                            //save teh thump in photo location
//
//                            String thumpOutputStream = null;
//                            thumpOutputStream = FileUtils.createMapImageOrVideoFile(ImageStoryListAdapter.this, "photo").getAbsolutePath();
//                            FileOutputStream out = null;
//                            try {
//                                out = new FileOutputStream(thumpOutputStream);
//                                thumb.compress(Bitmap.CompressFormat.PNG, 100, out);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            } finally {
//                                try {
//                                    if (out != null) {
//                                        out.close();
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//
//                        } else {
//                            selectedAttachment = 1;
//                            isImageOrVideoPlaced = true;
//                            isChangesDoneForEdit = true;
//                            editCommentAttachment = 1;
//                            isDeleteForEdit = 0;
//                            File imageDir = new File(getApplicationContext().getExternalCacheDir(), "create_map/photo");
//                            String latestphotopath = FileUtils.getLatestFileFromDir(imageDir.getPath());
//                            bmp = BitmapFactory.decodeFile(latestphotopath);
//                            imgDelete.setVisibility(View.VISIBLE);
//                            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
//                            bmp.compress(Bitmap.CompressFormat.JPEG, 60, bos2);
////                            checkDeleteIconVisibility();
//                            imgHeaderImage.setImageBitmap(bmp);
//                            imagevideoLayout.setVisibility(View.VISIBLE);
//                            videoView.setVisibility(View.GONE);
//                            imgHeaderImage.setVisibility(View.VISIBLE);
//
//                            imagePlay.setVisibility(View.GONE);
//                            imgEditPhoto.setVisibility(View.VISIBLE);
//                            videoProgress.setVisibility(View.GONE);
//                        }
//                    }
//                } catch (IllegalArgumentException e) {
//
//                }
//
//            }
//        }
//
//    }
//
//
//
//}
