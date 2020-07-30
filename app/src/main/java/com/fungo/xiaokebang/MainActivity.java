package com.fungo.xiaokebang;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.arthenica.mobileffmpeg.FFmpeg;
import com.fungo.xiaokebang.app.Constants;
import com.fungo.xiaokebang.base.activity.BaseActivity;
import com.fungo.xiaokebang.base.fragment.BaseFragment;
import com.fungo.xiaokebang.contract.main.MainContract;
import com.fungo.xiaokebang.core.bean.PPTContent;
import com.fungo.xiaokebang.core.bean.PPTPreView;
import com.fungo.xiaokebang.presenter.main.MainPresenter;
import com.fungo.xiaokebang.ui.main.CreationFragment;
import com.fungo.xiaokebang.ui.main.MineFragment;
import com.fungo.xiaokebang.ui.main.VideoActivity;
import com.fungo.xiaokebang.ui.ppt.MakePPTFragment;
import com.fungo.xiaokebang.utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.htcy.matisselib.Matisse;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View, CreationFragment.MainBottomVisable {
    public static final int FIRST = 0;
    public static final int SECOND = 1;

    @BindView(R.id.fragment_group)
    FrameLayout fragmentGroup;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.iv_his_delete)
    ImageView ivHisDelete;

    private ArrayList<BaseFragment> mFragments;


    CreationFragment mCreationFragment;
    MineFragment mineFragment;
    MakePPTFragment makePPTFragment;

    {
//         const char * args[] = {
//            "ffmpeg",
//                    "-y",
//                    "-i",
//                    video_file,
//                    "-i",
//                    image_src_file,
//                    "-filter_complex",
//                    output_quality == Q1080P ?
//                            "[0:v]scale=480:-1,pad=1920:1080:1440:0:black[padded];[1:v]scale=1440:-1[src];[padded][src]overlay=0:0" :
//                            output_quality == Q720P ?
//                                    "[0:v]scale=320:-1,pad=1280:720:960:0:black[padded];[1:v]scale=960:-1[src];[padded][src]overlay=0:0" : "",
//                    "-b:v",
//                    "1200K",
//                    "-b:a",
//                    "44K"
//                    output_file
//
//
//    }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                File downloadFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//                //
//                final String fromePath = downloadFile.toString() + "/more.mp4";
//                final String videoPath = downloadFile.toString() + "/test.mp4";
//                //
//                File imgFile = new File(fromePath);
//                File videoFile = new File(videoPath);
//                Log.e("fileexits: ", imgFile.canRead() + "  " + videoFile.canRead());
//
//                File outputFile = new File(downloadFile, "output.mp4");
//
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append("-y ").append("-i ").append(videoPath + " ").append("-i ")
//                        .append(fromePath + " ").append("-filter_complex ")
//                        .append("[0:v]scale=480:-1,pad=1920:1080:1440:0:black[padded];[1:v]scale=1440:-1[src];[padded][src]overlay=0:0 ")
//                        .append("-b:v ")
//                        .append("2400K ")
//                        .append("-b:a ")
//                        .append("44K ")
//                        .append(outputFile.getPath() + " ");
//
//                String executeStr = stringBuilder.toString();
//
//                FFmpeg.execute(executeStr);
//            }
//        }).start();


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        mFragments = new ArrayList<>();
        bottomNavigationView.setItemIconTintList(null);
        if (null == savedInstanceState) {
            initPager(false, Constants.TYPE_MAIN_WORK);
        } else {
            initPager(true, Constants.TYPE_MAIN_WORK);
        }
    }

    private int mLastFgIndex;

    /**
     * 切换fragment
     *
     * @param position 要显示的fragment的下标
     */
    public void switchFragment(int position) {

        if (position >= mFragments.size()) {
            return;
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment targetFg = mFragments.get(position);
        Fragment lastFg = mFragments.get(mLastFgIndex);
        mLastFgIndex = position;
        if (position != Constants.TYPE_MAIN_WORK) {
            setBottomVisable(true);
        }


        fragmentTransaction.hide(lastFg);

        if (!targetFg.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(targetFg).commitAllowingStateLoss();
            fragmentTransaction.add(R.id.fragment_group, targetFg);
        }

        fragmentTransaction.show(targetFg);
        fragmentTransaction.commitAllowingStateLoss();

    }


    void initPager(boolean isRecreate, int pos) {
        mCreationFragment = CreationFragment.newInstance();
        mFragments.add(mCreationFragment);
        initFragments();
        initBottomNavigationView();
        switchFragment(pos);
    }

    private void initBottomNavigationView() {
//        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.tab_work:
                    loadPager(Constants.TYPE_MAIN_WORK, mCreationFragment);
                    break;
                case R.id.tab_mine:
                    loadPager(Constants.TYPE_MAIN_MINE, mineFragment);
                    break;
                default:
                    break;
            }
            return true;
        });
    }


    private void loadPager(int pos, BaseFragment mFragment) {
        switchFragment(pos);
        mFragment.reload();
    }

    public void toVideoActivity(int prepos, int cpos, List<PPTPreView> pptPreViews) {
        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
        intent.putExtra("prepos", prepos);
        intent.putExtra("contentpos", cpos);
        intent.putExtra("preList", (Serializable) pptPreViews);
        startActivity(intent);
    }

    private void initFragments() {
        mineFragment = MineFragment.newInstance();
        makePPTFragment = MakePPTFragment.newInstance();
        mFragments.add(mineFragment);
        mFragments.add(makePPTFragment);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView.getVisibility() != View.VISIBLE) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bottomNavigationView.getVisibility() != View.GONE) {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initEventAndData() {

    }


    @Override
    public void showLoginSuccess() {

    }

    public void setBottomVisable(boolean visable) {
        if (visable) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    @Override
    public void visableBottom(boolean isVisable) {
        Log.e("FC: ", "visable " + isVisable + " " + getTopFragment().getClass().getSimpleName());
        if (isVisable) {
            ivHisDelete.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            if (mLastFgIndex != Constants.TYPE_MAIN_WORK) {
                bottomNavigationView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void viaableDelete(boolean isVisable) {
        if (isVisable) {
            bottomNavigationView.setVisibility(View.GONE);
            ivHisDelete.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);
            ivHisDelete.setVisibility(View.GONE);
        }
    }


    private int nowIvRequestCode = -1;

    //获取相册数据回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MakePPTFragment.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

            for (String str : Matisse.obtainPathResult(data)) {
                System.out.println("OnActivityResult " + str);
            }

            if (null != data && mDataCallBack != null) {
                mDataCallBack.databack(data);
            }
        } else if (requestCode == MakePPTFragment.REQUESTCODE_IMAGE_TITLE || requestCode == MakePPTFragment.REQUESTCODE_IMAGE_JIANDA) {
            nowIvRequestCode = requestCode;
            Log.e("nowIvRequestCode: ", nowIvRequestCode + " ");
            //从相册拿到图片
            // String videoPath = Constants.SDCardConstants.getDir(this) + System.currentTimeMillis() + ".mp4";
            if (null != data) {
                Uri sourceUri = data.getData();
                //裁切后图片路径
                Uri destUri = Uri.fromFile(new File(Constants.SDCardConstants.getDir(this) + System.currentTimeMillis() + ".jpg"));
                //开始裁剪
                UCrop.of(sourceUri, destUri)
                        .start(MainActivity.this);
            }

        } else if (requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            //获取到裁切的图片， 传递给对应iv
            if (mDataCallBack != null) {
                if (nowIvRequestCode == MakePPTFragment.REQUESTCODE_IMAGE_TITLE) {
                    mDataCallBack.caiqieUriBack(resultUri);
                } else if (nowIvRequestCode == MakePPTFragment.REQUESTCODE_IMAGE_JIANDA) {
                    mDataCallBack.jiandaUriBack(resultUri);
                }
            }

        } else if (requestCode == MakePPTFragment.REQUESTCODE_IMAGE_SELECT) {
            //获取到 选项的图片数据
            if (null != data) {
                Uri selectUri = data.getData();
                if (mDataCallBack != null) {
                    mDataCallBack.selectUriBack(selectUri);
                }
            }
        } else if (requestCode == UCrop.RESULT_ERROR) {
            Toast.makeText(this, "裁切图片失败", Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick(R.id.iv_his_delete)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_his_delete:
                //点击删除
                if (mDeleteCallBack != null) {
                    mDeleteCallBack.deleteHisItems();
                }
                break;
        }

    }


    private DeleteCallBack mDeleteCallBack;

    public void setDeleteCallBack(DeleteCallBack deleteCallBack) {
        mDeleteCallBack = deleteCallBack;
    }

    public interface DeleteCallBack {
        void deleteHisItems();//删除历史数据
    }


    private DataCallBack mDataCallBack;

    public void setDataCallBack(DataCallBack dataCallBack) {
        mDataCallBack = dataCallBack;
    }

    public interface DataCallBack {
        void databack(Intent intent);

        void caiqieUriBack(Uri uri);  //返回题目图片uri

        void selectUriBack(Uri uri); //返回选择题选项图片uri

        void jiandaUriBack(Uri uri); //简答题目

    }


}
