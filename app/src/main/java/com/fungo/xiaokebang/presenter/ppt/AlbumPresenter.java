package com.fungo.xiaokebang.presenter.ppt;

import com.fungo.xiaokebang.base.presenter.BasePresenter;
import com.fungo.xiaokebang.contract.ppt.AlbumContract;
import com.fungo.xiaokebang.core.DataManager;

import javax.inject.Inject;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/2.
 */
public class AlbumPresenter extends BasePresenter<AlbumContract.View> implements AlbumContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public AlbumPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }
}
