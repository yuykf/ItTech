package com.fungo.xiaokebang.presenter.main;

import javax.inject.Inject;

import com.fungo.xiaokebang.base.presenter.BasePresenter;
import com.fungo.xiaokebang.contract.main.MainContract;
import com.fungo.xiaokebang.core.DataManager;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/28.
 */
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public MainPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }

    @Override
    public void attachView(MainContract.View view) {
        super.attachView(view);
    }


}
