package com.fungo.xiaokebang.ui.main;

import android.os.Bundle;

import com.fungo.xiaokebang.MainActivity;
import com.fungo.xiaokebang.base.fragment.BaseFragment;
import com.fungo.xiaokebang.contract.main.MineContract;
import com.fungo.xiaokebang.presenter.main.MinePresenter;

import com.fungo.xiaokebang.R;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/23.
 */
public class MineFragment extends BaseFragment<MinePresenter> implements MineContract.View {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initEventAndData() {

    }

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        if (_mActivity instanceof MainActivity) {
            ((MainActivity) _mActivity).setBottomVisable(true);
        }
    }
}
