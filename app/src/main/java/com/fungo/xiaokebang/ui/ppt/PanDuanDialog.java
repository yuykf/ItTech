package com.fungo.xiaokebang.ui.ppt;

import android.content.Context;
import android.view.View;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fungo.xiaokebang.base.fragment.BaseDialog;

import com.fungo.xiaokebang.R;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/20.
 */
public class PanDuanDialog extends BaseDialog {

    private RecyclerView mPdRv;
    private int workType;
    private SampleAdapter sampleAdapter;
    private Context context;


    {
        setLayoutId(R.layout.dialog_panduan);
        hasBottomUP(true);
    }


    @Override
    protected void initView() {
        super.initView();
        mPdRv = mViewHolder.getView(R.id.rv_sample_pd);
        sampleAdapter = new SampleAdapter(workType, context, new SampleAdapter.ClickCallBack() {
            @Override
            public void callback(int workStyle) {
                //点击回调type 中的第几个模板  //返回题目样式
                if (null != selectListener) {
                    //题目样式 答案个数
                    selectListener.select(workStyle); //模板选择结束

                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mPdRv.setLayoutManager(layoutManager);
        mPdRv.setAdapter(sampleAdapter);
        sampleAdapter.notifyDataSetChanged();

    }

    private PanDuanSelectListener selectListener;

    interface PanDuanSelectListener {
        //第一个参数 表示题目类型， 第二个参数
        void select(int workType);
    }

    public void setValue(int type, Context context, PanDuanSelectListener selectListener) {
        this.workType = type; //题目种类  选择题 判断题  问答题  语音题
        this.selectListener = selectListener;
        this.context = context;
        updateView();
    }


}
