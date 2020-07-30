package com.fungo.xiaokebang.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/22.
 */
public abstract class AbstractSimpleDialogFragment extends DialogFragment {

    private Unbinder unBinder;
    public View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayout(), container, false);
        unBinder = ButterKnife.bind(this, mRootView);
        initEventAndData();
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unBinder != null && unBinder != Unbinder.EMPTY) {
            unBinder.unbind();
            unBinder = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            //防止连续点击add多个fragment
            manager.beginTransaction().remove(this).commit();
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract int getLayout();

    /**
     * 初始化数据
     */
    protected abstract void initEventAndData();
}
