package com.fungo.xiaokebang.ui.main;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.sdk.android.vod.upload.VODSVideoUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODSVideoUploadClient;
import com.alibaba.sdk.android.vod.upload.VODUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODUploadClient;
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.alibaba.sdk.android.vod.upload.session.VodSessionCreateInfo;
import com.aliyun.common.utils.CommonUtil;
import com.aliyun.common.utils.ToastUtil;
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
import com.bumptech.glide.Glide;
import com.fungo.xiaokebang.R;
import com.fungo.xiaokebang.app.Constants;
import com.fungo.xiaokebang.base.activity.BaseActivity;
import com.fungo.xiaokebang.contract.video.MakeVideoContract;
import com.fungo.xiaokebang.core.bean.PPTContent;
import com.fungo.xiaokebang.core.bean.PPTPreView;
import com.fungo.xiaokebang.core.dao.PPTHistoryData;
import com.fungo.xiaokebang.presenter.video.MakeVideoPresenter;
import com.fungo.xiaokebang.ui.adapter.PreItemDecoration;
import com.fungo.xiaokebang.ui.adapter.VideoPreAdapter;
import com.fungo.xiaokebang.ui.ppt.MakePPTFragment;
import com.fungo.xiaokebang.ui.ppt.SampleAdapter;
import com.fungo.xiaokebang.utils.FastClickUtil;
import com.fungo.xiaokebang.utils.OrientationDetector;
import com.fungo.xiaokebang.utils.PermissionUtils;
import com.fungo.xiaokebang.utils.ThreadUtils;
import com.fungo.xiaokebang.utils.UriUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qu.preview.callback.OnFrameCallBack;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class VideoActivity extends BaseActivity<MakeVideoPresenter> implements MakeVideoContract.View {


    VODSVideoUploadClient mVODSVideoUploadClient;  //上传对象
    VODUploadClient mVODUploadClient;
    VODUploadCallback mVODUploadCallback;
    List<PPTContent> pptContentDatas;

    /**
     * 权限申请
     */
    String[] permission = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private final String TAG = "VideoActivity";
    @BindView(R.id.aliyun_preview)
    SurfaceView mSurfaceView;
    @BindView(R.id.tv_time)
    TextView mRecordTimeTxt;
    @BindView(R.id.btn_controll)
    Button btnControll;
    @BindView(R.id.btn_delete)
    Button btnClear;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.iv_content)  //下半屏 iv
            ImageView iv_content;
    @BindView(R.id.ppt_video) //下半屏ppt 视频
            VideoView pptVideo;
    @BindView(R.id.pre_video)  //录制视频
            VideoView luzhiVideo;
    @BindView(R.id.btn_continue)
    LinearLayout btnContinue;
    @BindView(R.id.iv_pre_stop)
    ImageView ivPreStop;
    @BindView(R.id.ll_no_video)
    LinearLayout ll_noViewo;
    @BindView(R.id.tv_startluzhi)
    TextView tvStartRecord;
    @BindView(R.id.iv_novideo_bg)
    ImageView iv_novideo_bg;

    List<PPTPreView> pptPreViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOrientationDetector();
        getPPTDatas();
        initUpLoad();
        initSDK();
        initRv();
    }

    private int allGroupNum = 0;  //全部组数
    private PPTContent nowVideoContent; //
    private int nowPPTindex = 0; //对应拍摄ppt 页面的下标
    private int nowPPTGroupIndex = 0;// ppt页面 对应默认组号
    private int nowGroupNums = 0; //当前ppt 页面个数

    private void initRv() {

        nowVideoContent = pptPreViews.get(nowPPTGroupIndex).mContentList.get(nowPPTindex);
        nowVideoContent.isLuzhiing = true;
        if (null == nowVideoContent) {
            return;
        }
        allGroupNum = pptPreViews.size(); //全部组数
        nowGroupNums = pptPreViews.get(0).mContentList.size(); //ppt 页面数

        preAdapter = new VideoPreAdapter(VideoActivity.this, pptPreViews, new VideoPreAdapter.VideoPreClickListener() {
            @Override
            public void clickItem(int gp, int ip) {
                Log.e("sizee: ", " gp" + gp + "  ip: " + ip);
                getNewContentPPT(gp, ip);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(VideoActivity.this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rv_pre.setLayoutManager(layoutManager);
        rv_pre.setAdapter(preAdapter);

    }

    @BindView(R.id.tv_reback_luzhi)
    Button tv_reback_luzhi;

    //通过点击获取拍摄页面
    private void getNewContentPPT(int gp, int ip) {
        if (allGroupNum < gp + 1 || gp < 0 || ip < 0) {
            return;
        }


        if (nowVideoContent.isLuzhiing && hasRecoded) {

            btnControll.setVisibility(View.GONE);
            btnCommit.setVisibility(View.GONE);
            btnClear.setVisibility(View.GONE);

            mRecordTimeTxt.setVisibility(View.GONE);
            tv_reback_luzhi.setVisibility(View.GONE);
            ll_noViewo.setVisibility(View.GONE);
            btnContinue.setVisibility(View.VISIBLE);
            iv_novideo_bg.setVisibility(View.GONE);

            //
            for (int i = 0; i < pptPreViews.size(); i++) {
//                if (i == nowPPTGroupIndex)
//                    continue;
                for (int j = 0; j < pptPreViews.get(i).mContentList.size(); j++) {
//                    if (j == nowPPTindex)
//                        continue;
                    pptPreViews.get(i).mContentList.get(j).isClicked = false;
                }
            }

            if (gp != nowPPTGroupIndex) {
                ToastUtil.showToast(VideoActivity.this,"当前页面未录制完成");
                //
                pptPreViews.get(gp).mContentList.get(ip).isClicked = true;
            }
            preAdapter.notifyDataSetChanged();
            showDownShow(gp, ip); //
            return;
        }

        //当有其他页面在拍摄态 则显示回复录制
        if (hasRecoded) {
            pptPreViews.get(gp).mContentList.get(ip).isClicked = true;
            preAdapter.notifyDataSetChanged();

            tv_reback_luzhi.setVisibility(View.VISIBLE);

            //
            btnControll.setVisibility(View.GONE);
            btnCommit.setVisibility(View.GONE);
            btnContinue.setVisibility(View.GONE);
            btnClear.setVisibility(View.GONE);
            ll_noViewo.setVisibility(View.GONE);
            iv_novideo_bg.setVisibility(View.GONE);

            showDownShow(gp, ip);
            return;
        }

        if (allGroupNum >= gp + 1) {
            //组号满足条件
            //自动拍摄下一页ppt 当前
            nowPPTGroupIndex = gp;
            nowPPTindex = ip;
            nowGroupNums = pptPreViews.get(nowPPTGroupIndex).mContentList.size();
            nowVideoContent = pptPreViews.get(gp).mContentList.get(ip); //获取同组下页ppt

            if (!nowVideoContent.isLuzhiing) {
                for (PPTPreView pptPreView : pptPreViews) {
                    pptPreView.isSelected = false;
                    for (PPTContent pptContent : pptPreView.mContentList) {
                        pptContent.isLuzhiing = false;
                        pptContent.isClicked = false;
                    }
                }
                if (TextUtils.isEmpty(nowVideoContent.videoUrl)) {
                    //待拍摄
                    if (isRecordFinish || !isStoped) {
                        //已经提交 或者 非暂停态
                        //当前 ppt  未拍视频
                        //该ppt 还未拍 视频  pptindex + 1
                        //展示view
                        nowVideoContent.isLuzhiing = true;

                        ll_noViewo.setVisibility(View.VISIBLE);
                        iv_novideo_bg.setVisibility(View.VISIBLE);

                        btnContinue.setVisibility(View.GONE);
                        btnClear.setVisibility(View.GONE);
                        btnControll.setVisibility(View.GONE);
                        btnCommit.setVisibility(View.GONE);
                    }

                } else {
                    //预览
                    pptPreViews.get(gp).mContentList.get(ip).isClicked = true;
                    videoPath = pptPreViews.get(gp).mContentList.get(ip).videoUrl;
                    diffVideoVisable(true);  //
                    btnContinue.setVisibility(View.GONE);

                }
            } else {
                //录制页
                diffVideoVisable(false);
            }

//            nowVideoContent.isLuzhiing = true; //拍摄dot
            //下半屏
            showDownShow(gp, ip);
            pptPreViews.get(gp).isSelected = true;
            preAdapter.notifyDataSetChanged();

        } else {
            ToastUtil.showToast(VideoActivity.this, "拍摄完毕");
        }
    }

    private void setOutRvBg(int gp, int ip) {
        //限制组号下标越界
        if (allGroupNum < gp + 1 || gp < 0 || ip < 0) {
            return;
        }
        //点击
        ToastUtil.showToast(VideoActivity.this, " gi: " + gp + " ii: " + ip);
        for (PPTPreView pptPreView : pptPreViews) {
            pptPreView.isSelected = false;
        }

        for (PPTPreView pptPreView : pptPreViews) {
            for (PPTContent pptContent : pptPreView.mContentList) {
                pptContent.isLuzhiing = false;
                pptContent.isClicked = false;
            }
        }

        nowVideoContent.isLuzhiing = true; //拍摄dot
        //预览之前拍摄
        if (!TextUtils.isEmpty(pptPreViews.get(gp).mContentList.get(ip).videoUrl)) {
            pptPreViews.get(gp).mContentList.get(ip).isClicked = true;
            diffVideoVisable(true);  //改变
            videoPath = pptPreViews.get(gp).mContentList.get(ip).videoUrl;
        }

        //下半屏
        showDownShow(gp, ip);
        pptPreViews.get(gp).isSelected = true;
        preAdapter.notifyDataSetChanged();

    }

    //下半屏同步
    private void showDownShow(int gp, int ip) {
        PPTContent pptContent = pptPreViews.get(gp).mContentList.get(ip);
        int typework = -1;
        if (null != pptContent) {
            String contentStr = pptContent.contentStr;
            String contentType = "";
            if (!TextUtils.isEmpty(contentStr)) {
                //
                contentType = MakePPTFragment.getContentType(contentStr);
            } else {
                String contentTypeWork = pptContent.contentType;

                try {
                    typework = Integer.valueOf(contentTypeWork);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (typework > 0) {
                    contentType = MakePPTFragment.TYPE_WORK;
                }
            }
            switch (contentType) {
                case MakePPTFragment.TYPE_IMAGE:
                    iv_content.setVisibility(View.VISIBLE);
                    pptVideo.setVisibility(View.GONE);

                    Glide.with(VideoActivity.this).load(contentStr).into(iv_content);
                    break;
                case MakePPTFragment.TYPE_VIDEO:
                    iv_content.setVisibility(View.GONE);
                    pptVideo.setVisibility(View.VISIBLE);
                    //下半屏
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && null != pptVideo && !TextUtils.isEmpty(contentStr)) {
                        File file = new File(contentStr);
                        Log.e("onError: ", "file.exist: " + file.exists() + "path: " + file.getAbsolutePath());
                        if (file.exists()) {
                            try {
                                FileInputStream fileInputStream = new FileInputStream(file);
                                int size = fileInputStream.available();
                                Log.e("onError: ", " size: " + size);
                            } catch (FileNotFoundException e) {
                                Log.e("onError: ", " FileNotFoundException: " + e.getMessage());
                                e.printStackTrace();
                            } catch (IOException e) {
                                Log.e("onError: ", " IOException: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        pptVideo.setVideoPath(file.getAbsolutePath());
                        pptVideo.start();
                        pptVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                Log.e("onError: ", "what: " + what + " extra: " + extra);
                                return false;
                            }
                        });
                    }
                    break;
                case MakePPTFragment.TYPE_WORK:
                    Glide.with(VideoActivity.this).load(null).into(iv_content);
                    switch (typework) {
                        //选择题
                        case SampleAdapter.TYPE_XUANZE_TI_CT:
                        case SampleAdapter.TYPE_XUANZE_TT_CI:
                        case SampleAdapter.TYPE_XUANZE_TT_CT:
                            iv_content.setBackgroundResource(R.mipmap.img_xuanze_img_text_4);
                            break;
                        //判断题
                        case SampleAdapter.TYPE_PANDUAN_TI:
                        case SampleAdapter.TYPE_PANDUAN_TT:
                            iv_content.setBackgroundResource(R.mipmap.img_panduan_ti);
                            break;

                        //简答
                        case SampleAdapter.TYPE_WENDA_TI:
                        case SampleAdapter.TYPE_WENDA_TT:
                            iv_content.setBackgroundResource(R.mipmap.img_jianda_img_ti);
                            break;
                        case SampleAdapter.TYPE_VOICE_TI:
                        case SampleAdapter.TYPE_VOICE_TT:
                            iv_content.setBackgroundResource(R.mipmap.img_audio_img_ti);

                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private String videoPath;

    //改变视频播放布局
    private void diffVideoVisable(boolean preView) {
        if (preView) {
            //预览
            ll_noViewo.setVisibility(View.GONE);
            iv_novideo_bg.setVisibility(View.GONE);
            btnCommit.setVisibility(View.GONE);
            btnClear.setVisibility(View.GONE);
            btnControll.setVisibility(View.GONE);
            mRecordTimeTxt.setVisibility(View.GONE);
            mSurfaceView.setVisibility(View.INVISIBLE);


            luzhiVideo.setVisibility(View.VISIBLE);//预览
            btnContinue.setVisibility(View.VISIBLE);
            ivPreStop.setVisibility(View.VISIBLE);

            stopRecording(); //暂停当前录制
        } else {
            //继续录制
            for (int i = 0; i < pptPreViews.size(); i++) {
                for (int j = 0; j < pptPreViews.get(i).mContentList.size(); i++) {
                    if (pptPreViews.get(i).mContentList.get(j).isLuzhiing) {
                        nowPPTGroupIndex = i;
                        nowPPTindex = j;
                        nowVideoContent = pptPreViews.get(nowPPTGroupIndex).mContentList.get(nowPPTindex); //当前页面
                        nowVideoContent.isClicked = true;
                        nowGroupNums = pptPreViews.get(nowPPTGroupIndex).mContentList.size();
                    } else {
                        pptPreViews.get(i).mContentList.get(j).isClicked = false;

                    }
                }
            }

            preAdapter.notifyDataSetChanged();

            luzhiVideo.setVisibility(View.INVISIBLE);
            btnContinue.setVisibility(View.GONE);
            ivPreStop.setVisibility(View.GONE);

            btnCommit.setVisibility(View.VISIBLE);
            btnClear.setVisibility(View.VISIBLE);
            btnControll.setVisibility(View.VISIBLE);
            mRecordTimeTxt.setVisibility(View.VISIBLE);
            mSurfaceView.setVisibility(View.VISIBLE);

            startRecording(); //继续拍摄
        }
    }


    //数据库加载ppt 数据
    private void getPPTDatas() {

        Intent intent = getIntent();
        pptPreViews = (List<PPTPreView>) intent.getSerializableExtra("preList");
        nowPPTGroupIndex = intent.getIntExtra("prepos", -1);
        nowPPTindex = intent.getIntExtra("contentpos", -1);

        for (PPTPreView pptPreView : pptPreViews) {
            List<PPTContent> pptContents = pptPreView.mContentList;
            if (pptContents == null)
                continue;
            Log.e("cListV: ", pptContents.size() + "");
            for (PPTContent pptContent : pptContents) {
                Log.e("pptContentV: ", pptContent.contentType);
            }
        }

//        Bundle listBundle = intent.getExtras();
//        Log.e("pptpreview: ", (listBundle==null) + "");
//        pptPreViews = listBundle.getParcelableArrayList("preList");
//        historyData = mPresenter.loadPPTDatasByTitle("一");
//
//        if (null != historyData) {
//            Log.e("historyData: ", historyData.getData());
//
//            JsonParser jsonParser = new JsonParser();
//            //PPTStoreData
//            JsonObject jsonObject = jsonParser.parse(historyData.getData()).getAsJsonObject();
//
//            JsonArray jsonArray = jsonObject.getAsJsonArray("mPreDatas");
//
//            pptPreViews = new ArrayList<>();
//
//            Gson gson = new Gson();
//            for (JsonElement jsonElement : jsonArray) {
//                PPTPreView pptPreView = gson.fromJson(jsonElement, PPTPreView.class);
//                if (pptPreView.itemType.equals(MakePPTFragment.NORMAL_PPT)) {
//                    pptPreView.isSelected = false;
//                    pptPreViews.add(pptPreView);
//                }
//            }
//            Log.e("historyDatahh: ", pptPreViews.size() + "");
//        }
    }

    private void initUpLoad() {

        mVODUploadClient = new VODUploadClientImpl(getApplicationContext());
        mVODUploadCallback = new VODUploadCallback() {
            @Override
            public void onUploadFailed(UploadFileInfo info, String code, String message) {
                super.onUploadFailed(info, code, message);
            }

            @Override
            public void onUploadStarted(UploadFileInfo uploadFileInfo) {
                super.onUploadStarted(uploadFileInfo);
                //设置：uploadAuth, uploadAddress
                String uploadAuth = "", uploadAddress = "";
                mVODUploadClient.setUploadAuthAndAddress(uploadFileInfo, uploadAuth, uploadAddress);
            }
        };

        mVODSVideoUploadClient = new VODSVideoUploadClientImpl();
        mVODSVideoUploadClient.init();
        mVODUploadClient.init(mVODUploadCallback);

    }

    //
    class VODSVideoUploadClientImpl implements VODSVideoUploadClient {
        @Override
        public void init() {
        }

        @Override
        public void uploadWithVideoAndImg(VodSessionCreateInfo vodSessionCreateInfo, VODSVideoUploadCallback vodsVideoUploadCallback) {

        }

        @Override
        public void refreshSTSToken(String s, String s1, String s2, String s3) {

        }

        @Override
        public void cancel() {

        }

        @Override
        public void resume() {

        }

        @Override
        public void pause() {

        }

        @Override
        public void release() {

        }

        @Override
        public void setAppVersion(String s) {

        }

        @Override
        public void setRegion(String s) {

        }

        @Override
        public void setRecordUploadProgressEnabled(boolean b) {

        }
    }

    private void initSDK() {
        mRecorder = AliyunRecorderCreator.getRecorderInstance(this);
        mRecorder.setDisplayView(mSurfaceView);
        mRecorder.setOnFrameCallback(new OnFrameCallBack() {
            @Override
            public void onFrameBack(byte[] bytes, int i, int i1, Camera.CameraInfo cameraInfo) {
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
                //录制完成
                handleRecordCallback(validClip, clipDuration);
                if (isOnMaxDuration) {
                    //满足最大录制时间
                    isOnMaxDuration = false;
                    toEditor();
                }
            }

            @Override
            public void onFinish(String outputPath) {
                //提交
                Log.d(TAG, "onFinish");
                hasRecoded = false;
                isRecordFinish = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //适配android Q
                    ThreadUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            UriUtils.saveVideoToMediaStore(getApplicationContext(), outputPath);
                        }
                    });
                } else {
                    scanFile(outputPath);
                }
//                nowVideoContent.videoUrl = outputPath; // 保存当前ppt 拍摄视频路径
                //结束后自动切换
                if (allGroupNum > nowPPTGroupIndex) {
                    mRecordTimeTxt.setText("00:00");
                    pptPreViews.get(nowPPTGroupIndex).mContentList.get(nowPPTindex).videoUrl = outputPath; //保存文件路径到列表中
                    ToastUtil.showToast(VideoActivity.this, "路径： " + outputPath);

                    //自定切换录制下一个
                    videoNext(nowPPTGroupIndex, nowPPTindex);
                    setOutRvBg(nowPPTGroupIndex, nowPPTindex);
                    //
                    mClipManager.deleteAllPart();
                    preAdapter.notifyDataSetChanged();
                } else {

                    for (PPTPreView pptPreView : pptPreViews) {
                        for (PPTContent pptContent : pptPreView.mContentList) {
                            if (TextUtils.isEmpty( pptContent.videoUrl)) {
                                return;
                            }
                        }
                    }

                    ToastUtil.showToast(VideoActivity.this, "视频录制完成");
                }

            }

            @Override
            public void onProgress(long duration) {
                //录制回调
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int time = (int) (mClipManager.getDuration() + duration) / 1000;
                        int min = time / 60; //分钟
                        int sec = time % 60;  //
                        if (mRecordTimeTxt.getVisibility() != View.VISIBLE) {
                            mRecordTimeTxt.setVisibility(View.VISIBLE);
                        }
                        mRecordTimeTxt.setText(String.format("%1$02d:%2$02d", min, sec));

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
        mRecorder.setExposureCompensationRatio(mExposureCompensationRatio);
        mRecorder.setFocusMode(CameraParam.FOCUS_MODE_CONTINUE);

    }

    //自动拍摄下一个 ppt  //p1 组号  p2 页号
    private void videoNext(int nowPptGroupIndex, int nowPptindex) {
        if (allGroupNum >= nowPptGroupIndex + 1) {
            //组号满足条件
            //自动拍摄下一页ppt 当前
            if (nowGroupNums > nowPptindex + 1) {
                //当前组还未拍完
                Log.e("nowPptGroupIndex: ", nowPptGroupIndex + "  " + nowPptindex + "  " + pptPreViews.size() + "   " + pptPreViews.get(nowPptGroupIndex).mContentList.size());
                if (pptPreViews.get(nowPptGroupIndex).mContentList.size() < 1) {
                    ToastUtil.showToast(VideoActivity.this, "录制完成");
                    return;
                }
                if (TextUtils.isEmpty(pptPreViews.get(nowPptGroupIndex).mContentList.get(nowPptindex + 1).videoUrl)) {
                    //该ppt 还未拍 视频  pptindex + 1
                    nowVideoContent = pptPreViews.get(nowPptGroupIndex).mContentList.get(++nowPPTindex); //获取同组下页ppt
                }
            } else {
                //当前组已拍完 更新数据
                nowPPTindex = -1; //新组从头开始
                nowGroupNums = pptPreViews.get(nowPPTGroupIndex).mContentList.size(); //
                ++nowPPTGroupIndex; //组号加1
                //递归调用 传入新组 和下标
                videoNext(nowPPTGroupIndex, nowPPTindex);
            }
        } else {
            ToastUtil.showToast(VideoActivity.this, "拍摄完毕");
        }
    }


    private AliyunIRecorder mRecorder;

    //Camera Confit data
    private final int RESOLUTIONMODE = AliyunSnapVideoParam.RESOLUTION_720P;
    private final int MIN_DURATION = 1000;
    private final int MAX_DURATION = 1000 * 60 * 60; //一小时
    private final int RATIOMODE = AliyunSnapVideoParam.RATIO_MODE_1_1;
    private final int GOP = 250;
    private final VideoQuality VIDEOQUALITY = VideoQuality.SSD;
    private final VideoCodecs VIDEOCODEC = VideoCodecs.H264_HARDWARE; //解码方式
    private boolean isOpenFailed;
    private CameraType CAMERATYPE = CameraType.FRONT;
    public static final String OUTPUT_PATH = "output_path";

    /**
     * 曝光度
     */
    private float mExposureCompensationRatio = 0.5f;


    private void scanFile(String path) {
        MediaScannerConnection.scanFile(this.getApplicationContext(),
                new String[]{path}, new String[]{"video/mp4"}, null);
    }

    //提交文件
    private void toEditor() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //停止拍摄
                mRecorder.finishRecording();
                //上传
                //获取文件

            }
        });
    }

    private boolean isOnMaxDuration;

    private void handleRecordCallback(final boolean validClip, final long clipDuration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("validClip", "validClip : " + validClip);
                isRecording = false;
            }
        });
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

    @Override
    public void onStart() {
        super.onStart();
        Log.d("MyGlSurfaceView", "onStart");
        Log.e(TAG, (btnControll == null) + " r");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MyGlSurfaceView", "onResume");
        /**
         * 部分android4.4机型会出现跳转Activity gl为空的问题，如果不需要适配，显示视图代码可以去掉
         */
        mRecorder.startPreview();
        mSurfaceView.setVisibility(View.VISIBLE);

    }


    @Override
    public void onPause() {
        super.onPause();
        if (isRecording && !isRecordFinish) {
            stopRecording();
            isRecording = false;
        }
        mRecorder.stopPreview();
        // 部分android4.4机型会出现跳转Activity gl为空的问题，如果不需要适配，隐藏视图代码可以去掉
        mSurfaceView.setVisibility(View.INVISIBLE);
    }


    private int mRecordMode = AliyunSnapVideoParam.RECORD_MODE_AUTO;

    public void setRecordMode(int recordMode) {
        this.mRecordMode = recordMode;
    }

    private boolean checkIfStartRecording() {
        if (btnControll.isActivated()) {
            return false;
        }
        if (CommonUtil.SDFreeSize() < 50 * 1000 * 1000) {
            Toast.makeText(this, "剩余磁盘空间不足", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    boolean isStoped;

    private void stopRecording() {
        mRecorder.stopRecording();
        //
        rv_pre.setVisibility(View.VISIBLE);
        btnControll.setBackgroundResource(R.mipmap.luzhi_act_pause);
        isStoped = true;
    }


    public static final int PERMISSION_REQUEST_CODE = 1001;


    private boolean hasRecoded = false; //标志是否在拍摄状态
    //开始拍摄
    private void startRecording() {

        if (!PermissionUtils.checkPermissionsGroup(this, permission)) {
            PermissionUtils.requestPermissions(this, permission, PERMISSION_REQUEST_CODE);
            return;
        }

        hasRecoded = true;//在拍摄态

        //
        rv_pre.setVisibility(View.GONE);

        isStoped = false;

        isRecording = true;
        //保存路径
        String videoPath = Constants.SDCardConstants.getDir(this) + System.currentTimeMillis() + ".mp4";
        mRecorder.setOutputPath(videoPath); //输出路径
        //执行了start但是不执行，mRecorder.startRecording();
        handleRecordStart();

        mRecorder.setRotation(getPictureRotation());
        mRecorder.startRecording();
        Log.d(TAG, "startRecording");

        //更新外层ui 背景
        setOutRvBg(nowPPTGroupIndex, nowPPTindex);

    }

    private OrientationDetector mOrientationDetector;

    private void initOrientationDetector() {
        mOrientationDetector = new OrientationDetector(this.getApplicationContext());
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

    //
    private void handleRecordStart() {
        //提交 撤销不可点击
        btnControll.setBackgroundResource(R.mipmap.luzhi_act_start);
    }


    private boolean isRecording = false;
    private AliyunIClipManager mClipManager;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initToolbar() {
    }

    @Override
    protected void initEventAndData() {
    }

    @BindView(R.id.rv_pre)
    RecyclerView rv_pre;

    VideoPreAdapter preAdapter;


    @OnClick({R.id.btn_controll, R.id.btn_delete, R.id.btn_commit, R.id.iv_pre_stop, R.id.btn_continue,
            R.id.tv_startluzhi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_startluzhi:
                //录制指定页面
                isRecordFinish = false;
                //拍摄控制
                if (isOpenFailed) {
                    Toast.makeText(this, "请开启摄像头/存储权限", Toast.LENGTH_SHORT).show();
                }
                if (!isRecording) {
                    //没在拍摄中
                    if (FastClickUtil.isFastClick() || !checkIfStartRecording()) {
                        return;
                    }
                    ll_noViewo.setVisibility(View.GONE);
                    iv_novideo_bg.setVisibility(View.GONE);

                    btnCommit.setVisibility(View.VISIBLE);
                    btnControll.setVisibility(View.VISIBLE);
                    btnClear.setVisibility(View.VISIBLE);

                    startRecording();
                }

                break;
            case R.id.btn_continue:
                isRecordFinish = false;
                //预览->继续录制
                diffVideoVisable(false);
                break;
            case R.id.iv_pre_stop:
                //预览
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && null != luzhiVideo && !TextUtils.isEmpty(videoPath)) {
                    File file = new File(videoPath);
                    Log.e("onError: ", "file.exist: " + file.exists() + "path: " + file.getAbsolutePath());
                    if (file.exists()) {
                        try {
                            FileInputStream fileInputStream = new FileInputStream(file);
                            int size = fileInputStream.available();
                            Log.e("onError: ", " size: " + size);
                        } catch (FileNotFoundException e) {
                            Log.e("onError: ", " FileNotFoundException: " + e.getMessage());
                            e.printStackTrace();
                        } catch (IOException e) {
                            Log.e("onError: ", " IOException: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    luzhiVideo.setVideoPath(file.getAbsolutePath());
                    luzhiVideo.start();
                    luzhiVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            ivPreStop.setVisibility(View.GONE);

                        }
                    });
                    luzhiVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Log.e("onCompletion", "播放完成");
                            ivPreStop.setVisibility(View.VISIBLE);
                        }
                    });

                    luzhiVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            Log.e("onError: ", "what: " + what + " extra: " + extra);
                            return false;
                        }
                    });
                }
                break;
            case R.id.btn_controll:
                isRecordFinish = false;
                //拍摄控制
                if (isOpenFailed) {
                    Toast.makeText(this, "请开启摄像头/存储权限", Toast.LENGTH_SHORT).show();
                }
                if (!isRecording) {
                    //没在拍摄中
                    if (FastClickUtil.isFastClick()) {
                        return;
                    }
                    if (!checkIfStartRecording()) {
                        return;
                    }
                    startRecording();
                } else {
                    //拍摄中
                    stopRecording();
                }

                break;
            case R.id.btn_delete:
                isRecordFinish = false;
                if (isRecording) {
                    //正在拍
                    ToastUtil.showToast(VideoActivity.this, "录制中不可撤销");
//                    btnClear.setEnabled(false);
                } else {
                    mClipManager.deleteAllPart(); //清除
                    mRecordTimeTxt.setText("00:00");
                }
                break;
            case R.id.btn_commit:
                if (isRecording) {
                    ToastUtil.showToast(VideoActivity.this, "录制中不可提交");
                } else {
                    //完成
                    if (mClipManager.getDuration() >= mClipManager.getMinDuration()) {
                        toEditor();
                    } else {
                        ToastUtil.showToast(VideoActivity.this, "请拍摄后再提交");
                    }
                }

                break;
            default:
                break;
        }
    }

    boolean isMute = false;
}
