package com.fungo.xiaokebang.ui.ppt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Visibility;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.aliyun.common.utils.ToastUtil;
import com.bumptech.glide.Glide;
import com.fungo.xiaokebang.MainActivity;
import com.fungo.xiaokebang.R;
import com.fungo.xiaokebang.app.Constants;
import com.fungo.xiaokebang.base.fragment.BaseFragment;
import com.fungo.xiaokebang.contract.ppt.MakePPTContract;
import com.fungo.xiaokebang.core.bean.PPTContent;
import com.fungo.xiaokebang.core.bean.PPTPreView;
import com.fungo.xiaokebang.core.bean.PPTStoreData;
import com.fungo.xiaokebang.core.dao.PPTHistoryData;
import com.fungo.xiaokebang.presenter.ppt.MakePPTPresenter;
import com.fungo.xiaokebang.ui.main.CreationFragment;
import com.fungo.xiaokebang.ui.main.VideoActivity;
import com.fungo.xiaokebang.utils.CommonUtils;
import com.fungo.xiaokebang.utils.GifSizeFilter;
import com.fungo.xiaokebang.utils.SoftKeyBoardListener;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import cn.htcy.matisselib.Matisse;
import cn.htcy.matisselib.MimeType;
import cn.htcy.matisselib.engine.impl.PicassoEngine;
import cn.htcy.matisselib.filter.Filter;

/**
 * Class:
 * Other: 制作ppt 页面
 * Create by yuy on  2020/6/28.
 */
public class MakePPTFragment extends BaseFragment<MakePPTPresenter> implements MakePPTContract.View {

    @BindView(R.id.iv_back)
    ImageView back;
    @BindView(R.id.tv_record)
    TextView tvRecord;
    @BindView(R.id.iv_save)
    ImageView ivSave;
    @BindView(R.id.see)
    ImageView see;
    @BindView(R.id.ll_selector)
    LinearLayout llSelector;
    @BindView(R.id.rl_controller)
    RelativeLayout rlController;
    @BindView(R.id.rv_ppt_content)
    RecyclerView rvPptContent;
    @BindView(R.id.rv_preview)
    RecyclerView rvPreview;
    @BindView(R.id.ll_pic)
    LinearLayout llPic;
    @BindView(R.id.ll_video)
    LinearLayout llVideo;
    @BindView(R.id.ll_xuanze)
    LinearLayout llSelect;
    @BindView(R.id.ll_judege)
    LinearLayout llJudege;
    @BindView(R.id.ll_que)
    LinearLayout llQue;
    @BindView(R.id.ll_voice)
    LinearLayout llVoice;
    @BindView(R.id.ll_xuanze_answer)
    LinearLayout ll_xuanze_answer;
    @BindView(R.id.ll_pd_answer)
    LinearLayout ll_pd_answer;
    @BindView(R.id.tv_answer_a)
    TextView tv_answer_a;
    @BindView(R.id.tv_answer_b)
    TextView tv_answer_b;
    @BindView(R.id.tv_answer_c)
    TextView tv_answer_c;
    @BindView(R.id.tv_answer_d)
    TextView tv_answer_d;
    @BindView(R.id.ll_luzhi)
    LinearLayout ll_luzhi;

    static TextView tvpdLeft;

    static TextView tvpdRight;

    static ImageView empty_content;

    public static final String TYPE_IMAGE = "IMAGE";
    public static final String TYPE_VIDEO = "VIDEO";
    public static final String TYPE_WORK = "WORK";
    public static final String TYPE_ERROR = "ERROR";
    private PopupWindow mPopupWindow, mContentPopWindow;
    PreviewAdapter previewAdapter; //预览adapter
    PPTAdapter mPPTAdapter; //内容Adapter
    List<PPTPreView> pptPreDatas; //ppt list
    List<PPTContent> pptContentDatas; // ppt content list
    PPTContent nowPptContent; //内容列表展示的pptcontent

    @Override
    protected void initView() {
        super.initView();
        empty_content = view.findViewById(R.id.iv_empty_content);
        tvpdLeft = view.findViewById(R.id.et_pd_left);

        tvpdRight = view.findViewById(R.id.et_pd_right);
        Log.e("et: ", (tvpdLeft == null) + "");
        Log.e("et: ", (tvpdRight == null) + "");

    }

    public static MakePPTFragment newInstance() {
        Bundle args = new Bundle();
        MakePPTFragment fragment = new MakePPTFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SoftKeyBoardListener.setListener(_mActivity, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                if (null != llSelect) {
                    llSelector.setVisibility(View.INVISIBLE);
                }
                if (null != rvPreview) {
                    rvPreview.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void keyBoardHide(int height) {
                if (null != llSelector)
                    llSelector.setVisibility(View.VISIBLE);
                if (rvPreview != null) {
                    rvPreview.setVisibility(View.VISIBLE);
                }
            }
        });

        xuanzeSelectListener = new XuanzeSelectListener();

    }

    public static final String NORMAL_PPT = "normal";
    public static final String ADD_PPT = "add";
    private int preNowPos = -1;
    private int lastScrollContentIndex = -1;
    private int contentNowPos = -1; //当前item pos

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //预览RV
        pptPreDatas = new ArrayList<>();
        previewAdapter = new PreviewAdapter(_mActivity, pptPreDatas, new PreviewAdapter.OnItemClickListener() {
            @Override
            public void addItem() {
                saveNowWorkPpt(); //添加
                //添加空白
                ll_xuanze_answer.setVisibility(View.GONE);
                ll_pd_answer.setVisibility(View.GONE);
                int size = pptPreDatas.size(); //
                pptPreDatas.add(size - 1, new PPTPreView(NORMAL_PPT)); //添加空白页面
                previewAdapter.notifyItemInserted(size - 1);
                rvPreview.scrollToPosition(pptPreDatas.size() - 1);
            }

            @Override
            public void longClickItem(int pos) {
                //长按item
                if (pos != preNowPos) {
                    return;
                }
                Log.e("width: ", mPopupWindow.getContentView().getMeasuredWidth() + "");
                showPopWindow(pos);
            }

            @Override
            public void changePosition(int pos) {
                saveNowWorkPpt(); //
                //点击pre item
                preNowPos = pos; //记录点击位置
                //添加内容
                for (int i = 0; i < pptPreDatas.size(); i++) {
                    pptPreDatas.get(i).isSelected = false;
                }

                //获取该 app 中数据集合
                List<PPTContent> pptContentList = pptPreDatas.get(preNowPos).mContentList;
                if (pptContentList == null || pptContentList.size() < 1) {
                    //
                    empty_content.setVisibility(View.VISIBLE);
                    rvPptContent.setVisibility(View.GONE);
                } else {
                    empty_content.setVisibility(View.GONE);
                    rvPptContent.setVisibility(View.VISIBLE);
                    if (pptContentDatas != null && pptContentDatas.size() > 0) {
                        pptContentDatas.clear();
                    }
                    pptContentDatas.addAll(pptContentList);
                }

                mPPTAdapter.notifyDataSetChanged();
                pptPreDatas.get(pos).isSelected = true;
                previewAdapter.notifyDataSetChanged();

                Log.e("preNow: ", preNowPos + "  mContentList: " + pptPreDatas.get(preNowPos).mContentList.size());
                ll_pd_answer.setVisibility(View.GONE);
                ll_xuanze_answer.setVisibility(View.GONE);
                if (preNowPos >= 0 && pptPreDatas.get(preNowPos).mContentList.size() > 0) {
                    nowPptContent = pptPreDatas.get(preNowPos).mContentList.get(0); //拿到对应ppt 页面
                    showLlAnswer(nowPptContent.xuanzeNums, nowPptContent.realAnswer);
                }
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        PreViewItenDec itenDec = new PreViewItenDec();
        rvPreview.addItemDecoration(itenDec);
        rvPreview.setLayoutManager(manager);
        rvPreview.setAdapter(previewAdapter);

        //内容RV
        pptContentDatas = new ArrayList<>();
        mPPTAdapter = new PPTAdapter(pptContentDatas, _mActivity, new PPTAdapter.LongClickListener() {
            @Override
            public void longClickItem(int pos) {
                //点击监听c
//                ToastUtil.showToast(_mActivity, " " + pos);
                Log.e("width: ", " " + _mActivity.getWindow().getDecorView().getWidth() + " " + mContentPopWindow.getContentView().getMeasuredWidth());
                mContentPopWindow.showAsDropDown(rvPptContent, (_mActivity.getWindow().getDecorView().getWidth() - CommonUtils.dp2px(200)) / 2, -(rvPptContent.getHeight() + 100), Gravity.NO_GRAVITY);
                contentDeletePos = pos; //
            }
        });

        //
        pptPreDatas.add(new PPTPreView(ADD_PPT)); //


        //关联PagerSnapHelper 一次反一页
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rvPptContent);
        rvPptContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    //判断RecyclerView 滑动的不同状态
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if (rvPptContent == null) {
                            return;
                        }
                        LinearLayoutManager layoutManager = (LinearLayoutManager) rvPptContent.getLayoutManager();
                        //当前RV item下标
                        int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                        contentNowPos = position; //记录当前cppt pos
//                        Log.e("scpos: ", position + " lastpos: " + lastScrollContentIndex);

                        if ((lastScrollContentIndex != position) && position >= 0) {
                            Log.e("target: ", position + "");
                            if (pptContentDatas.get(position).contentType.equals(TYPE_VIDEO)) {
                                mPPTAdapter.notifyItemChanged(position);
                            }
                            nowPptContent = pptContentDatas.get(position);
                            ll_xuanze_answer.setVisibility(View.GONE);
                            ll_pd_answer.setVisibility(View.GONE);
                            try {
                                if (Integer.parseInt(nowPptContent.getContentType()) > 0) {
                                    //题目 页
                                    ToastUtil.showToast(_mActivity, nowPptContent.realAnswer);
                                    //答案
                                    showLlAnswer(nowPptContent.xuanzeNums, nowPptContent.realAnswer);
                                } else {
                                    ll_xuanze_answer.setVisibility(View.GONE);
                                    ll_pd_answer.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        lastScrollContentIndex = position; //保存
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        saveNowWorkPpt();
                        break;
                    default:
                        break;
                }
            }
        });

        LinearLayoutManager contentLManager = new LinearLayoutManager(_mActivity);
        contentLManager.setOrientation(RecyclerView.HORIZONTAL);
        rvPptContent.setLayoutManager(contentLManager);
        rvPptContent.setAdapter(mPPTAdapter);

        //监听点击相片数据
        mDataCallBack = new MainActivity.DataCallBack() {
            @Override
            public void databack(Intent data) {

                List<PPTContent> addPptContents = new ArrayList<>();
                //相册返回数据
                for (String str : Matisse.obtainPathResult(data)) {
                    // ppt 包含的数据  分类 视频或者图片
                    String contentType = getContentType(str);
                    Log.e("SelectPic ", str + " type: " + contentType);
                    //p2 图片或视频地址
                    addPptContents.add(new PPTContent(contentType, str));
                }

                Log.e("PPT: ", "size: " + addPptContents.size());

                if (addPptContents.size() > 0) {
                    ll_pd_answer.setVisibility(View.GONE);
                    ll_xuanze_answer.setVisibility(View.GONE);
                    empty_content.setVisibility(View.GONE);
                    rvPptContent.setVisibility(View.VISIBLE);
                } else {
                    empty_content.setVisibility(View.VISIBLE);
                    rvPptContent.setVisibility(View.GONE);
                }

                //已有的
                List<PPTContent> nowPptContentList = pptPreDatas.get(preNowPos).mContentList;
                Log.e("PLIST: ", " now:" + nowPptContentList.size() + " add: " + addPptContents.size() + " presize: " + pptPreDatas.size());

                if (nowPptContentList != null) {
                    //添加
                    nowPptContentList.addAll(addPptContents);
                    pptContentDatas.clear();
                    pptContentDatas.addAll(nowPptContentList);
                }


                mPPTAdapter.notifyDataSetChanged(); //刷新内容
                rvPptContent.scrollToPosition(pptPreDatas.get(preNowPos).mContentList.size() - 1); //定位到最后一页
                //
                boolean isFreshPre = false;
                if (pptPreDatas.get(preNowPos).mContentList.size() > 0) {
                    isFreshPre = true;
                }

                if (isFreshPre) {
                    previewAdapter.notifyDataSetChanged(); //预览刷新
                }

            }

            //获取裁切的uri
            @Override
            public void caiqieUriBack(Uri uri) {
                RecyclerView.ViewHolder viewHolder = rvPptContent.findViewHolderForAdapterPosition(((LinearLayoutManager) rvPptContent.getLayoutManager()).findFirstCompletelyVisibleItemPosition());

                if (null != nowPptContent) {
                    nowPptContent.titleIvPath = uri.getPath(); //保存题目图像路径
                }

                if (viewHolder instanceof PPTAdapter.PPTViewHolder) {
                    //neir
                    ImageView iv_title = ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.iv_title);
                    iv_title.setBackground(null);
                    Glide.with(_mActivity).load(uri.getPath()).into(iv_title);
                }
            }

            @Override
            public void jiandaUriBack(Uri uri) {
                RecyclerView.ViewHolder viewHolder = rvPptContent.findViewHolderForAdapterPosition(((LinearLayoutManager) rvPptContent.getLayoutManager()).findFirstCompletelyVisibleItemPosition());

                if (null != nowPptContent) {
                    nowPptContent.titleIvPath = uri.getPath(); //保存题目图像路径
                }
                if (viewHolder instanceof PPTAdapter.PPTViewHolder) {
                    //neir
                    ImageView iv_jianda = ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.iv_jiada);
                    iv_jianda.setBackground(null);
                    Glide.with(_mActivity).load(uri.getPath()).into(iv_jianda);
                }

            }



            //获取选项图片uri
            @Override
            public void selectUriBack(Uri uri) {
                RecyclerView.ViewHolder viewHolder = rvPptContent.findViewHolderForAdapterPosition(((LinearLayoutManager) rvPptContent.getLayoutManager()).findFirstCompletelyVisibleItemPosition());

                ImageView iv_a, iv_b, iv_c, iv_d;

                if (null != nowPptContent && viewHolder instanceof PPTAdapter.PPTViewHolder) {
                    iv_a = ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.iv_ci_a);
                    iv_c = ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.iv_ci_c);
                    iv_d = ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.iv_ci_d);
                    iv_b = ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.iv_ci_b);
                    switch (nowSelectIv) {
                        case 1:  //选项A 返回
                            nowPptContent.selectUris[0] = uri.getPath();
                            Glide.with(_mActivity).load(uri).into(iv_a);
                            break;
                        case 2:
                            nowPptContent.selectUris[1] = uri.getPath();
                            Glide.with(_mActivity).load(uri).into(iv_b);
                            break;
                        case 3:
                            nowPptContent.selectUris[2] = uri.getPath();
                            Glide.with(_mActivity).load(uri).into(iv_c);
                            break;
                        case 4:
                            nowPptContent.selectUris[3] = uri.getPath();
                            Glide.with(_mActivity).load(uri).into(iv_d);
                            break;

                        default:
                            break;
                    }
                }
            }
        };

        //获取相片地址回调
        if (_mActivity instanceof MainActivity) {
            ((MainActivity) _mActivity).setDataCallBack(mDataCallBack);
        }

        iniPopWindows();
    }

    //只有往左滑动才调用
    private void showLlAnswer(int xuanzeNums, String realAnswer) {
        //保存文本数据 图像数据在图像返回时就自动保存了
        Log.e("scpos: ", " contentNowPos: " + contentNowPos);
        RecyclerView.ViewHolder viewHolder = rvPptContent.findViewHolderForAdapterPosition(contentNowPos);

        EditText et_a, et_b, et_c, et_d, et_title;

        if (null != nowPptContent && viewHolder instanceof PPTAdapter.PPTViewHolder) {

            et_a = ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_a);
            et_b = ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_b);
            et_c = ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_c);
            et_d = ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_d);
            et_title = ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_title);

            Log.e("showLl: ", nowPptContent.selectStrings[4]);
            if (TextUtils.isEmpty(nowPptContent.selectStrings[0])) {
                et_a.setHint("请输入选项内容");
            } else {
                et_a.setText(nowPptContent.selectStrings[0]);
            }

            if (TextUtils.isEmpty(nowPptContent.selectStrings[1])) {
                et_b.setHint("请输入选项内容");
            } else {
                et_b.setText(nowPptContent.selectStrings[1]);
            }

            if (TextUtils.isEmpty(nowPptContent.selectStrings[2])) {
                et_c.setHint("请输入选项内容");
            } else {
                et_c.setText(nowPptContent.selectStrings[2]);
            }

            if (TextUtils.isEmpty(nowPptContent.selectStrings[3])) {
                et_d.setHint("请输入选项内容");
            } else {
                et_d.setText(nowPptContent.selectStrings[3]);
            }


            //title
            if ((nowPptContent.selectStrings[4] == null)) {
                et_title.setHint("请输入问题");
            } else {
                et_title.setText(nowPptContent.selectStrings[4]);
            }

        }

        int workType = -1;
        try {
            workType = Integer.parseInt(nowPptContent.contentType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("preNow: ", workType + "");
        switch (workType) {
            //选择题
            case SampleAdapter.TYPE_XUANZE_TI_CT:
            case SampleAdapter.TYPE_XUANZE_TT_CI:
            case SampleAdapter.TYPE_XUANZE_TT_CT:

                showXuanzeAnswer(xuanzeNums, realAnswer);
                break;
            //判断题
            case SampleAdapter.TYPE_PANDUAN_TI:
            case SampleAdapter.TYPE_PANDUAN_TT:
                showPanDuanAnswer(realAnswer);
                break;
            default:
                ll_pd_answer.setVisibility(View.GONE);
                ll_xuanze_answer.setVisibility(View.GONE);
                break;
        }
    }

    private void showPanDuanAnswer(String realAnswer) {
        ll_pd_answer.setVisibility(View.VISIBLE);
        ll_xuanze_answer.setVisibility(View.GONE);
        //
        tvpdLeft.setText(TextUtils.isEmpty(nowPptContent.selectStrings[0]) ? "输入文字" : nowPptContent.selectStrings[0]);
        tvpdRight.setText(TextUtils.isEmpty(nowPptContent.selectStrings[1]) ? "输入文字" : nowPptContent.selectStrings[1]);

        String left = tvpdLeft.getText().toString().trim();
        String right = tvpdRight.getText().toString().trim();

        if (!TextUtils.isEmpty(realAnswer)) {
            if (realAnswer.equals(left)) {
                tvpdLeft.setBackgroundResource(R.mipmap.panduan_xuanxiang_input1);
                tvpdRight.setBackgroundResource(R.mipmap.panduan_wrong_right);
            } else if (realAnswer.equals(right)) {
                tvpdLeft.setBackgroundResource(R.mipmap.panduan_wrong_left);
                tvpdRight.setBackgroundResource(R.mipmap.panduan_xuanxiang_input2);
            } else {
                tvpdLeft.setBackgroundResource(R.mipmap.panduan_xuanxiang_input1);
                tvpdRight.setBackgroundResource(R.mipmap.panduan_xuanxiang_input2);
            }
        } else {
            tvpdLeft.setBackgroundResource(R.mipmap.panduan_xuanxiang_input1);
            tvpdRight.setBackgroundResource(R.mipmap.panduan_xuanxiang_input2);
        }

    }

    private void showXuanzeAnswer(int xuanzeNums, String realAnswer) {
        ll_xuanze_answer.setVisibility(View.VISIBLE);
        ll_pd_answer.setVisibility(View.GONE);
        //选项显隐藏
        switch (xuanzeNums) {
            case 2:
                tv_answer_c.setVisibility(View.GONE);
                tv_answer_d.setVisibility(View.GONE);
                break;
            case 3:
                tv_answer_c.setVisibility(View.VISIBLE);
                tv_answer_d.setVisibility(View.GONE);
                break;
            case 4:
                tv_answer_c.setVisibility(View.VISIBLE);
                tv_answer_d.setVisibility(View.VISIBLE);
            default:
                break;
        }

        if (!TextUtils.isEmpty(realAnswer)) {
            //
            switch (realAnswer) {
                case "a":
                    tv_answer_a.setBackgroundColor(Color.parseColor("#38C886"));
                    tv_answer_b.setBackgroundColor(Color.WHITE);
                    tv_answer_c.setBackgroundColor(Color.WHITE);
                    tv_answer_d.setBackgroundColor(Color.WHITE);
                    break;
                case "b":
                    tv_answer_b.setBackgroundColor(Color.parseColor("#38C886"));
                    tv_answer_a.setBackgroundColor(Color.WHITE);
                    tv_answer_c.setBackgroundColor(Color.WHITE);
                    tv_answer_d.setBackgroundColor(Color.WHITE);
                    break;
                case "c":
                    tv_answer_c.setBackgroundColor(Color.parseColor("#38C886"));
                    tv_answer_a.setBackgroundColor(Color.WHITE);
                    tv_answer_b.setBackgroundColor(Color.WHITE);
                    tv_answer_d.setBackgroundColor(Color.WHITE);
                    break;
                case "d":
                    tv_answer_d.setBackgroundColor(Color.parseColor("#38C886"));
                    tv_answer_a.setBackgroundColor(Color.WHITE);
                    tv_answer_b.setBackgroundColor(Color.WHITE);
                    tv_answer_c.setBackgroundColor(Color.WHITE);
                    break;
                default:

                    break;
            }
        } else {
            tv_answer_d.setBackgroundColor(Color.WHITE);
            tv_answer_a.setBackgroundColor(Color.WHITE);
            tv_answer_b.setBackgroundColor(Color.WHITE);
            tv_answer_c.setBackgroundColor(Color.WHITE);
        }
    }

    //根据文件后缀返回文件类型
    public static String getContentType(String str) {
        String type = str.substring(str.length() - 3).toLowerCase();
        Log.e("type: ", type);
        String returnType = "";
        switch (type) {
            case "jpg":
            case "bmp":
            case "peg":
            case "gif":
            case "png":
                returnType = TYPE_IMAGE;
                break;
            case "avi":
            case "mov":
            case "rmvb":
            case "flv":
            case "mp4":
            case "3gp":
                returnType = TYPE_VIDEO;
                break;
            default:
                returnType = TYPE_ERROR;
        }
        return returnType;
    }

    private int contentDeletePos = -1; //内容删除下标

    // 展示在pre rv上面
    private void showPopWindow(int pos) {
        Log.e("width: ", " " + _mActivity.getWindow().getDecorView().getWidth() + " " + mPopupWindow.getContentView().getMeasuredWidth());
        mPopupWindow.showAsDropDown(rvPreview, (_mActivity.getWindow().getDecorView().getWidth() - CommonUtils.dp2px(200)) / 2, -(rvPreview.getHeight() + 100), Gravity.NO_GRAVITY);
    }


    private void iniPopWindows() {
        //popwindow1
        View view = LayoutInflater.from(_mActivity).inflate(R.layout.popwindow_layout, null, false);
        View view_content = LayoutInflater.from(_mActivity).inflate(R.layout.popwindow_content_layout, null, false);

        TextView  tv_delete, tv_bg;

        tv_delete = view.findViewById(R.id.delete);
        tv_bg = view.findViewById(R.id.background);



        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(_mActivity, "删除");
            }
        });

        tv_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(_mActivity, "背景");
            }
        });

        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效

        //popwindow 2
        TextView tv_full, tv_adapt, tv_delete_content;
        tv_full = view_content.findViewById(R.id.full);
        tv_adapt = view_content.findViewById(R.id.adapt);
        tv_delete_content = view_content.findViewById(R.id.delete_content);

        tv_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(_mActivity, "填充");
                if (mContentPopWindow != null && mContentPopWindow.isShowing()) {
                    mContentPopWindow.dismiss();
                }
            }
        });

        tv_adapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(_mActivity, "适应");
                if (mContentPopWindow != null && mContentPopWindow.isShowing()) {
                    mContentPopWindow.dismiss();
                }
            }
        });

        tv_delete_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (PPTContent pptContent : pptContentDatas) {
                    Log.e("DeleteContent: ", pptContent.contentStr);
                }
                pptContentDatas.remove(contentDeletePos);
                mPPTAdapter.notifyItemRemoved(contentDeletePos);
                //监听pre rv
                Log.e("DeleteContent: ", pptPreDatas.get(preNowPos).mContentList.size() + "");
                pptPreDatas.get(preNowPos).mContentList.remove(contentDeletePos); //移除pre
                Log.e("DeleteContent: ", pptPreDatas.get(preNowPos).mContentList.size() + "");
                if (pptPreDatas.get(preNowPos).mContentList.size() < 1) {
                    previewAdapter.notifyDataSetChanged();
                }

                ToastUtil.showToast(_mActivity, "删除");
                if (mContentPopWindow != null && mContentPopWindow.isShowing()) {
                    mContentPopWindow.dismiss();
                }
            }
        });

        mContentPopWindow = new PopupWindow(view_content, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mContentPopWindow.setTouchable(true);
        mContentPopWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_makeppt;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        pptPreDatas.clear();
    }


    @Override
    protected void initEventAndData() {
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (_mActivity instanceof MainActivity) {
            ((MainActivity) _mActivity).setBottomVisable(false);
        }

        Log.e("onSupportVisible: ", "true");
        //回到初始状态
        if (pptPreDatas.size() == 0) {
            pptPreDatas.add(new PPTPreView(ADD_PPT));
        }
        mPPTAdapter.notifyDataSetChanged();
        previewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        ll_pd_answer.setVisibility(View.GONE);
        ll_xuanze_answer.setVisibility(View.GONE);
        Log.e("MF: ", "onSupportInvisible");
    }

    /**
     * ppt
     */
    String[] permission = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    /**
     * 拍摄
     */
    String[] permissionvideo = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @OnClick({R.id.iv_back, R.id.tv_record, R.id.ll_pic, R.id.ll_video, R.id.ll_xuanze
            , R.id.ll_judege, R.id.tv_answer_a, R.id.tv_answer_b, R.id.tv_answer_c, R.id.tv_answer_d
            , R.id.et_pd_right, R.id.et_pd_left, R.id.ll_que, R.id.ll_voice, R.id.iv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_save:
                if (savePPTDialog == null) {
                    savePPTDialog = new SavePPTDialog(new SavePPTDialog.PPTSaveBack() {
                        @Override
                        public void savePPTDatas() {
                            savePPTDialog.dismiss();
                            //保存
                            if (namedDraftDialog == null) {
                                namedDraftDialog = new NamedDraftDialog(new NamedDraftDialog.DraftStrCallBack() {
                                    @Override
                                    public void callbackDraft(String draftStrtitle) {
                                        //回调title
                                        //保存ppt 及title
                                        PPTStoreData pptStoreData = new PPTStoreData();
                                        pptStoreData.mPreDatas = pptPreDatas;
                                        String pptStoreDataStr = new Gson().toJson(pptStoreData);
                                        List<PPTHistoryData> allppt = mPresenter.addPreDatas(pptStoreDataStr, draftStrtitle);
                                        Log.e("pptStoreDataStr: ", "json" + pptStoreDataStr + " title " + draftStrtitle + "  " + allppt.size());
                                        if (allppt.size() > 0) {
                                            if (_mActivity instanceof MainActivity) {
                                                ((MainActivity) _mActivity).switchFragment(Constants.TYPE_MAIN_WORK);
                                                pptPreDatas.clear();
                                                pptContentDatas.clear();
                                            }
                                        } else {
                                            ToastUtil.showToast(_mActivity, "保存失败");
                                        }
                                        namedDraftDialog.dismiss();
                                    }
                                });
                            }
                            namedDraftDialog.show(_mActivity);
                        }
                    });
                }
                savePPTDialog.show(_mActivity);
                break;
            case R.id.et_pd_left:
                nowPptContent.realAnswer = nowPptContent.selectStrings[0];
                tvpdRight.setBackgroundResource(R.mipmap.panduan_wrong_right);
                tvpdLeft.setBackgroundResource(R.mipmap.panduan_xuanxiang_input1);
                break;
            case R.id.et_pd_right:
                nowPptContent.realAnswer = nowPptContent.selectStrings[1];
                tvpdLeft.setBackgroundResource(R.mipmap.panduan_wrong_left);
                tvpdRight.setBackgroundResource(R.mipmap.panduan_xuanxiang_input2);
                break;
            case R.id.tv_answer_a:
                nowPptContent.realAnswer = "a";
                tv_answer_a.setBackgroundColor(Color.parseColor("#38C886"));
                tv_answer_b.setBackgroundColor(Color.WHITE);
                tv_answer_c.setBackgroundColor(Color.WHITE);
                tv_answer_d.setBackgroundColor(Color.WHITE);
                break;
            case R.id.tv_answer_b:
                nowPptContent.realAnswer = "b";
                tv_answer_b.setBackgroundColor(Color.parseColor("#38C886"));
                tv_answer_a.setBackgroundColor(Color.WHITE);
                tv_answer_c.setBackgroundColor(Color.WHITE);
                tv_answer_d.setBackgroundColor(Color.WHITE);
                break;
            case R.id.tv_answer_c:
                nowPptContent.realAnswer = "c";
                tv_answer_c.setBackgroundColor(Color.parseColor("#38C886"));
                tv_answer_b.setBackgroundColor(Color.WHITE);
                tv_answer_a.setBackgroundColor(Color.WHITE);
                tv_answer_d.setBackgroundColor(Color.WHITE);
                break;
            case R.id.tv_answer_d:
                nowPptContent.realAnswer = "d";
                tv_answer_d.setBackgroundColor(Color.parseColor("#38C886"));
                tv_answer_b.setBackgroundColor(Color.WHITE);
                tv_answer_c.setBackgroundColor(Color.WHITE);
                tv_answer_a.setBackgroundColor(Color.WHITE);
                break;
            case R.id.iv_back:
                onBackPressedSupport();
                break;
            case R.id.tv_record:
                //填写title
                //保存ppt 到数据库
                String prelistjson = new Gson().toJson(pptPreDatas);
                Log.e("prelistjson：", " prelistjson: " + prelistjson);
                //有的话先删除
                PPTHistoryData pptHistoryData = mPresenter.getPreByTitle("title1");
                if (null != pptHistoryData && !TextUtils.isEmpty(pptHistoryData.getData())) {
//                    mPresenter.removePreByTitle("title1");
                }
                //添加本次
                //跳转录制视频
                RxPermissions rxPermissions = new RxPermissions(_mActivity);
                rxPermissions.request(permissionvideo)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                if (_mActivity instanceof MainActivity) {

                                    for (PPTPreView pptPreView : pptPreDatas) {
                                        List<PPTContent> pptContents = pptPreView.mContentList;
                                        if (pptContents == null)
                                            continue;
//                                        Log.e("cList: ", pptContents.size() + "");
                                        for (PPTContent pptContent : pptContents) {
                                            if (TextUtils.isEmpty(pptContent.contentType))
                                                continue;
                                            Log.e("pptContent: ", pptContent.contentType);
                                        }
                                    }
                                    ((MainActivity) _mActivity).toVideoActivity(preNowPos, contentNowPos,pptPreDatas);
                                }
                            } else {
                                Toast.makeText(_mActivity, "需要相应的权限", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }, Throwable::printStackTrace);
                break;
            case R.id.ll_pic:
                //跳转AlbumAdapter
            case R.id.ll_video:
                if (preNowPos >= 0) {
                        //已有页面
                        Log.e("mContentList.size: ", pptPreDatas.get(preNowPos).mContentList.size() + "");
                        if (pptPreDatas.get(preNowPos).mContentList.size() < 1) {
                            saveNowWorkPpt();
                            getPicDataFromAlbum();
                        } else {
                            ToastUtil.showToast(_mActivity, "页面不为空");
                        }
                } else {

                    ToastUtil.showToast(_mActivity, "未定位到ppt页面");
                }
                break;
            case R.id.ll_voice:
                //语言题
                if (preNowPos >= 0) {
                    //已有页面
                    Log.e("mContentList.size: ", pptPreDatas.get(contentNowPos).mContentList.size() + "");
                    if (pptPreDatas.get(preNowPos).mContentList.size() < 1) {

                        saveNowWorkPpt(); //
                        if (null == vioceDialog) {
                            vioceDialog = new PanDuanDialog();
                        }

                        vioceDialog.setValue(SampleAdapter.WORK_TYPE_VOICE, _mActivity, new PanDuanDialog.PanDuanSelectListener() {
                            @Override
                            public void select(int workType) {

                                //题目数据回调 一次一个  题目样式 和答案个数
                                PPTContent workContent = new PPTContent(String.valueOf(workType), ""); // 注入题目样式
                                nowPptContent = workContent; //更新当前页面
                                //添加当前页面
                                List<PPTContent> nowPptContentList = pptPreDatas.get(preNowPos).mContentList;
                                if (nowPptContentList != null) {
                                    //添加
                                    nowPptContentList.add(workContent);
                                    pptContentDatas.clear();
                                    pptContentDatas.addAll(nowPptContentList);
                                }

                                //添加完成
                                contentNowPos = nowPptContentList.size() - 1;
                                rvPptContent.scrollToPosition(pptPreDatas.get(preNowPos).mContentList.size() - 1); //定位到最后一页
                                mPPTAdapter.notifyDataSetChanged(); //刷新内容

                                //展示答案布局
                                showLlAnswer(nowPptContent.xuanzeNums, nowPptContent.realAnswer);

                                if (pptContentDatas.size() > 0) {
                                    rvPptContent.setVisibility(View.VISIBLE);
                                }

                                boolean isFreshPre = false;
                                if (pptPreDatas.get(preNowPos).mContentList.size() > 0) {
                                    isFreshPre = true;
                                }

                                if (isFreshPre) {
                                    previewAdapter.notifyDataSetChanged(); //预览刷新
                                }
                                //
                                vioceDialog.dismiss();

                            }
                        });

                        vioceDialog.show(_mActivity);
                    } else {
                        ToastUtil.showToast(_mActivity, "页面不为空");
                    }

                } else {
                    ToastUtil.showToast(_mActivity, "未定位到ppt页面");
                }

                break;
            case R.id.ll_que:
                //问答题
                if (preNowPos >= 0) {
                    if (pptPreDatas.get(preNowPos).mContentList.size() < 1) {

                        saveNowWorkPpt();
                        if (null == jiandaDialog) {
                            jiandaDialog = new PanDuanDialog();
                        }

                        jiandaDialog.setValue(SampleAdapter.WORK_TYPE_WENDA, _mActivity, new PanDuanDialog.PanDuanSelectListener() {
                            @Override
                            public void select(int workType) {

                                //题目数据回调 一次一个  题目样式 和答案个数
                                PPTContent workContent = new PPTContent(String.valueOf(workType), ""); // 注入题目样式

                                nowPptContent = workContent; //更新当前页面

                                //添加当前页面
                                List<PPTContent> nowPptContentList = pptPreDatas.get(preNowPos).mContentList;
                                if (nowPptContentList != null) {
                                    //添加
                                    nowPptContentList.add(workContent);
                                    pptContentDatas.clear();
                                    pptContentDatas.addAll(nowPptContentList);
                                }

                                //添加完成
                                contentNowPos = nowPptContentList.size() - 1;
                                rvPptContent.scrollToPosition(pptPreDatas.get(preNowPos).mContentList.size() - 1); //定位到最后一页
                                mPPTAdapter.notifyDataSetChanged(); //刷新内容

                                //展示答案布局
                                showLlAnswer(nowPptContent.xuanzeNums, nowPptContent.realAnswer);

                                if (pptContentDatas.size() > 0) {
                                    rvPptContent.setVisibility(View.VISIBLE);
                                }

                                boolean isFreshPre = false;
                                if (pptPreDatas.get(preNowPos).mContentList.size() > 0) {
                                    isFreshPre = true;
                                }

                                if (isFreshPre) {
                                    previewAdapter.notifyDataSetChanged(); //预览刷新
                                }
                                //
                                jiandaDialog.dismiss();
                            }
                        });

                        jiandaDialog.show(_mActivity);

                    } else {
                        ToastUtil.showToast(_mActivity, "页面不为空");
                    }
                } else {
                    ToastUtil.showToast(_mActivity, "未定位到ppt页面");
                }
                break;

            case R.id.ll_judege:
                //添加判断题
                if (preNowPos >= 0) {
                    if (pptPreDatas.get(preNowPos).mContentList.size() < 1) {
                        //保存题目数据
                        saveNowWorkPpt();
                        if (null == panDuanDialog) {
                            panDuanDialog = new PanDuanDialog();
                        }

                        panDuanDialog.setValue(SampleAdapter.WORK_TYPE_PANDUAN, _mActivity, new PanDuanDialog.PanDuanSelectListener() {
                            @Override
                            public void select(int workType) {

                                //题目数据回调 一次一个  题目样式 和答案个数
                                PPTContent workContent = new PPTContent(String.valueOf(workType), ""); // 注入题目样式

                                nowPptContent = workContent; //更新当前页面

                                //添加当前页面
                                List<PPTContent> nowPptContentList = pptPreDatas.get(preNowPos).mContentList;
                                if (nowPptContentList != null) {
                                    //添加
                                    nowPptContentList.add(workContent);
                                    pptContentDatas.clear();
                                    pptContentDatas.addAll(nowPptContentList);
                                }

                                //添加完成
                                contentNowPos = nowPptContentList.size() - 1;
                                rvPptContent.scrollToPosition(pptPreDatas.get(preNowPos).mContentList.size() - 1); //定位到最后一页
                                mPPTAdapter.notifyDataSetChanged(); //刷新内容

                                //展示答案布局
                                showLlAnswer(nowPptContent.xuanzeNums, nowPptContent.realAnswer);

                                if (pptContentDatas.size() > 0) {
                                    rvPptContent.setVisibility(View.VISIBLE);
                                }

                                boolean isFreshPre = false;
                                if (pptPreDatas.get(preNowPos).mContentList.size() > 0) {
                                    isFreshPre = true;
                                }

                                if (isFreshPre) {
                                    previewAdapter.notifyDataSetChanged(); //预览刷新
                                }
                                //
                                panDuanDialog.dismiss();

                            }
                        });

                        //
                        panDuanDialog.show(_mActivity);
                    } else {
                        ToastUtil.showToast(_mActivity, "页面不为空");
                    }
                } else {
                    ToastUtil.showToast(_mActivity, "未定位到ppt页面");
                }

                break;
            case R.id.ll_xuanze:
                if (preNowPos >= 0) {
                    if (pptPreDatas.get(preNowPos).mContentList.size() < 1) {
                        //保存题目数据
                        saveNowWorkPpt();
                        if (null == xuanzeDialog) {
                            xuanzeDialog = new XuanzeDialog();
                        }
                        xuanzeDialog.setValue(SampleAdapter.WORK_TYPE_XUANZE, _mActivity, new XuanzeDialog.SelectListener() {
                            @Override
                            public void select(int workType, int xuanZeNum) {
                                Log.e("xuanzeNum: ", xuanZeNum + "");
                                //题目数据回调 一次一个  题目样式 和答案个数
                                PPTContent workContent = new PPTContent(String.valueOf(workType), ""); // 注入题目样式
                                if (xuanZeNum > 1) {
                                    workContent.xuanzeNums = xuanZeNum;
                                }
                                nowPptContent = workContent;

                                List<PPTContent> nowPptContentList = pptPreDatas.get(preNowPos).mContentList;
                                if (nowPptContentList != null) {
                                    //添加
                                    nowPptContentList.add(workContent);
                                    pptContentDatas.clear();
                                    pptContentDatas.addAll(nowPptContentList);
                                }

                                //添加完成
                                contentNowPos = nowPptContentList.size() - 1;
                                rvPptContent.scrollToPosition(pptPreDatas.get(preNowPos).mContentList.size() - 1); //定位到最后一页
                                mPPTAdapter.notifyDataSetChanged(); //刷新内容
                                //
                                showLlAnswer(nowPptContent.xuanzeNums, nowPptContent.realAnswer);
                                if (pptContentDatas.size() > 0) {
                                    rvPptContent.setVisibility(View.VISIBLE);
                                }

                                boolean isFreshPre = false;
                                if (pptPreDatas.get(preNowPos).mContentList.size() > 0) {
                                    isFreshPre = true;
                                }

                                if (isFreshPre) {
                                    previewAdapter.notifyDataSetChanged(); //预览刷新
                                }
                                //保存一下
                                saveNowWorkPpt();
                                xuanzeDialog.dismiss();
                            }
                        });
                        xuanzeDialog.show(_mActivity);

                    } else {
                        ToastUtil.showToast(_mActivity, "页面不为空");
                    }
                } else {
                    ToastUtil.showToast(_mActivity, "未定位到ppt页面");
                }
                break;
        }
    }


    //滑动保存当前页面的数据 选项等
    private  void saveNowWorkPpt() {
        //保存文本数据 图像数据在图像返回时就自动保存了
        //获取对应viewholdwer
//        Log.e("scpos: ", "save");
        RecyclerView.ViewHolder viewHolder = rvPptContent.findViewHolderForAdapterPosition(((LinearLayoutManager) rvPptContent.getLayoutManager()).findFirstCompletelyVisibleItemPosition());

//        Log.e("scpos: ", ((LinearLayoutManager) rvPptContent.getLayoutManager()).findFirstCompletelyVisibleItemPosition()+ "");
        String text_a, text_b, text_c, text_d, text_title,
                //判断选项
                text_pd_left, text_pd_right,
                //简答问题
                text_jd_up, text_jd_down;

        if (null != nowPptContent && viewHolder instanceof PPTAdapter.PPTViewHolder) {
            int workStyle = -1;
            String contentType = nowPptContent.contentType.trim();

            try {
                workStyle = Integer.valueOf(contentType);
            } catch (Exception e) {
                e.printStackTrace();
            }

            switch (workStyle) {
                //选择题
                case SampleAdapter.TYPE_XUANZE_TI_CT:
                case SampleAdapter.TYPE_XUANZE_TT_CI:
                case SampleAdapter.TYPE_XUANZE_TT_CT:

                    text_a = ((EditText) ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_a)).getText().toString().trim();
                    text_b = ((EditText) ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_b)).getText().toString().trim();
                    text_c = ((EditText) ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_c)).getText().toString().trim();
                    text_d = ((EditText) ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_d)).getText().toString().trim();
                    text_title = ((EditText) ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_title)).getText().toString().trim();

                    Log.e("scpostitle", text_title + " nowpos: " + contentNowPos);

                    nowPptContent.selectStrings[0] = text_a;
                    nowPptContent.selectStrings[1] = text_b;
                    nowPptContent.selectStrings[2] = text_c;
                    nowPptContent.selectStrings[3] = text_d;
                    nowPptContent.selectStrings[4] = text_title;

                    break;
                //判断题
                case SampleAdapter.TYPE_PANDUAN_TI:
                case SampleAdapter.TYPE_PANDUAN_TT:
                    text_pd_left = ((EditText) ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_pd_left)).getText().toString().trim();
                    text_pd_right = ((EditText) ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_pd_right)).getText().toString().trim();
                    text_title = ((EditText) ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_title)).getText().toString().trim();
                    nowPptContent.selectStrings[0] = text_pd_left;
                    nowPptContent.selectStrings[1] = text_pd_right;
                    nowPptContent.selectStrings[4] = text_title;
                    break;

                //简答
                case SampleAdapter.TYPE_WENDA_TI:
                case SampleAdapter.TYPE_WENDA_TT:
                    text_jd_up = ((EditText) ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_jd_c1)).getText().toString().trim();
                    text_jd_down = ((EditText) ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_jd_c2)).getText().toString().trim();
                    text_title = ((EditText) ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_title)).getText().toString().trim();
                    nowPptContent.selectStrings[0] = text_jd_up;
                    nowPptContent.selectStrings[1] = text_jd_down;
                    nowPptContent.selectStrings[4] = text_title;
                    break;
                case SampleAdapter.TYPE_VOICE_TI:
                case SampleAdapter.TYPE_VOICE_TT:
                    text_title = ((EditText) ((PPTAdapter.PPTViewHolder) viewHolder).worklayout.findViewById(R.id.et_title)).getText().toString().trim();
                    nowPptContent.selectStrings[4] = text_title;
                    break;
                default:
                    break;
            }
        }
    }

    private XuanzeDialog xuanzeDialog;
    private PanDuanDialog panDuanDialog, jiandaDialog, vioceDialog;

    void getPicDataFromAlbum() {
        //跳转AlbumAdapter
        RxPermissions rxPermissions1 = new RxPermissions(_mActivity);
        rxPermissions1.request(permission)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        //获取相册相片
                        getPicAndVideo();
                    } else {
                        Toast.makeText(_mActivity, "需要相应的权限", Toast.LENGTH_LONG)
                                .show();
                    }
                }, Throwable::printStackTrace);

    }

    public static final int REQUEST_CODE_CHOOSE = 23;

    //获取相册
    private void getPicAndVideo() {
        Matisse.from(_mActivity)
                .choose(MimeType.ofAll(), false)
                .theme(R.style.Matisse_Dracula)
                .countable(false)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .maxSelectable(9)
                .originalEnable(true)
                .maxOriginalSize(10)
                .imageEngine(new PicassoEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }


    MainActivity.DataCallBack mDataCallBack;

    long exitTime;

    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtil.showToast(_mActivity, "再次点击返回");
            exitTime = System.currentTimeMillis();
        } else {
            if (_mActivity instanceof MainActivity) {
                nowPptContent = null;
                pptPreDatas.clear();
                pptContentDatas.clear();
                ((MainActivity)_mActivity).switchFragment(Constants.TYPE_MAIN_WORK);
            }
        }
        return true;
    }

    public static final int REQUESTCODE_IMAGE_TITLE = 1;
    public static final int REQUESTCODE_IMAGE_SELECT = 2;
    public static final int REQUESTCODE_IMAGE_JIANDA = 3;

    //内容
    static class PPTAdapter extends RecyclerView.Adapter<PPTAdapter.PPTViewHolder> {
        private List<PPTContent> datas;
        private Context mContext;
        private PPTAdapter.LongClickListener mOnItemClickListener;

        public PPTAdapter(List<PPTContent> datas, Context context, PPTAdapter.LongClickListener onItemClickListener) {
            this.datas = datas;
            mContext = context;
            mOnItemClickListener = onItemClickListener;
        }

        @NonNull
        @Override
        public PPTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_ppt, parent, false);
            PPTViewHolder pptViewHolder = new PPTViewHolder(itemView);
            return pptViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PPTViewHolder holder, int position) {
            //暂时只显示图片
            Log.e("PPT: ", datas.get(position).contentStr + " pos: " + position);
            String path = datas.get(position).contentStr;
            String type = datas.get(position).contentType;  //题目类型
            int workType = -1;
            try {
                workType = Integer.parseInt(type);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //图片
            if (TYPE_IMAGE.equals(type)) {
                if (holder.mVideoView != null) {
                    holder.mVideoView.setVisibility(View.GONE);
                    empty_content.setVisibility(View.GONE);
                }
                if (holder.worklayout != null) {
                    holder.worklayout.setVisibility(View.GONE);
                }

                holder.mImageView.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(path).into(holder.mImageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.longClickItem(position);
                        }
                    }
                });
            } else if (TYPE_VIDEO.equals(type)) {
                //视频
                if (holder.mImageView != null) {
                    empty_content.setVisibility(View.GONE);
                    holder.mImageView.setVisibility(View.GONE);
                }
                if (holder.worklayout != null) {
                    holder.worklayout.setVisibility(View.GONE);
                }

                VideoView videoView = null;
                if (holder.mVideoView != null) {
                    videoView = holder.mVideoView;
                    videoView.setVisibility(View.VISIBLE);
                }

                //根据文件路径播放
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && null != videoView) {
                    File file = new File(path);
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

                    videoView.setVideoPath(file.getAbsolutePath());
                    videoView.start();
                    videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            Log.e("onError: ", "what: " + what + " extra: " + extra);
                            return false;
                        }
                    });
                }
            } else if (workType > 0) {
                //题目
                Log.e("workType: ", type);
                //当内容为题目
                if (holder.mVideoView != null) {
                    holder.mVideoView.setVisibility(View.GONE);
                    empty_content.setVisibility(View.GONE);
                }
                if (holder.mImageView != null) {
                    holder.mImageView.setVisibility(View.GONE);
                }

                View worklayout = holder.worklayout;

                switch (workType) {

                    case SampleAdapter.TYPE_XUANZE_TT_CT: //类型

                        if (worklayout != null) {
                            worklayout.setVisibility(View.VISIBLE);
                        }
                        //
                        //展示文本选项布局
                        worklayout.findViewById(R.id.ll_ci).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_ct).setVisibility(View.VISIBLE);
                        worklayout.findViewById(R.id.ll_pd).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_jd).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_voice).setVisibility(View.GONE);

                        worklayout.findViewById(R.id.iv_title).setVisibility(View.GONE);
                        break;

                    case SampleAdapter.TYPE_XUANZE_TT_CI:
                        // 题文本 选择iv
                        if (worklayout != null) {
                            worklayout.setVisibility(View.VISIBLE);
                            worklayout.findViewById(R.id.iv_title).setVisibility(View.GONE);
                            worklayout.findViewById(R.id.ll_pd).setVisibility(View.GONE);
                        }

                        //展示文本选项布局
                        worklayout.findViewById(R.id.ll_ci).setVisibility(View.VISIBLE);
                        worklayout.findViewById(R.id.ll_ct).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_jd).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_pd).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_voice).setVisibility(View.GONE);

                        //
                        break;
                    case SampleAdapter.TYPE_XUANZE_TI_CT:
                        if (worklayout != null) {
                            worklayout.setVisibility(View.VISIBLE);
                        }
                        //展示文本选项布局
                        worklayout.findViewById(R.id.ll_ci).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_ct).setVisibility(View.VISIBLE);
                        worklayout.findViewById(R.id.ll_jd).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_pd).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_voice).setVisibility(View.GONE);

                        //
                        ImageView ivTitle = worklayout.findViewById(R.id.iv_title);
                        ivTitle.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(datas.get(position).titleIvPath)) {
                            ivTitle.setBackground(null);
                        }
                        Glide.with(mContext).load(datas.get(position).titleIvPath).into(ivTitle);

                        ivTitle.setOnClickListener(v -> {
                            Intent intent = new Intent(Intent.ACTION_PICK, null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            if (mContext instanceof MainActivity) {
                                ((Activity) mContext).startActivityForResult(intent, REQUESTCODE_IMAGE_TITLE);
                            }
                        });

                        break;
                    case SampleAdapter.TYPE_PANDUAN_TT:
                        if (worklayout != null) {
                            worklayout.setVisibility(View.VISIBLE);
                            worklayout.findViewById(R.id.iv_title).setVisibility(View.GONE);
                        }
                        //展示文本选项布局
                        worklayout.findViewById(R.id.ll_ci).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_ct).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_jd).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_voice).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_pd).setVisibility(View.VISIBLE);
                        break;

                    case SampleAdapter.TYPE_PANDUAN_TI:
                        if (worklayout != null) {
                            worklayout.setVisibility(View.VISIBLE);
                        }

                        ImageView iv_title;
                        iv_title = worklayout.findViewById(R.id.iv_title);
                        iv_title.setVisibility(View.VISIBLE);
                        iv_title.setBackground(null);
                        if (!TextUtils.isEmpty(datas.get(position).titleIvPath)) {
                            iv_title.setBackground(null);
                            Glide.with(mContext).load(datas.get(position).titleIvPath).into(iv_title);
                        } else {
                            iv_title.setBackgroundResource(R.mipmap.img_xuanze_2);

                        }
                        //展示文本选项布局
                        worklayout.findViewById(R.id.ll_ci).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_ct).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_jd).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_voice).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_pd).setVisibility(View.VISIBLE);
                        break;

                    case SampleAdapter.TYPE_WENDA_TI:

                        if (worklayout != null) {
                            worklayout.setVisibility(View.VISIBLE);
                            worklayout.findViewById(R.id.iv_title).setVisibility(View.GONE);
                        }
                        //展示文本选项布局
                        worklayout.findViewById(R.id.ll_ci).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_ct).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_pd).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_voice).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_jd).setVisibility(View.VISIBLE);

                        //
                        worklayout.findViewById(R.id.iv_jiada).setVisibility(View.VISIBLE);

                        break;
                    case SampleAdapter.TYPE_WENDA_TT:
                        if (worklayout != null) {
                            worklayout.setVisibility(View.VISIBLE);
                            worklayout.findViewById(R.id.iv_title).setVisibility(View.GONE);
                        }
                        worklayout.findViewById(R.id.iv_jiada).setVisibility(View.GONE);

                        //展示文本选项布局
                        worklayout.findViewById(R.id.ll_ci).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_ct).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_pd).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_voice).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_jd).setVisibility(View.VISIBLE);
                        break;
                    case SampleAdapter.TYPE_VOICE_TT:
                        //
                        if (worklayout != null) {
                            worklayout.setVisibility(View.VISIBLE);
                            worklayout.findViewById(R.id.iv_title).setVisibility(View.GONE);
                        }
                        //展示文本选项布局
                        worklayout.findViewById(R.id.ll_ci).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_ct).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_pd).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_jd).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_voice).setVisibility(View.VISIBLE);

                        break;
                    case SampleAdapter.TYPE_VOICE_TI:

                        if (worklayout != null) {
                            worklayout.setVisibility(View.VISIBLE);
                        }

                        //展示文本选项布局
                        worklayout.findViewById(R.id.ll_ci).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_ct).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_pd).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_jd).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_voice).setVisibility(View.VISIBLE);

                        worklayout.findViewById(R.id.iv_title).setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }

                int selectNums = datas.get(position).xuanzeNums;
                if (workType == SampleAdapter.TYPE_XUANZE_TI_CT || workType == SampleAdapter.TYPE_XUANZE_TT_CT) {

                    EditText et_a, et_b, et_c, et_d, et_title;
                    et_a = worklayout.findViewById(R.id.et_a);
                    et_b = worklayout.findViewById(R.id.et_b);
                    et_c = worklayout.findViewById(R.id.et_c);
                    et_d = worklayout.findViewById(R.id.et_d);
                    et_title = worklayout.findViewById(R.id.et_title);

                    if ((datas.get(position).selectStrings[0] == null)) {
                        et_a.setText("");
                        et_a.setHint("请输入选项内容");
                    } else {
                        et_a.setText(datas.get(position).selectStrings[0]);
                    }

                    if ((datas.get(position).selectStrings[1] == null)) {
                        et_b.setText("");
                        et_b.setHint("请输入选项内容");
                    } else {
                        et_b.setText(datas.get(position).selectStrings[1]);
                    }

                    if (datas.get(position).selectStrings[2] == null) {
                        et_c.setText("");
                        et_c.setHint("请输入选项内容");
                    } else {
                        et_c.setText(datas.get(position).selectStrings[2]);
                    }

                    if (datas.get(position).selectStrings[3] == null) {
                        et_d.setText("");
                        et_d.setHint("请输入选项内容");
                    } else {
                        et_d.setText(datas.get(position).selectStrings[3]);
                    }

                    if ((datas.get(position).selectStrings[4] == null)) {
                        et_title.setText("");
                        et_title.setHint("请输入问题");
                    } else {
                        et_title.setText(datas.get(position).selectStrings[4]);
                    }

                    //非选项图片
                    Log.e("xuanzeNum: vis", selectNums + "");
                    if (selectNums == 2) {
                        worklayout.findViewById(R.id.ll_select_c).setVisibility(View.GONE);
                        worklayout.findViewById(R.id.ll_select_d).setVisibility(View.GONE);
                    } else if (selectNums == 3) {
                        worklayout.findViewById(R.id.ll_select_c).setVisibility(View.VISIBLE);
                        worklayout.findViewById(R.id.ll_select_d).setVisibility(View.GONE);
                    } else if (selectNums == 4) {
                        worklayout.findViewById(R.id.ll_select_c).setVisibility(View.VISIBLE);
                        worklayout.findViewById(R.id.ll_select_d).setVisibility(View.VISIBLE);
                    } else {
                        ToastUtil.showToast(mContext, "error");
                    }
                } else if (workType == SampleAdapter.TYPE_XUANZE_TT_CI) {
                    //选项图片
                    ImageView iv_a, iv_b, iv_c, iv_d;
                    EditText et_title;
                    View v_interval;

                    v_interval = worklayout.findViewById(R.id.v_interval);
                    iv_a = worklayout.findViewById(R.id.iv_ci_a);
                    iv_b = worklayout.findViewById(R.id.iv_ci_b);
                    iv_c = worklayout.findViewById(R.id.iv_ci_c);
                    iv_d = worklayout.findViewById(R.id.iv_ci_d);
                    et_title = worklayout.findViewById(R.id.et_title);

                    iv_a.setOnClickListener(xuanzeSelectListener);
                    iv_b.setOnClickListener(xuanzeSelectListener);
                    iv_c.setOnClickListener(xuanzeSelectListener);
                    iv_d.setOnClickListener(xuanzeSelectListener);

                    String path1 = datas.get(position).selectUris[0];
                    String path2 = datas.get(position).selectUris[1];
                    String path3 = datas.get(position).selectUris[2];
                    String path4 = datas.get(position).selectUris[3];

                    Log.e("ivpath: ", path1 + "  " + path2 + "  " + path3 + "  " + path4);


                    if (!TextUtils.isEmpty(path1)) {
                        Bitmap bm = BitmapFactory.decodeFile(path1);
                        iv_a.setImageBitmap(bm);
                    }

                    if (!TextUtils.isEmpty(path2)) {
                        Bitmap bm = BitmapFactory.decodeFile(path2);
                        iv_b.setImageBitmap(bm);
                    }

                    if (selectNums == 2) {
                        iv_c.setVisibility(View.GONE);
                        iv_d.setVisibility(View.GONE);
                        v_interval.setVisibility(View.GONE);
                    } else if (selectNums == 3) {
                        if (!TextUtils.isEmpty(path3)) {
                            Bitmap bm = BitmapFactory.decodeFile(path3);
                            iv_c.setImageBitmap(bm);
                        }
                        iv_c.setVisibility(View.VISIBLE);
                        iv_d.setVisibility(View.GONE);
                        v_interval.setVisibility(View.VISIBLE);
                    } else if (selectNums == 4) {
                        if (!TextUtils.isEmpty(path4)) {
                            Bitmap bm = BitmapFactory.decodeFile(path4);
                            iv_d.setImageBitmap(bm);
                        }

                        iv_c.setVisibility(View.VISIBLE);
                        iv_d.setVisibility(View.VISIBLE);
                        v_interval.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(datas.get(position).selectStrings[4])) {
                        et_title.setText(datas.get(position).selectStrings[4]);
                    } else {
                        et_title.setText("");
                        et_title.setHint("请输入问题");
                    }

                } else if (workType == SampleAdapter.TYPE_PANDUAN_TT || workType == SampleAdapter.TYPE_PANDUAN_TI) {
                    EditText etinput1, etinput2, et_title;

                    etinput1 = worklayout.findViewById(R.id.et_pd_left);
                    etinput2 = worklayout.findViewById(R.id.et_pd_right);

                    etinput1.setText(datas.get(position).selectStrings[0]);
                    etinput2.setText(datas.get(position).selectStrings[1]);

                    et_title = worklayout.findViewById(R.id.et_title);

                    if (workType == SampleAdapter.TYPE_PANDUAN_TI) {
                        ImageView ivTitle = worklayout.findViewById(R.id.iv_title);
                        ivTitle.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(datas.get(position).titleIvPath).into(ivTitle);
                        ivTitle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //跳转相册 -> 裁切
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                if (mContext instanceof MainActivity) {
                                    ((Activity) mContext).startActivityForResult(intent, REQUESTCODE_IMAGE_TITLE);
                                }
                            }
                        });
                    }

                    //监听答案输入
                    etinput1.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            //
                            Log.e("edStr: ", s.toString());
                            showPdLlAnswerLeft(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    etinput2.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            Log.e("edStr: ", s.toString());
                            showPdLlAnswerRight(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    if ((datas.get(position).selectStrings[4] == null)) {
                        et_title.setText("");
                        et_title.setHint("请输入问题");
                    } else {
                        et_title.setText(datas.get(position).selectStrings[4]);
                    }

                } else if (workType == SampleAdapter.TYPE_WENDA_TT) {
                    EditText editTextup, editTextdown, etTitle;
                    //
                    editTextup = worklayout.findViewById(R.id.et_jd_c1);
                    //
                    editTextdown = worklayout.findViewById(R.id.et_jd_c2);
                    etTitle = worklayout.findViewById(R.id.et_title);

                    if ((datas.get(position).selectStrings[4] == null)) {
                        etTitle.setText("");
                        etTitle.setHint("请输入问题");
                    } else {
                        etTitle.setText(datas.get(position).selectStrings[4]);
                    }

                    editTextup.setText(datas.get(position).selectStrings[0]);
                    editTextdown.setText(datas.get(position).selectStrings[1]);
                } else if (workType == SampleAdapter.TYPE_WENDA_TI) {
                    EditText editTextup, editTextdown, etTitle;
                    //
                    editTextup = worklayout.findViewById(R.id.et_jd_c1);
                    //
                    editTextdown = worklayout.findViewById(R.id.et_jd_c2);

                    etTitle = worklayout.findViewById(R.id.et_title);

                    if ((datas.get(position).selectStrings[4] == null)) {
                        etTitle.setText("");
                        etTitle.setHint("请输入问题");
                    } else {
                        etTitle.setText(datas.get(position).selectStrings[4]);
                    }

                    editTextup.setText(datas.get(position).selectStrings[0]);
                    editTextdown.setText(datas.get(position).selectStrings[1]);
                    ImageView iv_jianda = worklayout.findViewById(R.id.iv_jiada);
                    iv_jianda.setVisibility(View.VISIBLE);
                    iv_jianda.setBackground(null);
                    if (!TextUtils.isEmpty(datas.get(position).titleIvPath)) {
                        Glide.with(mContext).load(datas.get(position).titleIvPath).into(iv_jianda);
                    } else {
                        iv_jianda.setBackgroundResource(R.mipmap.img_xuanze_2);
                    }

                    iv_jianda.setOnClickListener(v -> {
                        //简答iv
                        //跳转相册 -> 裁切
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        if (mContext instanceof MainActivity) {
                            ((Activity) mContext).startActivityForResult(intent, REQUESTCODE_IMAGE_JIANDA);
                        }
                    });

                } else if (workType == SampleAdapter.TYPE_VOICE_TI) {

                    EditText editTextTitle;
                    editTextTitle = worklayout.findViewById(R.id.et_title);
                    if ((datas.get(position).selectStrings[4] == null)) {
                        editTextTitle.setText("");
                        editTextTitle.setHint("请输入问题");
                    } else {
                        editTextTitle.setText(datas.get(position).selectStrings[4]);
                    }

                    ImageView iv_title;
                    iv_title = worklayout.findViewById(R.id.iv_title);
                    iv_title.setVisibility(View.VISIBLE);
                    iv_title.setBackground(null);
                    if (!TextUtils.isEmpty(datas.get(position).titleIvPath)) {
                        iv_title.setBackground(null);
                        Glide.with(mContext).load(datas.get(position).titleIvPath).into(iv_title);
                    } else {
                        iv_title.setBackgroundResource(R.mipmap.img_xuanze_2);
                    }



                    iv_title.setOnClickListener(v -> {
                        //跳转相册 -> 裁切
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        if (mContext instanceof MainActivity) {
                            ((Activity) mContext).startActivityForResult(intent, REQUESTCODE_IMAGE_TITLE);
                        }
                    });

                } else if (workType == SampleAdapter.TYPE_VOICE_TT) {

                    EditText editTextTitle;
                    worklayout.findViewById(R.id.iv_title).setVisibility(View.GONE);
                    editTextTitle = worklayout.findViewById(R.id.et_title);
                    if ((datas.get(position).selectStrings[4] == null)) {
                        editTextTitle.setText("");
                        editTextTitle.setHint("请输入问题");
                    } else {
                        editTextTitle.setText(datas.get(position).selectStrings[4]);
                    }
                } else {
                    worklayout.findViewById(R.id.iv_title).setVisibility(View.GONE);
                }
            }
        }


        @Override
        public int getItemCount() {
            if (datas.size() > 0) {
                empty_content.setVisibility(View.GONE);
            }
            return datas.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        public class PPTViewHolder extends RecyclerView.ViewHolder {

            private ImageView mImageView;
            private VideoView mVideoView;
            ConstraintLayout worklayout;  //题目

            public PPTViewHolder(@NonNull View itemView) {
                super(itemView);
                worklayout = itemView.findViewById(R.id.layout_xaunze);
                mImageView = itemView.findViewById(R.id.iv_ppt);
                mVideoView = itemView.findViewById(R.id.video_ppt);
            }
        }

        interface LongClickListener {
            void longClickItem(int pos);
        }

    }

    //同步答案
    private static void showPdLlAnswerLeft(String s) {
        Log.e("edStr: Left", s);
        tvpdLeft.setText(s);
    }

    private static void showPdLlAnswerRight(String s) {
        Log.e("edStr: Right", s);
        tvpdRight.setText(s);
    }

    private int nowSelectIv = 0; //当前选择的选项

    private static XuanzeSelectListener xuanzeSelectListener;

    private SavePPTDialog savePPTDialog; //是否保存弹窗
    private NamedDraftDialog namedDraftDialog; //title 设置弹窗

    //选择 图像选择点击监听
    class XuanzeSelectListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_ci_a:
                    //进入相册
                    nowSelectIv = 1;
                    break;
                case R.id.iv_ci_b:
                    nowSelectIv = 2;
                    break;
                case R.id.iv_ci_c:
                    nowSelectIv = 3;
                    break;
                case R.id.iv_ci_d:
                    nowSelectIv = 4;
                    break;
                default:
                    break;
            }

            //跳转相册
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            if (_mActivity instanceof MainActivity) {
                _mActivity.startActivityForResult(intent, REQUESTCODE_IMAGE_SELECT);
            }

        }
    }

    //预览列表
    static class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.PreviewHolder> {
        private List<PPTPreView> datas;
        private Context mContext;

        private OnItemClickListener mOnItemClickListener;

        public PreviewAdapter(Context context, List<PPTPreView> datas, OnItemClickListener clickListener) {
            this.datas = datas;
            this.mContext = context;
            this.mOnItemClickListener = clickListener;
        }

        interface OnItemClickListener {
            void addItem();  //

            void longClickItem(int pos);

            void changePosition(int pos);
        }

        @NonNull
        @Override
        public PreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_preview, parent, false);
            PreviewHolder previewHolder = new PreviewHolder(itemView);
            return previewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PreviewHolder holder, int position) {
            Log.e("MPF: ", " pos" + position + " " + datas.size());
            if (datas.size() - 1 == position) {  //add item
                holder.mBcImageView.setVisibility(View.GONE);
                holder.mImageView.setImageResource(R.mipmap.add);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            //点击添加
                            mOnItemClickListener.addItem();
                        }
                    }
                });
            } else {
                //相片
                holder.mBcImageView.setVisibility(View.GONE);
                holder.mImageView.setImageResource(R.mipmap.empty);
//                Log.e("MPFT: ", "pos:" + position + " datatype: " + datas.get(position).itemType + " data: " + datas.get(position).mContentList.get(0).contentStr);
                if (datas.get(position).itemType.equals(NORMAL_PPT)) {
                    Log.e("MPFT: ", "" + datas.get(position).mContentList.size());
                    if (datas.get(position).mContentList.size() > 0 && !TextUtils.isEmpty(datas.get(position).mContentList.get(0).contentStr)) {
                        Glide.with(mContext).load(datas.get(position).mContentList.get(0).contentStr).into(holder.mImageView);
                    } else if (datas.get(position).mContentList.size() > 0) {
                        int workStyle = -1;  //题目样式
                        try {
                            String cType = datas.get(position).mContentList.get(0).contentType;
                            workStyle = Integer.valueOf(cType);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (workStyle > 0) {
                            if (workStyle == SampleAdapter.TYPE_XUANZE_TT_CT || workStyle == SampleAdapter.TYPE_XUANZE_TT_CI ||
                                    workStyle == SampleAdapter.TYPE_XUANZE_TI_CT) {
                                Glide.with(mContext).load(R.mipmap.img_xuanze_img_text_4).into(holder.mImageView);
                            } else if (workStyle == SampleAdapter.TYPE_PANDUAN_TI || workStyle == SampleAdapter.TYPE_PANDUAN_TT) {
                                Glide.with(mContext).load(R.mipmap.img_panduan_ti).into(holder.mImageView);
                            } else if (workStyle == SampleAdapter.TYPE_VOICE_TT || workStyle == SampleAdapter.TYPE_VOICE_TI) {
                                Glide.with(mContext).load(R.mipmap.img_audio_img_ti).into(holder.mImageView);
                            } else if (workStyle == SampleAdapter.TYPE_WENDA_TI || workStyle == SampleAdapter.TYPE_WENDA_TT) {
                                Glide.with(mContext).load(R.mipmap.img_jianda_img_ti).into(holder.mImageView);
                            }
                        }


                    }
                }

                if (datas.get(position).isSelected == true) {
                    holder.mBcImageView.setVisibility(View.VISIBLE);
                }

                // 点击 背景
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.changePosition(position);
                        }
                    }
                });

                //长按 弹窗
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.longClickItem(position);
                        }
                        return false;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }


        class PreviewHolder extends RecyclerView.ViewHolder {
            ImageView mImageView, mBcImageView;

            public PreviewHolder(@NonNull View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.iv_preview);
                mBcImageView = itemView.findViewById(R.id.iv_bc);
            }
        }
    }

    class PreViewItenDec extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(8, 0, 8, 0);
        }
    }


}
