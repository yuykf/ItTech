package com.fungo.xiaokebang.ui.video;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.aliyun.common.utils.CommonUtil;
import com.aliyun.recorder.AliyunRecorderCreator;
import com.aliyun.recorder.supply.AliyunIClipManager;
import com.aliyun.recorder.supply.AliyunIRecorder;
import com.aliyun.recorder.supply.RecordCallback;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.encoder.VideoCodecs;
import com.aliyun.svideo.sdk.external.struct.recorder.CameraParam;
import com.aliyun.svideo.sdk.external.struct.recorder.CameraType;
import com.aliyun.svideo.sdk.external.struct.recorder.MediaInfo;
import com.aliyun.svideo.sdk.external.struct.snap.AliyunSnapVideoParam;
import com.fungo.xiaokebang.R;
import com.fungo.xiaokebang.base.fragment.BaseFragment;
import com.fungo.xiaokebang.utils.FastClickUtil;
import com.fungo.xiaokebang.utils.OrientationDetector;
import com.fungo.xiaokebang.utils.PermissionUtils;
import com.fungo.xiaokebang.utils.ThreadUtils;
import com.fungo.xiaokebang.utils.UriUtils;
import com.qu.preview.callback.OnFrameCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.fungo.xiaokebang.app.Constants;
import com.fungo.xiaokebang.contract.video.MakeVideoContract;
import com.fungo.xiaokebang.presenter.video.MakeVideoPresenter;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/29.
 */
public class MakeVideoFragment extends BaseFragment<MakeVideoPresenter> implements MakeVideoContract.View, View.OnTouchListener {

    /**
     * 权限申请
     */
    String[] permission = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private final String TAG = "MakeVideoFragment";

    @BindView(R.id.aliyun_preview)
    SurfaceView mSurfaceView;
    @BindView(R.id.btn_controll)
    TextView ivControll;
    @BindView(R.id.btn_delete)
    TextView ivClear;
    @BindView(R.id.btn_commit)
    TextView ivCommit;

    @BindView(R.id.tv_time)
    TextView mRecordTimeTxt; //
    public static final String RESULT_TYPE = "result_type";
    public static final int RESULT_TYPE_CROP = 4001;
    public static final int RESULT_TYPE_RECORD = 4002;

    private AliyunIRecorder mRecorder;

    //Camera Confit data
    private final int RESOLUTIONMODE = AliyunSnapVideoParam.RESOLUTION_720P;
    private final boolean ISBEAUTYON = false;
    private final int MIN_DURATION = 1000;
    private final int MAX_DURATION = 1000 * 60 * 60; //一小时
    private final int RATIOMODE = AliyunSnapVideoParam.RATIO_MODE_1_1;
    private final int GOP = 250;
    private final VideoQuality VIDEOQUALITY = VideoQuality.SSD;
    private final VideoCodecs VIDEOCODEC = VideoCodecs.H264_HARDWARE; //解码方式
    private int isNeedGallery;
    private final boolean ISNEEDCLIP = true;
    private boolean isOpenFailed;
    private CameraType CAMERATYPE = CameraType.FRONT;
    public static final String OUTPUT_PATH = "output_path";

    public static MakeVideoFragment newInstance() {
        Bundle args = new Bundle();
        MakeVideoFragment fragment = new MakeVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOrientationDetector();
        setCameraParams();
        initSDK();
        reSizePreview();

    }

    private void reSizePreview() {
        RelativeLayout.LayoutParams previewParams = null;
        RelativeLayout.LayoutParams timeLineParams = null;
        RelativeLayout.LayoutParams durationTxtParams = null;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        switch (RATIOMODE) {
            case AliyunSnapVideoParam.RATIO_MODE_1_1:
//                previewParams = new RelativeLayout.LayoutParams(screenWidth, screenWidth);
//                previewParams.addRule(RelativeLayout.BELOW, R.id.aliyun_tools_bar);
//                timeLineParams = new RelativeLayout.LayoutParams(screenWidth, TIMELINE_HEIGHT);
//                timeLineParams.addRule(RelativeLayout.BELOW, R.id.aliyun_preview);
//                durationTxtParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                durationTxtParams.addRule(RelativeLayout.ABOVE, R.id.aliyun_record_timeline);
//                timeLineParams.topMargin = -mTimelinePosY;
//                mToolBar.setBackgroundColor(getResources().getColor(R.color.alivc_common_bg_transparent));
//                mRecorderBar.setBackgroundColor(getResources().getColor(R.color.alivc_common_bg_transparent));
//                mRecordTimelineView.setColor(mTintColor, mTimelineDelBgColor, R.color.alivc_common_bg_black_alpha_70, mTimelineBgColor);
                break;
            case AliyunSnapVideoParam.RATIO_MODE_3_4:
//                int barHeight = getVirtualBarHeight();
//                float ratio = (float) screenHeight / screenWidth;
//                previewParams = new RelativeLayout.LayoutParams(screenWidth, screenWidth * 4 / 3);
//                if (barHeight > 0 || ratio < (16f / 9.2f)) {
//                    mToolBar.setBackgroundColor(getResources().getColor(R.color.alivc_record_bg_tools_bar));
//                } else {
//                    previewParams.addRule(RelativeLayout.BELOW, R.id.aliyun_tools_bar);
//                    mToolBar.setBackgroundColor(getResources().getColor(R.color.alivc_common_bg_transparent));
//                }
//                timeLineParams = new RelativeLayout.LayoutParams(screenWidth, TIMELINE_HEIGHT);
//                timeLineParams.addRule(RelativeLayout.BELOW, R.id.aliyun_preview);
//                durationTxtParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                durationTxtParams.addRule(RelativeLayout.ABOVE, R.id.aliyun_record_timeline);
//                timeLineParams.topMargin = -mTimelinePosY;
//                mRecorderBar.setBackgroundColor(getResources().getColor(R.color.alivc_common_bg_transparent));
//                mRecordTimelineView.setColor(mTintColor, mTimelineDelBgColor, R.color.alivc_common_bg_black_alpha_70, mTimelineBgColor);
                break;
            case AliyunSnapVideoParam.RATIO_MODE_9_16:
//                previewParams = new RelativeLayout.LayoutParams(screenWidth, screenWidth * 16 / 9);
//                if (previewParams.height > screenHeight) {
//                    previewParams.height = screenHeight;
//                }
//                timeLineParams = new RelativeLayout.LayoutParams(screenWidth, TIMELINE_HEIGHT);
//                timeLineParams.addRule(RelativeLayout.ABOVE, R.id.aliyun_record_layout);
//                timeLineParams.bottomMargin = mTimelinePosY;
//                durationTxtParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                durationTxtParams.addRule(RelativeLayout.ABOVE, R.id.aliyun_record_timeline);
//                mToolBar.setBackgroundColor(getResources().getColor(R.color.alivc_record_bg_tools_bar));
//                mRecorderBar.setBackgroundColor(getResources().getColor(R.color.alivc_record_bg_tools_bar));
//                mRecordTimelineView.setColor(mTintColor, mTimelineDelBgColor, R.color.alivc_common_bg_black_alpha_70, R.color.alivc_common_bg_transparent);
                break;
            default:
                break;
        }
        if (previewParams != null) {
            mSurfaceView.setLayoutParams(previewParams);
        }
//        if (timeLineParams != null) {
//            mRecordTimelineView.setLayoutParams(timeLineParams);
//        }
        if (durationTxtParams != null) {
            mRecordTimeTxt.setLayoutParams(durationTxtParams);
        }
    }


    private void initSDK() {
        mRecorder = AliyunRecorderCreator.getRecorderInstance(_mActivity);
        mRecorder.setDisplayView(mSurfaceView);
        mRecorder.setOnFrameCallback(new OnFrameCallBack() {
            @Override
            public void onFrameBack(byte[] bytes, int i, int i1, Camera.CameraInfo cameraInfo) {
//                Log.e(TAG, "openSuccess");
                isOpenFailed = false;
            }

            @Override
            public Camera.Size onChoosePreviewSize(List<Camera.Size> list, Camera.Size size) {
                return null;
            }

            @Override
            public void openFailed() {
                Log.e(TAG, "openFailed");
                isOpenFailed = true;
            }
        });

        mClipManager = mRecorder.getClipManager();
        mClipManager.setMinDuration(MIN_DURATION);
        mClipManager.setMaxDuration(MAX_DURATION);
//        mRecordTimelineView.setMaxDuration(mClipManager.getMaxDuration());
//        mRecordTimelineView.setMinDuration(mClipManager.getMinDuration());
        int[] resolution = getResolution();
        final MediaInfo info = new MediaInfo();
        info.setVideoWidth(resolution[0]);
        info.setVideoHeight(resolution[1]);
        info.setVideoCodec(VIDEOCODEC);
        info.setCrf(25);
        mRecorder.setMediaInfo(info);
        CAMERATYPE = mRecorder.getCameraCount() == 1 ? CameraType.BACK : CAMERATYPE;
        mRecorder.setCamera(CAMERATYPE);
        mRecorder.setGop(GOP);
        mRecorder.setVideoQuality(VIDEOQUALITY);

        mRecorder.setRecordCallBack(new RecordCallback() {
            @Override
            public void onComplete(boolean validClip, long clipDuration) {
                Log.d(TAG, "onComplete");
                handleRecordCallback(validClip, clipDuration);
                if (isOnMaxDuration) {
                    isOnMaxDuration = false;
                    toEditor();

                }
                if (!isNeedClip) {
                    toEditor();
                }
            }

            @Override
            public void onFinish(String outputPath) {
                Log.d(TAG, "onFinish");
                isRecordFinish = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //适配android Q
                    ThreadUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            UriUtils.saveVideoToMediaStore(_mActivity.getApplicationContext(), outputPath);
                        }
                    });
                } else {
                    scanFile(outputPath);
                }
                mClipManager.deleteAllPart();
                Intent intent = new Intent();
                intent.putExtra(OUTPUT_PATH, outputPath);
                intent.putExtra(RESULT_TYPE, RESULT_TYPE_RECORD);
//                setResult(Activity.RESULT_OK, intent);
//                finish();
            }

            @Override
            public void onProgress(long duration) {
                _mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mRecordTimelineView.setDuration((int) duration);
                        int time = (int) (mClipManager.getDuration() + duration) / 1000;
                        int min = time / 60;
                        int sec = time % 60;
                        mRecordTimeTxt.setText(String.format("%1$02d:%2$02d", min, sec));
                        if (mRecordTimeTxt.getVisibility() != View.VISIBLE) {
                            mRecordTimeTxt.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onMaxDuration() {
                Log.d(TAG, "onMaxDuration");
                isOnMaxDuration = true;
            }

            @Override
            public void onError(int i) {
                isRecordError = true;
                handleRecordCallback(false, 0);
            }

            @Override
            public void onInitReady() {
                Log.e(TAG, "onInitReady: " + "");
            }

            @Override
            public void onDrawReady() {

            }

            @Override
            public void onPictureBack(Bitmap bitmap) {

            }

            @Override
            public void onPictureDataBack(byte[] bytes) {

            }
        });


        setRecordMode(mRecordMode);
//        setFilterList(getIntent().getStringArrayExtra(AliyunSnapVideoParam.FILTER_LIST));
//        mBeautyLevel = getIntent().getIntExtra(AliyunSnapVideoParam.BEAUTY_LEVEL, 80);
//        setBeautyLevel(mBeautyLevel);
//        setBeautyStatus(getIntent().getBooleanExtra(AliyunSnapVideoParam.BEAUTY_STATUS, true));
//        setFlashType((FlashType) getIntent().getSerializableExtra(AliyunSnapVideoParam.FLASH_TYPE));
        mRecorder.setExposureCompensationRatio(mExposureCompensationRatio);
//        mIvRecordFocusView.setProgress(mExposureCompensationRatio);
        mRecorder.setFocusMode(CameraParam.FOCUS_MODE_CONTINUE);

    }


    /**
     * 曝光度
     */
    private float mExposureCompensationRatio = 0.5f;


    private void scanFile(String path) {
        MediaScannerConnection.scanFile(_mActivity.getApplicationContext(),
                new String[] {path}, new String[] {"video/mp4"}, null);
    }

    private void toEditor() {
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIsToEditor = false;
                //停止拍摄
                mRecorder.finishRecording();
            }
        });
    }

    private boolean isNeedClip;
    private boolean mIsToEditor;
    private boolean isOnMaxDuration;

    private void handleRecordCallback(final boolean validClip, final long clipDuration) {
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivControll.setActivated(false);
                ivControll.setHovered(false);
                ivControll.setSelected(false);
//                if (validClip) {
//                    mRecordTimelineView.setDuration((int) clipDuration);
//                    mRecordTimelineView.clipComplete();
//                } else {
//                    mRecordTimelineView.setDuration(0);
//                }
                Log.e("validClip", "validClip : " + validClip);
                mRecordTimeTxt.setVisibility(View.GONE);
                ivCommit.setEnabled(true);
                ivClear.setEnabled(true);
                showComplete();
                isRecording = false;
            }
        });

    }

    private void showComplete() {
        if (mClipManager.getDuration() > mClipManager.getMinDuration()) {
            ivCommit.setActivated(true);
        } else {
            ivCommit.setActivated(false);
        }
    }


    //根据分辨率和 画面宽高比 计算 拍摄画面显示
    private int[] getResolution() {
        int[] resolution = new int[2];
        int width = 0;
        int height = 0;
        switch (RESOLUTIONMODE) {
            case AliyunSnapVideoParam.RESOLUTION_360P:
                width = 360;
                break;
            case AliyunSnapVideoParam.RESOLUTION_480P:
                width = 480;
                break;
            case AliyunSnapVideoParam.RESOLUTION_540P:
                width = 540;
                break;
            case AliyunSnapVideoParam.RESOLUTION_720P:
                width = 720;
                break;
            default:
                break;
        }
        switch (RATIOMODE) {
            case AliyunSnapVideoParam.RATIO_MODE_1_1:
                height = width;
                break;
            case AliyunSnapVideoParam.RATIO_MODE_3_4:
                height = width * 4 / 3;
                break;
            case AliyunSnapVideoParam.RATIO_MODE_9_16:
                height = width * 16 / 9;
                break;
            default:
                height = width;
                break;
        }
        resolution[0] = width;
        resolution[1] = height;
        return resolution;
    }

    private void setCameraParams() {
//        mVideoParam = new AliyunVideoParam.Builder()
//                .gop(GOP)
//                .frameRate(25)
//                .videoQuality(VIDEOQUALITY)
//                .videoCodec(VIDEOCODEC)
//                .build();


        //
//        mSurfaceView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mIvRecordFocusView.setDrawingSize(mSurfaceView.getMeasuredWidth(), mSurfaceView.getMeasuredHeight());
//            }
//        });

    }

    private boolean isRecording = false;
    private AliyunIClipManager mClipManager;
//    private RecordTimelineView mRecordTimelineView;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    public void onStart() {
        super.onStart();
        ivControll.setOnTouchListener(this);
        Log.d("MyGlSurfaceView", "onStart");
        Log.e(TAG, (ivControll == null) + " r");
    }

    @Override
    public void onResume() {
        super.onResume();
        ivControll.setOnTouchListener(this);
        Log.d("MyGlSurfaceView", "onResume");
        /**
         * 部分android4.4机型会出现跳转Activity gl为空的问题，如果不需要适配，显示视图代码可以去掉
         */
        mSurfaceView.setVisibility(View.VISIBLE);
        mRecorder.startPreview();

    }

    private OrientationDetector mOrientationDetector;

    private void initOrientationDetector() {
        mOrientationDetector = new OrientationDetector(_mActivity.getApplicationContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isRecording) {
            mRecorder.cancelRecording();
            isRecording = false;
        }
        mRecorder.stopPreview();

        // 部分android4.4机型会出现跳转Activity gl为空的问题，如果不需要适配，隐藏视图代码可以去掉
        mSurfaceView.setVisibility(View.INVISIBLE);

    }


    @Override
    public void onStop() {
        super.onStop();
        if (mOrientationDetector != null) {
            mOrientationDetector.disable();
        }


    }

    //afger onresume
    @Override
    protected void initEventAndData() {

    }

    private int mRecordMode = AliyunSnapVideoParam.RECORD_MODE_AUTO;

    public void setRecordMode(int recordMode) {
        this.mRecordMode = recordMode;
    }


    @OnClick({R.id.btn_controll, R.id.btn_delete, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_controll:
                //拍摄控制
                if (isOpenFailed) {
                    Toast.makeText(_mActivity, "请开启摄像头/存储权限", Toast.LENGTH_SHORT).show();
                    return;
                }
               break;
            case R.id.btn_delete:
                //清除拍摄


                break;
            case R.id.btn_commit:
                //提交拍摄


                break;

        }
    }

    private boolean checkIfStartRecording() {
        if (ivControll.isActivated()) {
            return false;
        }
        if (CommonUtil.SDFreeSize() < 50 * 1000 * 1000) {
            Toast.makeText(_mActivity, "剩余磁盘空间不足", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //拍摄控制
        if (isOpenFailed) {
            Toast.makeText(_mActivity, "请开启摄像头/存储权限", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (mRecordMode == AliyunSnapVideoParam.RECORD_MODE_TOUCH) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (!isRecording) {
                    if (!checkIfStartRecording()) {
                        return false;
                    }
                    ivControll.setHovered(true);
                    startRecording();
                    isRecording = true;
                } else {
                    //  stopRecording();
                    isRecording = false;
                }
            }
        } else if (mRecordMode == AliyunSnapVideoParam.RECORD_MODE_PRESS) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (!checkIfStartRecording()) {
                    return false;
                }
                ivControll.setSelected(true);
                startRecording();
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                stopRecording();
            }
        } else if (mRecordMode == AliyunSnapVideoParam.RECORD_MODE_AUTO) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mDownTime = System.currentTimeMillis();
                if (!isRecording) {
                    if (FastClickUtil.isFastClick()) {
                        return true;
                    }
                    if (!checkIfStartRecording()) {
                        return false;
                    }

                    ivControll.setPressed(true);
                    startRecording();

                    ivControll.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (ivControll.isPressed()) {
                                ivControll.setSelected(true);
                                ivControll.setHovered(true);
                            }
                        }
                    }, 200);
                    isRecording = true;
                } else {
                    stopRecording();
                    isRecording = false;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                long timeOffset = System.currentTimeMillis() - mDownTime;
                ivControll.setPressed(false);
                if (timeOffset > 1000) {
                    stopRecording();
                    isRecording = false;
                } else {
                    if (!isRecordError) {
                        ivControll.setSelected(false);
                        ivControll.setHovered(true);
                    } else {
                        isRecording = false;
                    }
                }
            }
        }
        return true;
    }

    private long mDownTime;

    private boolean isRecordError;

    private void stopRecording() {
        if (!isRecordFinish) {
            mRecorder.stopRecording();
        }
//        handleRecordStop();
    }



    public static final int PERMISSION_REQUEST_CODE = 1001;


        //开始拍摄
        private void startRecording() {
            if (!PermissionUtils.checkPermissionsGroup(_mActivity, permission)) {
                PermissionUtils.requestPermissions(_mActivity, permission, PERMISSION_REQUEST_CODE);
                return;
            }

            String videoPath = Constants.SDCardConstants.getDir(_mActivity) + System.currentTimeMillis() + ".mp4";
            mRecorder.setOutputPath(videoPath);
            //执行了start但是不执行，mRecorder.startRecording();
            handleRecordStart();
            mRecorder.setRotation(getPictureRotation());
            isRecordError = false;
            if (!isRecordFinish) {
                mRecorder.startRecording();
                Log.d(TAG, "startRecording");
            }

    }

    private int getPictureRotation() {
        int orientation = mOrientationDetector.getOrientation();
        int rotation = 90;
        if ((orientation >= 45) && (orientation < 135)) {
            rotation = 180;
        }
        if ((orientation >= 135) && (orientation < 225)) {
            rotation = 270;
        }
        if ((orientation >= 225) && (orientation < 315)) {
            rotation = 0;
        }
        if (Camera.getNumberOfCameras() > CAMERATYPE.getType()) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(CAMERATYPE.getType(), cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                if (rotation != 0) {
                    rotation = 360 - rotation;
                }
            }
        }
        Log.d("MyOrientationDetector", "generated rotation ..." + rotation);
        return rotation;
    }

    /**
     * 添加标志，录制结束后不允许调用startRecord,否则会出现下次进入录制的视频全部为不可用
     */
    private boolean isRecordFinish;

    private void handleRecordStart() {
        ivControll.setActivated(true);
        ivCommit.setVisibility(View.VISIBLE);
        ivClear.setVisibility(View.VISIBLE);
        ivCommit.setEnabled(false);
        ivClear.setEnabled(false);
        ivClear.setActivated(false);
        isSelected = false;
    }
    private boolean isSelected = false;

}
