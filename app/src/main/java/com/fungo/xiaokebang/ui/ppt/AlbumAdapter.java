package com.fungo.xiaokebang.ui.ppt;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fungo.xiaokebang.ui.ppt.adapter.RecyclerViewCursorAdapter;


import com.fungo.xiaokebang.R;
import cn.htcy.matisselib.internal.entity.Album;
import cn.htcy.matisselib.internal.entity.Item;
import cn.htcy.matisselib.internal.entity.SelectionSpec;
import cn.htcy.matisselib.internal.model.SelectedItemCollection;
import cn.htcy.matisselib.internal.ui.widget.CheckView;
import cn.htcy.matisselib.internal.ui.widget.MediaGrid;

/**
 * Class:
 * Other: 相册adapter
 * Create by yuy on  2020/7/2.
 */
public class AlbumAdapter extends
        RecyclerViewCursorAdapter<RecyclerView.ViewHolder> implements
        MediaGrid.OnMediaGridClickListener {

    private static final int VIEW_TYPE_CAPTURE = 0x01;
    private static final int VIEW_TYPE_MEDIA = 0x02;
    private final SelectedItemCollection mSelectedCollection;
    private final Drawable mPlaceholder;
    private SelectionSpec mSelectionSpec;
    private CheckStateListener mCheckStateListener;
    private OnMediaClickListener mOnMediaClickListener;
    private RecyclerView mRecyclerView;
    private int mImageResize;


    public AlbumAdapter(Context context, SelectedItemCollection selectedCollection, RecyclerView recyclerView) {
        super(null);

        mSelectionSpec = SelectionSpec.getInstance();
        mSelectedCollection = selectedCollection;
        TypedArray ta = context.getTheme().obtainStyledAttributes(new int[]{R.attr.item_placeholder});
        mPlaceholder = ta.getDrawable(0);
        ta.recycle();
        mRecyclerView = recyclerView;

    }

    public interface CheckStateListener {
        void onUpdate();
    }

    public interface OnMediaClickListener {
        void onMediaClick(Album album, Item item, int adapterPosition);
    }

    public interface OnPhotoCapture {
        void capture();
    }


    public void registerCheckStateListener(CheckStateListener listener) {
        mCheckStateListener = listener;
    }

    public void registerOnMediaClickListener(OnMediaClickListener listener) {
        mOnMediaClickListener = listener;
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        MediaViewHolder mediaViewHolder = (MediaViewHolder) holder;

        final Item item = Item.valueOf(cursor);
        mediaViewHolder.mMediaGrid.preBindMedia(new MediaGrid.PreBindInfo(
                getImageResize(mediaViewHolder.mMediaGrid.getContext()),
                mPlaceholder,
                mSelectionSpec.countable,
                holder
        ));
        mediaViewHolder.mMediaGrid.bindMedia(item);
        mediaViewHolder.mMediaGrid.setOnMediaGridClickListener(this);
        setCheckStatus(item, mediaViewHolder.mMediaGrid);
    }


    private int getImageResize(Context context) {
        if (mImageResize == 0) {
            RecyclerView.LayoutManager lm = mRecyclerView.getLayoutManager();
            int spanCount = ((GridLayoutManager) lm).getSpanCount();
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            int availableWidth = screenWidth - context.getResources().getDimensionPixelSize(
                  R.dimen.media_grid_spacing) * (spanCount - 1);
            mImageResize = availableWidth / spanCount;
            mImageResize = (int) (mImageResize * mSelectionSpec.thumbnailScale);
        }
        return mImageResize;
    }



    private void setCheckStatus(Item item, MediaGrid mediaGrid) {
        if (mSelectionSpec.countable) {
            int checkedNum = mSelectedCollection.checkedNumOf(item);
            if (checkedNum > 0) {
                mediaGrid.setCheckEnabled(true);
                mediaGrid.setCheckedNum(checkedNum);
            } else {
                if (mSelectedCollection.maxSelectableReached()) {
                    mediaGrid.setCheckEnabled(false);
                    mediaGrid.setCheckedNum(CheckView.UNCHECKED);
                } else {
                    mediaGrid.setCheckEnabled(true);
                    mediaGrid.setCheckedNum(checkedNum);
                }
            }
        } else {
            boolean selected = mSelectedCollection.isSelected(item);
            if (selected) {
                mediaGrid.setCheckEnabled(true);
                mediaGrid.setChecked(true);
            } else {
                if (mSelectedCollection.maxSelectableReached()) {
                    mediaGrid.setCheckEnabled(false);
                    mediaGrid.setChecked(false);
                } else {
                    mediaGrid.setCheckEnabled(true);
                    mediaGrid.setChecked(false);
                }
            }
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 内容
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_grid_item, parent, false);
        return new MediaViewHolder(v);

    }

    @Override
    public void onThumbnailClicked(ImageView thumbnail, Item item, RecyclerView.ViewHolder holder) {

    }

    @Override
    public void onCheckViewClicked(CheckView checkView, Item item, RecyclerView.ViewHolder holder) {

    }

    private static class MediaViewHolder extends RecyclerView.ViewHolder {

        private MediaGrid mMediaGrid;

        MediaViewHolder(View itemView) {
            super(itemView);
            mMediaGrid = (MediaGrid) itemView;
        }

    }
//
//    private static class CaptureViewHolder extends RecyclerView.ViewHolder {
//
//        private TextView mHint;
//
//        CaptureViewHolder(View itemView) {
//            super(itemView);
//
//            mHint = (TextView) itemView.findViewById(cR.id.hint);
//        }
//    }
}
