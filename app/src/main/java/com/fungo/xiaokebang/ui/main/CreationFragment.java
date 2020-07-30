package com.fungo.xiaokebang.ui.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fungo.xiaokebang.MainActivity;
import com.fungo.xiaokebang.R;
import com.fungo.xiaokebang.app.Constants;
import com.fungo.xiaokebang.base.fragment.BaseFragment;
import com.fungo.xiaokebang.contract.main.CreationContract;
import com.fungo.xiaokebang.core.bean.PPTContent;
import com.fungo.xiaokebang.core.bean.PPTPreView;
import com.fungo.xiaokebang.core.dao.PPTHistoryData;
import com.fungo.xiaokebang.presenter.main.CreationPresenter;
import com.fungo.xiaokebang.ui.MoreHisDialog;
import com.fungo.xiaokebang.ui.ppt.NamedDraftDialog;
import com.fungo.xiaokebang.ui.ppt.SampleAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Class:
 * Other:创作页面
 * Create by yuy on  2020/6/28.
 */
public class CreationFragment extends BaseFragment<CreationPresenter> implements CreationContract.View {

    @BindView(R.id.tv_draft_manager)
    TextView tv_draft_manager;

    @BindView(R.id.rv)
    RecyclerView rv;

    private List<PPTContent> firstPptContents = new ArrayList<>();

    private List<String> titles = new ArrayList<>();

    private List<Long> times = new ArrayList<>();

    public static CreationFragment newInstance() {
        Bundle args = new Bundle();
        CreationFragment fragment = new CreationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //获取相片地址回调
        if (_mActivity instanceof MainActivity) {
            ((MainActivity) _mActivity).setDeleteCallBack(new MainActivity.DeleteCallBack() {
                @Override
                public void deleteHisItems() {
                    //批量删除
                    for (int i = 0; i < firstPptContents.size(); i++) {

                        String titleStr = titles.get(i);
                        if (!TextUtils.isEmpty(titleStr)) {
                            mPresenter.removeFraftByTitle(titleStr);
                        }

                    }
                    //删除后重新拉新
                }
            });
        }

        mPresenter.getDraftData(); //展示草稿列表
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_creation;

    }


    @Override
    protected void initEventAndData() {
    }


    @Override
    protected void initView() {
        super.initView();
    }


    private boolean isManager = false;//管理状态标志

    @OnClick({R.id.iv_start_make, R.id.tv_draft_manager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_start_make:
                //加载ppt 制作页面
                if (_mActivity instanceof MainActivity) {
                    ((MainActivity) _mActivity).switchFragment(Constants.TYPE_MAIN_MAKE);
                }
                break;
            case R.id.tv_draft_manager:
                //
                isManager = !isManager;
                adapter.notifyDataSetChanged();
                if (isManager) {
                    //展示删除dialog
                    if (_mActivity instanceof MainActivity) {
                        ((MainActivity) _mActivity).viaableDelete(true);
                    }
                }else {
                    if (_mActivity instanceof MainActivity) {
                        ((MainActivity) _mActivity).viaableDelete(false);
                    }
                }
                break;
            default:
                break;

        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        Log.e("CF: ", "onSupportVisible " + getTopFragment().getClass().getSimpleName());
        if (_mActivity != null && _mActivity instanceof MainActivity) {
            ((MainBottomVisable) _mActivity).visableBottom(true);
        }

        if (firstPptContents.size() == 0) {
            mPresenter.getDraftData();
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        Log.e("CF: ", "onSupportInvisible ");
        if (_mActivity != null && _mActivity instanceof MainBottomVisable) {
            ((MainBottomVisable) _mActivity).visableBottom(false);
        }
        firstPptContents.clear();
        titles.clear();
        times.clear();
    }

    DraftAdapter adapter;


    @Override
    public void showDraftList(List<PPTHistoryData> historyData) {
        //先情空
         firstPptContents.clear();
         titles.clear();
         times.clear();

        // data
        Log.e("historyData: ", historyData.size() + "");
        if (historyData.size() > 0) {
            for (int i = 0; i < historyData.size(); i++) {
                Log.e("titles: ", historyData.get(i).title);
                titles.add(historyData.get(i).title); //获取title
                times.add(historyData.get(i).date);
                Log.e("historyData: ", historyData.get(i).title + "  data: " + historyData.get(i).getData());
            }

            //填充数据
            if (historyData.size() > 0) {
                Log.e("cstr: ", historyData.size() + "");

                for (int i = 0; i < historyData.size(); i++) {
                    String cstr = historyData.get(i).getData(); //
                    JsonParser jsonParser = new JsonParser();
                    //PPTStoreData
                    JsonObject jsonObject = jsonParser.parse(cstr).getAsJsonObject();
                    //再转JsonArray 加上数据头，即Javabean的类型，获取外部对象中的内部数组对象
                    JsonArray jsonArray = jsonObject.getAsJsonArray("mPreDatas");
                    JsonElement jsonElement = jsonArray.get(0); //第一个
                    PPTPreView firstPre = new Gson().fromJson(jsonElement, new TypeToken<PPTPreView>() {
                    }.getType());

                    PPTContent firstPptContent = firstPre.mContentList.get(0);
                    if (!TextUtils.isEmpty(firstPptContent.titleIvPath)) {
                        Log.e("titleIvPath: ", firstPptContent.titleIvPath);
                    }

                    firstPptContents.add(firstPptContent);//
                }

                for (int i = 0; i < firstPptContents.size(); i++) {
                    Log.e("firstC: ", titles.get(i) + "" + firstPptContents.get(i).contentType);
                }
                Log.e("cstr: firstPptContents", firstPptContents.size() + "");
            }
        }

        adapter = new DraftAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
    }

    private MoreHisDialog moreHisDialog;


    //底部导航可见回调
    public interface MainBottomVisable {

        void visableBottom(boolean isVisable);

        void viaableDelete(boolean isVisable);

    }

    class DraftAdapter extends RecyclerView.Adapter<DraftAdapter.DraftViewHolder> {

        @NonNull
        @Override
        public DraftAdapter.DraftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemview = LayoutInflater.from(_mActivity).inflate(R.layout.item_draft, parent, false);
            DraftViewHolder draftViewHolder = new DraftViewHolder(itemview);
            return draftViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull DraftViewHolder holder, int position) {

            //iv_his
            if (!TextUtils.isEmpty(firstPptContents.get(position).contentStr)) {
                Glide.with(_mActivity).load(firstPptContents.get(position).contentStr).into(holder.iv_his);
            } else {
                String workStyleStr = firstPptContents.get(position).contentType;
                int workStyle = -1;
                try {
                    workStyle = Integer.valueOf(workStyleStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (workStyle > 0) {
                    holder.iv_his.setBackground(null);
                    switch (workStyle) {
                        case SampleAdapter.TYPE_XUANZE_TT_CT:
                        case SampleAdapter.TYPE_XUANZE_TT_CI:
                        case SampleAdapter.TYPE_XUANZE_TI_CT:
                            holder.iv_his.setBackgroundResource(R.mipmap.img_xuanze_img_text_4);
                            break;
                        case SampleAdapter.TYPE_PANDUAN_TI:
                        case SampleAdapter.TYPE_PANDUAN_TT:
                            holder.iv_his.setBackgroundResource(R.mipmap.img_panduan_ti);
                            break;

                        case SampleAdapter.TYPE_WENDA_TI:
                        case SampleAdapter.TYPE_WENDA_TT:
                            holder.iv_his.setBackgroundResource(R.mipmap.img_jianda_img_ti);
                            break;
                        case SampleAdapter.TYPE_VOICE_TI:
                        case SampleAdapter.TYPE_VOICE_TT:
                            holder.iv_his.setBackgroundResource(R.mipmap.img_audio_img_ti);
                            break;
                        default:
                            break;
                    }
                }
            }

            if (isManager) {
                //管理状态 多项删除
                holder.iv_his_c.setImageResource(R.mipmap.home_icon_celloff);

                holder.iv_his_c.setOnClickListener(v -> {
                    firstPptContents.get(position).isDeleteChoose = !firstPptContents.get(position).isDeleteChoose;
                    notifyItemChanged(position);  //刷新单项
                });

                //
                if (firstPptContents.get(position).isDeleteChoose) {
                    holder.iv_his_c.setImageResource(R.mipmap.home_icon_cellon);
                } else {
                    holder.iv_his_c.setImageResource(R.mipmap.home_icon_celloff);
                }


            } else {
                //更多操作状态
                //单项删除 复制 重命名
//                holder.iv_his_c.setBackgroundResource(R.mipmap.home_icon_more);
                holder.iv_his_c.setImageResource(R.mipmap.home_icon_more);

                holder.iv_his_c.setOnClickListener(v -> {

                    String outTitle = titles.get(position);
                    Log.e("outtitle: ", outTitle);
//                    if (null == moreHisDialog) {
                        moreHisDialog = new MoreHisDialog(new MoreHisDialog.MoreCallBack() {
                            @Override
                            public void rename() {
//                                if (namedDraftDialog == null) {
                                    namedDraftDialog = new NamedDraftDialog(new NamedDraftDialog.DraftStrCallBack() {
                                        @Override
                                        public void callbackDraft(String draftStr) {
                                            Log.e("titlechange: ", titles.get(position) + "  " + draftStr);
                                            //                                            //新title
                                            mPresenter.changeDratTitle(titles.get(position),draftStr);
//                                            titles.set(position, draftStr); //刷新题目集合
                                        }
                                    });
//                                }

                                namedDraftDialog.show(_mActivity);
                            }

                            @Override
                            public void delete() {
                                mPresenter.removeFraftByTitle(titles.get(position));
                            }
                        });
//                    }
                    //展示
                    moreHisDialog.show(_mActivity);

                });
            }

            holder.tv_his_title.setText(titles.get(position));
            Date date = new Date(times.get(position)); //日期
            String histime = String.format("更新于 : %tY.%tm.%td", date, date, date);
            holder.tv_his_time.setText(histime + "");
        }


        private NamedDraftDialog namedDraftDialog;

        @Override
        public int getItemCount() {
            return firstPptContents.size();
        }

        class DraftViewHolder extends RecyclerView.ViewHolder {

            ImageView iv_his, iv_his_c;
            TextView tv_his_title, tv_his_time;

            public DraftViewHolder(@NonNull View itemView) {
                super(itemView);
                iv_his = itemView.findViewById(R.id.iv_his);
                iv_his_c = itemView.findViewById(R.id.iv_his_c);
                tv_his_title = itemView.findViewById(R.id.tv_his_title);
                tv_his_time = itemView.findViewById(R.id.tv_his_time);
            }
        }
    }

}
