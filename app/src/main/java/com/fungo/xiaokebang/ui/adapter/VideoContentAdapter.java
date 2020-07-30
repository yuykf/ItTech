package com.fungo.xiaokebang.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fungo.xiaokebang.R;
import com.fungo.xiaokebang.core.bean.PPTContent;
import com.fungo.xiaokebang.ui.ppt.MakePPTFragment;
import com.fungo.xiaokebang.ui.ppt.SampleAdapter;

import java.io.ObjectStreamClass;
import java.util.List;


/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/10.
 */
public class VideoContentAdapter extends RecyclerView.Adapter<VideoContentAdapter.VideoContentVideHolder> {

    Context context;
    List<PPTContent> pptContents; //
    LayoutInflater layoutInflater;
//    int preindex = -1;

//    int groupIndex = -1; //组index 计算颜色背景

    int[] g1 = {Color.parseColor("#849FAB"), Color.parseColor("#A3B7C0"), Color.parseColor("#C1CFD5"),
            Color.parseColor("#D1DBDF"), Color.parseColor("#E0E7EA")};
    int[] g2 = {Color.parseColor("#C7B072"), Color.parseColor("#D5C495"), Color.parseColor("#E3D7B8"),
            Color.parseColor("#EAE1CA"), Color.parseColor("#F1EBDB")};
    int[] g3 = {Color.parseColor("#9F89A7"), Color.parseColor("#B7A6BD"), Color.parseColor("#CFC4D3"),
            Color.parseColor("#DBD2DE"), Color.parseColor("#E7E1E9")};
    int[] g4 = {Color.parseColor("#B88484"), Color.parseColor("#CAA3A3"), Color.parseColor("#DBC1C1"),
            Color.parseColor("#E4D1D1"), Color.parseColor("#EDE0E0")};
    int[] g5 = {Color.parseColor("#8EA987"), Color.parseColor("#AABEA5"), Color.parseColor("#C6D4C3"),
            Color.parseColor("#D4DED2"), Color.parseColor("#E2E9E1")};

    //
    public VideoContentAdapter(Context context, List<PPTContent> pptContents, OnContentClickListener onContentClickListener) {
        this.context = context;
        this.pptContents = pptContents;
        this.layoutInflater = LayoutInflater.from(context);
//        this.preindex = groupindex;
        this.onContentClickListener = onContentClickListener;
    }

    private OnContentClickListener onContentClickListener;

    public interface OnContentClickListener {
        void clickkCallback(int itemIndex);
    }

    @NonNull
    @Override
    public VideoContentVideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_video_ppt_content, parent, false);
        VideoContentVideHolder contentVideHolder = new VideoContentVideHolder(itemView);
        return contentVideHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoContentVideHolder holder, int position) {
        PPTContent data = pptContents.get(position);
        String datatype = data.contentType;

        int workType = -1;
        try {
            workType =  Integer.valueOf(datatype);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        int colorIndex = position % 5;
//        Log.e("colorIndex: ", colorIndex + " position " + position + "  groupIndex" + groupIndex);
//        switch (groupIndex) {
//            case 0:
//                holder.iv_cover.setBackgroundColor(g1[colorIndex]);
//                break;
//            case 1:
//                holder.iv_cover.setBackgroundColor(g2[colorIndex]);
//                break;
//            case 2:
//                holder.iv_cover.setBackgroundColor(g3[colorIndex]);
//                break;
//            case 3:
//                holder.iv_cover.setBackgroundColor(g4[colorIndex]);
//                break;
//            case 4:
//                holder.iv_cover.setBackgroundColor(g5[colorIndex]);
//                break;
//            default:
//                break;
//        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onContentClickListener) {
                    //
                    Log.e("cIndex: ",   "  " + position);
                    // p1 preindex p2 contentindex
                    onContentClickListener.clickkCallback(position);
                }
            }
        });

//        if (TextUtils.isEmpty(pptContents.get(position).videoUrl)) {
//            //没拍视频的ppt
//            holder.iv_cover.setVisibility(View.VISIBLE);
//            holder.iv_content.setVisibility(View.GONE);
//        } else {
            holder.iv_cover.setVisibility(View.GONE);
            holder.iv_content.setVisibility(View.VISIBLE);
//        }
        //
        if (!TextUtils.isEmpty(pptContents.get(position).videoUrl)){
            holder.iv_already_video.setVisibility(View.VISIBLE);
        }else {
            holder.iv_already_video.setVisibility(View.GONE);
        }

        if (pptContents.get(position).isLuzhiing) {
            holder.iv_mc.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(pptContents.get(position).videoUrl)){
                holder.iv_luzhi_dot.setVisibility(View.VISIBLE);
            }
            holder.iv_cover.setVisibility(View.GONE);
            holder.iv_content.setVisibility(View.VISIBLE);
        }else {
            holder.iv_mc.setVisibility(View.GONE);
            holder.iv_luzhi_dot.setVisibility(View.GONE);
        }

        if (pptContents.get(position).isClicked) {
            holder.iv_mc.setVisibility(View.VISIBLE);
        }

        //视频
        Log.e("pptContents: ", pptContents.get(position).contentStr + "  " + (holder.iv_content == null));
        if (datatype.equals(MakePPTFragment.TYPE_IMAGE)) {
            //图片
            Glide.with(context).load(pptContents.get(position).contentStr).into(holder.iv_content);
        } else if (datatype.equals(MakePPTFragment.TYPE_VIDEO)) {
            Glide.with(context).load(pptContents.get(position).contentStr).into(holder.iv_content);
        } else if (workType >0) {
            //题目
            Glide.with(context).load(null).into(holder.iv_content);
            switch (workType) {
                // //选择题
                case SampleAdapter.TYPE_XUANZE_TI_CT:
                case SampleAdapter.TYPE_XUANZE_TT_CI:
                case SampleAdapter.TYPE_XUANZE_TT_CT:
                    holder.iv_content.setBackgroundResource(R.mipmap.img_xuanze_img_text_4);
                    break;
                //判断题
                case SampleAdapter.TYPE_PANDUAN_TI:
                case SampleAdapter.TYPE_PANDUAN_TT:
                    holder.iv_content.setBackgroundResource(R.mipmap.img_panduan_ti);
                    break;

                //简答
                case SampleAdapter.TYPE_WENDA_TI:
                case SampleAdapter.TYPE_WENDA_TT:
                    holder.iv_content.setBackgroundResource(R.mipmap.img_jianda_img_ti);
                    break;
                case SampleAdapter.TYPE_VOICE_TI:
                case SampleAdapter.TYPE_VOICE_TT:
                    holder.iv_content.setBackgroundResource(R.mipmap.img_audio_img_ti);
                    break;
            }
        }

    }


    @Override
    public int getItemCount() {
        return pptContents.size();
    }


    class VideoContentVideHolder extends RecyclerView.ViewHolder {
        ImageView iv_content;
        ImageView iv_cover;
        ImageView iv_luzhi_dot;
        ImageView iv_mc;
        ImageView iv_already_video;

        public VideoContentVideHolder(@NonNull View itemView) {
            super(itemView);
            iv_mc = itemView.findViewById(R.id.iv_mc);
            iv_content = itemView.findViewById(R.id.iv_video_content);
            iv_cover = itemView.findViewById(R.id.iv_cover);
            iv_luzhi_dot = itemView.findViewById(R.id.iv_luzhi_dot);
            iv_already_video = itemView.findViewById(R.id.iv_already_video);
        }

    }

}

