package com.fungo.xiaokebang.base.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import androidx.annotation.LayoutRes;

import com.fungo.xiaokebang.utils.CommonUtils;

import javax.inject.Inject;

import com.fungo.xiaokebang.base.presenter.AbstractPresenter;
import com.fungo.xiaokebang.base.view.AbstractView;
import com.fungo.xiaokebang.utils.ViewHolder;

import dagger.android.support.AndroidSupportInjection;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/23.
 */
public abstract class BaseDialogFragment<T extends AbstractPresenter> extends AbstractSimpleDialogFragment
        implements AbstractView {

    @Inject
    protected T mPresenter;

    protected int mLayoutId;
    private OnCustomListener mCustomListener;
    public ViewHolder mViewHolder;
    private boolean isBottom;
    private boolean windowTransparent;
    private boolean dialogTransparent = true;
    private DialogInterface.OnDismissListener dismissListener;


    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);

    }

    //继承方式使用时，在构造方法中初始化数据，initView中解析绑定数据
    //展示后需要更新UI时，需判断viewholder是否为空，因为调用show()到onCreateView需要一段时间
    protected void initView() {
    }

    //做一次非空判断，从走initview
    public void updateView() {
        if (mViewHolder != null) {
            initView();
        }
    }

    //会在初始化时调用一次，可通过viewHolder拿到所有控件
    public BaseDialogFragment setCustomListener(OnCustomListener customListener) {
        this.mCustomListener = customListener;
        return this;
    }

    public interface OnCustomListener {
        void onCustom(ViewHolder holder, BaseDialogFragment dialog);
    }


    /**
     * 设置布局id
     */
    public BaseDialogFragment setLayoutId(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
        return this;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) {
            dismissListener.onDismiss(dialog);
        }
    }


    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        super.onDestroyView();
    }


    public void hasBottomUP(boolean isBottom) {
        this.isBottom = isBottom;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            int divierId = getDialog().getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = getDialog().findViewById(divierId);
            if (divider != null) {
                divider.setBackgroundColor(Color.TRANSPARENT);
            }
        } catch (Exception e) {
        }
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        if (isBottom) {
            params.gravity = Gravity.BOTTOM;
            window.setAttributes(params);
        }
        if (windowTransparent) {
            params.dimAmount = 0.0f;
            window.setAttributes(params);
        }
        if (dialogTransparent) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresenter != null) {
            mPresenter = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void showErrorMsg(String errorMsg) {
        if (getActivity() != null) {
            CommonUtils.showSnackMessage(getActivity(), errorMsg);
        }
    }

    @Override
    public void useNightMode(boolean isNightMode) {
    }

    @Override
    public void showNormal() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void showCollectSuccess() {

    }

    @Override
    public void showCancelCollectSuccess() {

    }

    @Override
    public void showLoginView() {

    }

    @Override
    public void showLogoutView() {

    }

    @Override
    public void showToast(String message) {
        if (getActivity() == null) {
            return;
        }
        CommonUtils.showMessage(getActivity(), message);
    }

    @Override
    public void showSnackBar(String message) {
        if (getActivity() == null) {
            return;
        }
        CommonUtils.showSnackMessage(getActivity(), message);
    }


}
