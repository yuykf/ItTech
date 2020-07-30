package com.fungo.xiaokebang.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aliyun.common.utils.ToastUtil;
import com.fungo.xiaokebang.core.bean.PPTContent;
import com.fungo.xiaokebang.core.bean.PPTPreView;

import java.util.ArrayList;
import java.util.List;

import com.fungo.xiaokebang.R;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/10.
 */
public class VideoPreAdapter extends RecyclerView.Adapter<VideoPreAdapter.VidePreVideHolder> {

    private Context context;
    private List<PPTPreView> pptPreViews;
    private LayoutInflater layoutInflater;

    private VideoPreClickListener videoPreClickListener;
    public VideoPreAdapter(Context context, List<PPTPreView> pptpreViews ,VideoPreClickListener videoPreClickListener) {
        this.context = context;
        this.pptPreViews = pptpreViews;
        this.videoPreClickListener = videoPreClickListener;
        layoutInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public VidePreVideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_video_pre, parent, false);
        VidePreVideHolder videHolder = new VidePreVideHolder(view);
        return videHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VidePreVideHolder holder, int position) {

        holder.pptContents.clear();
        //注入对应preview 的内容list
        holder.pptContents.addAll(pptPreViews.get(position).mContentList);

        //拍摄 或点击 设置间隔  pptPreViews.get(0).mContentList.size()
        if (pptPreViews.get(position).mContentList.size() <= 0) {
            return;
        }
        if (pptPreViews.get(position).isSelected || pptPreViews.get(position).mContentList.get(0).isLuzhiing) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) holder.recyclerView.getLayoutParams();
            lp.setMargins(dp2px(context,5), 0, dp2px(context,5), 0);
        }else {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) holder.recyclerView.getLayoutParams();
            lp.setMargins(dp2px(context,0), 0, dp2px(context,0), 0);
        }

//        if (holder.videoContentAdapter == null) {
            holder.videoContentAdapter = new VideoContentAdapter(context, holder.pptContents, new VideoContentAdapter.OnContentClickListener() {
                @Override
                    public void clickkCallback(int itemIndex) {
                    if (videoPreClickListener != null) {
                        Log.e("pindex: ", position + "");
                        videoPreClickListener.clickItem(position, itemIndex);
                    }
                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(holder.videoContentAdapter);
            holder.videoContentAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return pptPreViews == null ? 0 : pptPreViews.size();
    }

    class VidePreVideHolder extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;
        List<PPTContent> pptContents = new ArrayList<>();

        VideoContentAdapter videoContentAdapter;

        public VidePreVideHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.rv_video_pre);
        }
    }


    public interface VideoPreClickListener {
        void clickItem(int gp, int ip);
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());

    }

}
