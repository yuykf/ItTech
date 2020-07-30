package com.fungo.xiaokebang.ui.ppt;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fungo.xiaokebang.R;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/14.
 */
public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.SampleViewHolder> {


    //题目类型 4类 选择、判断、问答、语音
    public static final int WORK_TYPE_XUANZE = 1;
    public static final int WORK_TYPE_PANDUAN = 2;
    public static final int WORK_TYPE_WENDA = 3;
    public static final int WORK_TYPE_VOICE = 4;

    //题目细分
    //选择
    public static final int TYPE_XUANZE_TT_CT = 11; //题文本-选项文本
    public static final int TYPE_XUANZE_TT_CI = 12; //题文本- 选项图片
    public static final int TYPE_XUANZE_TI_CT = 13; //题图片-选项文本

    //判断
    public static final int TYPE_PANDUAN_TT = 21; //判断 题文本
    public static final int TYPE_PANDUAN_TI = 22; //判断 题图像


    //问答
    public static final int TYPE_WENDA_TT = 31; //问答 题文本
    public static final int TYPE_WENDA_TI = 32; //问答 题图像

    //语言
    public static final int TYPE_VOICE_TT = 41; //语言题 题文本
    public static final int TYPE_VOICE_TI = 42; //语言题 题图像


    //题目样式个数
    final int TYPE_XUANZE_SIZE = 3;
    final int TYPE_PANDUAN_SIZE = 2;
    final int TYPE_WENDA_SIZE = 2;
    final int TYPE_VOICE_SIZE = 2;


    private int workType;
    private Context context;
    private LayoutInflater layoutInflater;

    private ClickCallBack clickCallBack;

    public SampleAdapter(int workType, Context context, ClickCallBack clickCallBack) {
        this.workType = workType;
        this.context = context;
        this.clickCallBack = clickCallBack;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_sample_work, parent, false);
        SampleViewHolder viewHolder = new SampleViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SampleViewHolder holder, int position) {
        int index = position % 3;
        int workStyle = -1;
        switch (index) {
            case 0:
                if (workType == WORK_TYPE_XUANZE) {
                    workStyle = TYPE_XUANZE_TT_CT;
                    holder.imageView.setBackgroundResource(R.mipmap.img_xuanze_text_text_4);

                } else if (workType == WORK_TYPE_PANDUAN) {
                    workStyle = TYPE_PANDUAN_TT;
                    holder.imageView.setBackgroundResource(R.mipmap.img_panduan_text_tt);

                } else if (workType == WORK_TYPE_WENDA) {
                    workStyle = TYPE_WENDA_TT;
                    holder.imageView.setBackgroundResource(R.mipmap.img_jianda_text_tt);

                } else if (workType == WORK_TYPE_VOICE) {
                    workStyle = TYPE_VOICE_TT;
                    holder.imageView.setBackgroundResource(R.mipmap.img_audio_text_tt);
                }
                break;
            case 1:
                if (workType == WORK_TYPE_XUANZE) {
                    workStyle = TYPE_XUANZE_TT_CI;
                    holder.imageView.setBackgroundResource(R.mipmap.img_xuanze_text_img_4);
                } else if (workType == WORK_TYPE_PANDUAN) {
                    workStyle = TYPE_PANDUAN_TI;
                    holder.imageView.setBackgroundResource(R.mipmap.img_panduan_ti);

                } else if (workType == WORK_TYPE_WENDA) {
                    workStyle = TYPE_WENDA_TI;
                    holder.imageView.setBackgroundResource(R.mipmap.img_jianda_img_ti);
                } else if (workType == WORK_TYPE_VOICE) {
                    workStyle = TYPE_VOICE_TI;
                    holder.imageView.setBackgroundResource(R.mipmap.img_audio_img_ti);

                }
                break;
            case 2:
                if (workType == WORK_TYPE_XUANZE) {
                    workStyle = TYPE_XUANZE_TI_CT;
                    holder.imageView.setBackgroundResource(R.mipmap.img_xuanze_img_text_4);
                }

                break;
            default:
                break;
        }

        int finalWorkStyle = workStyle;
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallBack != null) {
                    clickCallBack.callback(finalWorkStyle);
                }
            }
        });
    }

    public interface ClickCallBack {
        //第一个参数 表示题目类型， 第二个参数
        void callback(int workStyle);
    }


    @Override
    public int getItemCount() {
        int size = 0;
        switch (workType) {
            case WORK_TYPE_XUANZE:  //题目样式 选择题三个样式
                size = TYPE_XUANZE_SIZE;
                break;
            case WORK_TYPE_PANDUAN:
                size = TYPE_PANDUAN_SIZE;
                break;

            case WORK_TYPE_WENDA:
                size = TYPE_WENDA_SIZE;

                break;
            case WORK_TYPE_VOICE:
                size = TYPE_VOICE_SIZE;
                break;
            default:
                break;
        }
        return size;
    }

    class SampleViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public SampleViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_sample);
        }

    }
}
