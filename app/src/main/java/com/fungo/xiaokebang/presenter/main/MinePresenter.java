package com.fungo.xiaokebang.presenter.main;

import com.fungo.xiaokebang.base.presenter.BasePresenter;
import com.fungo.xiaokebang.contract.main.MineContract;
import com.fungo.xiaokebang.core.DataManager;

import javax.inject.Inject;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/23.
 */
public class MinePresenter extends BasePresenter<MineContract.View> implements MineContract.Presenter {

    @Inject
    public MinePresenter(DataManager dataManager) {

        super(dataManager);
    }
}
