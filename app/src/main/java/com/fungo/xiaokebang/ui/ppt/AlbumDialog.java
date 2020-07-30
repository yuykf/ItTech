//package com.fungo.xiaokebang.ui.ppt;
//
//import android.database.Cursor;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//
//import androidx.annotation.Nullable;
//import androidx.fragment.app.FragmentActivity;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.fungo.xiaokebang.base.fragment.BaseDialog;
//import com.fungo.xiaokebang.contract.ppt.AlbumContract;
//import com.zhihu.matisse.internal.entity.Album;
//import com.zhihu.matisse.internal.entity.Item;
//import com.zhihu.matisse.internal.entity.SelectionSpec;
//import com.zhihu.matisse.internal.model.AlbumCollection;
//import com.zhihu.matisse.internal.model.AlbumMediaCollection;
//import com.zhihu.matisse.internal.model.SelectedItemCollection;
//import cn.htcy.matisselib.interna.widget.CheckRadioView;
//import cn.htcy.matisselib.interna.widget.MediaGridInset;
//import com.zhihu.matisse.internal.utils.UIUtils;
//
//import butterknife.BindView;
//import com.fungo.xiaokebang.R;
//
///**
// * Class:
// * Other:
// * Create by yuy on  2020/7/2.
// */
//public class AlbumDialog extends
//        BaseDialog implements
//        AlbumAdapter.CheckStateListener,
//        AlbumAdapter.OnMediaClickListener,
//        AlbumAdapter.OnPhotoCapture,
//        AlbumCollection.AlbumCallbacks,
//        AlbumMediaCollection.AlbumMediaCallbacks {
//
//    {
//        setLayoutId(R.layout.dialog_album);
//        hasBottomUP(true);
//    }
//
//    private TextView mButtonPreview;
//    private TextView mButtonApply;
//    private View mContainer;
//    private View mEmptyView;
//
//    private LinearLayout mOriginalLayout;
//    private CheckRadioView mOriginal;
//    private boolean mOriginalEnable;
//    private SelectionSpec mSpec;
//    private final AlbumCollection mAlbumCollection = new AlbumCollection();
//    private SelectedItemCollection mSelectedCollection = new SelectedItemCollection(getActivity());
//    private AlbumAdapter.CheckStateListener mCheckStateListener;
//    private AlbumAdapter.OnMediaClickListener mOnMediaClickListener;
//
//    private SelectionProvider mSelectionProvider;
//
//    public static final String EXTRA_ALBUM = "extra_album";
//    @BindView(R.id.recyclerview)
//    RecyclerView recyclerview;
//
//    private AlbumAdapter mAlbumAdapter;
//
//    private final AlbumMediaCollection mAlbumMediaCollection = new AlbumMediaCollection();
//
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mAlbumCollection.onCreate(getActivity(), this);
//        mAlbumCollection.onRestoreInstanceState(savedInstanceState);
//        mAlbumCollection.loadAlbums();
//    }
//
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mSelectedCollection.onSaveInstanceState(outState);
//        mAlbumCollection.onSaveInstanceState(outState);
//        outState.putBoolean("checkState", mOriginalEnable);
//    }
//
//    @Override
//     public void onDestroy() {
//        super.onDestroy();
//        mAlbumCollection.onDestroy();
//        mSpec.onCheckedListener = null;
//        mSpec.onSelectedListener = null;
//    }
//
//
//    public void show(FragmentActivity activity) {
//        try {
//            //在每个add事务前增加一个remove事务，防止连续的add
//            activity.getSupportFragmentManager().beginTransaction().remove(this).commit();
//        } catch (Exception e) {
//            //同一实例使用不同的tag会异常,这里捕获一下
//            e.printStackTrace();
//        }
//        super.show(activity.getSupportFragmentManager(), "");
//    }
//
//    @Override
//    public void onAlbumMediaLoad(Cursor cursor) {
//        mAlbumAdapter.swapCursor(cursor);
//    }
//
//    @Override
//    public void onAlbumMediaReset() {
//        mAlbumAdapter.swapCursor(null);
//    }
//
//    @Override
//    public void onUpdate() {
//
//    }
//
//    @Override
//    public void onMediaClick(Album album, Item item, int adapterPosition) {
//
//    }
//
//
//    public interface SelectionProvider {
//        SelectedItemCollection provideSelectedItemCollection();
//    }
//
//
//    @Override
//    public void capture() {
//
//    }
//
//
//    @Override
//    public void onAlbumLoad(Cursor cursor) {
//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                cursor.moveToPosition(mAlbumCollection.getCurrentSelection());
//                Album album = Album.valueOf(cursor);
//                if (album.isAll() && SelectionSpec.getInstance().capture) {
//                    album.addCaptureCount();
//                }
//                initAlbumRv(album);  //创建相册RV
//            }
//
//        });
//
//    }
//
//
//    private void initAlbumRv(Album album) {
//        mAlbumAdapter = new AlbumAdapter(getActivity(), mSelectedCollection, recyclerview);
//        mAlbumAdapter.registerCheckStateListener(this);
//        mAlbumAdapter.registerOnMediaClickListener(this);
//        recyclerview.setHasFixedSize(true);
//
//        int spanCount;
//        SelectionSpec selectionSpec = SelectionSpec.getInstance();
//        if (selectionSpec.gridExpectedSize > 0) {
//            spanCount = UIUtils.spanCount(getContext(), selectionSpec.gridExpectedSize);
//        } else {
//            spanCount = selectionSpec.spanCount;
//        }
//
//        recyclerview.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
//        int spacing = getResources().getDimensionPixelSize(com.zhihu.matisse.R.dimen.media_grid_spacing);
//        recyclerview.addItemDecoration(new MediaGridInset(spanCount, spacing, false));
//        recyclerview.setAdapter(mAlbumAdapter);
//        mAlbumMediaCollection.onCreate(getActivity(), this);
//        mAlbumMediaCollection.load(album, selectionSpec.capture);
//
//    }
//
//    @Override
//    public void onAlbumReset() {
//
//    }
//}
