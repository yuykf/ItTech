package com.fungo.xiaokebang.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;


import com.fungo.xiaokebang.utils.CommonUtils;

import javax.inject.Inject;

import com.fungo.xiaokebang.base.presenter.AbstractPresenter;
import com.fungo.xiaokebang.base.view.AbstractView;

import dagger.android.support.AndroidSupportInjection;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/23.
 */
public abstract class BaseFragment<T extends AbstractPresenter> extends AbstractSimpleFragment
        implements AbstractView {

    @Inject
    protected T mPresenter;


    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (null != mPresenter) {
            mPresenter = null;
        }
    }

    @Override
    public void useNightMode(boolean isNightMode) {
    }

    @Override
    public void showErrorMsg(String errorMsg) {
        if (isAdded()) {
            CommonUtils.showSnackMessage(_mActivity, errorMsg);
        }
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
        CommonUtils.showMessage(_mActivity, message);
    }

    @Override
    public void showSnackBar(String message) {
        CommonUtils.showSnackMessage(_mActivity, message);
    }



    private boolean isBottom;
}
