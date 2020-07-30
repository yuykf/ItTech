package com.fungo.xiaokebang.base.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/19.
 */
public abstract class AbstractSimpleActivity extends SupportActivity {

    private Unbinder unBinder; //ButterKnife
    protected AbstractSimpleActivity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unBinder = ButterKnife.bind(this);
        mActivity = this;
        onViewCreated(savedInstanceState);
        initToolbar();
        initEventAndData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unBinder != null && unBinder != Unbinder.EMPTY) {
            unBinder.unbind();
            unBinder = null;
        }
    }


    /**
     * 在initEventAndData()之前执行
     */
    protected abstract void onViewCreated(Bundle savedInstanceState);

    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化ToolBar
     */
    protected abstract void initToolbar();

    /**
     * 初始化数据
     */
    protected abstract void initEventAndData();


}
