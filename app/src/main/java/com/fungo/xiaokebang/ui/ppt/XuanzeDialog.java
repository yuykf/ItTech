package com.fungo.xiaokebang.ui.ppt;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aliyun.common.utils.ToastUtil;
import com.fungo.xiaokebang.base.fragment.BaseDialog;

import com.fungo.xiaokebang.R;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/14.
 */
public class XuanzeDialog extends BaseDialog implements View.OnClickListener {

    private RecyclerView mxzRv;
    private ImageView iv_back;
    private int workType;
    private SampleAdapter sampleAdapter;
    private Context context;
    private Button btnnum2, btnnum3, btnnum4;
    private int selectNum;

    private int xzNum = 4;

    {
        setLayoutId(R.layout.dialog_xuanze);
        hasBottomUP(true);
    }

    @Override
    protected void initView() {
        super.initView();

        mxzRv = mViewHolder.getView(R.id.rv_sample);
        sampleAdapter = new SampleAdapter(workType, context, new SampleAdapter.ClickCallBack() {
            @Override
            public void callback(int workStyle) {
                //点击回调type 中的第几个模板  //返回题目样式
                if (null != selectListener) {
                    //题目样式 答案个数
                    selectListener.select(workStyle, xzNum); //模板选择结束
                    ToastUtil.showToast(context, workType + " num:" + xzNum);
                    Log.e("worktype: ", workType + " " + xzNum);

                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mxzRv.setLayoutManager(layoutManager);
        mxzRv.setAdapter(sampleAdapter);
        sampleAdapter.notifyDataSetChanged();

        btnnum2 = mViewHolder.getView(R.id.btn_num2);
        btnnum3 = mViewHolder.getView(R.id.btn_num3);
        btnnum4 = mViewHolder.getView(R.id.btn_num4);

        btnnum2.setOnClickListener(this);
        btnnum3.setOnClickListener(this);
        btnnum4.setOnClickListener(this);
    }


    private SelectListener selectListener;


    @Override
    public void dismiss() {
        super.dismiss();
        xzNum = 4;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_num2:
                xzNum = 2;
                btnnum3.setBackgroundResource(R.drawable.bg_num_select_false);
                btnnum4.setBackgroundResource(R.drawable.bg_num_select_false);
                btnnum2.setBackgroundResource(R.drawable.bg_num_select_true);
                btnnum2.setTextColor(Color.WHITE);
                btnnum3.setTextColor(Color.BLACK);
                btnnum4.setTextColor(Color.BLACK);
                break;
            case R.id.btn_num3:
                xzNum = 3;
                btnnum3.setTextColor(Color.WHITE);
                btnnum2.setTextColor(Color.BLACK);
                btnnum4.setTextColor(Color.BLACK);
                btnnum4.setBackgroundResource(R.drawable.bg_num_select_false);
                btnnum2.setBackgroundResource(R.drawable.bg_num_select_false);
                btnnum3.setBackgroundResource(R.drawable.bg_num_select_true);

                break;
            case R.id.btn_num4:
                xzNum = 4;
                btnnum4.setTextColor(Color.WHITE);
                btnnum2.setTextColor(Color.BLACK);
                btnnum3.setTextColor(Color.BLACK);
                btnnum3.setBackgroundResource(R.drawable.bg_num_select_false);
                btnnum2.setBackgroundResource(R.drawable.bg_num_select_false);
                btnnum4.setBackgroundResource(R.drawable.bg_num_select_true);
                break;
            default:
                break;
        }

    }


    interface SelectListener {
        //第一个参数 表示题目类型， 第二个参数
        void select(int workType, int xuanZeNum);
    }

    public void setValue(int type, Context context, SelectListener selectListener) {
        this.workType = type; //题目种类  选择题 判断题  问答题  语音题
        this.selectListener = selectListener;
        this.context = context;
        updateView();
    }


}
