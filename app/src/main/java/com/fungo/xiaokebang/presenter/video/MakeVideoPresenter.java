package com.fungo.xiaokebang.presenter.video;

import com.fungo.xiaokebang.base.presenter.BasePresenter;
import com.fungo.xiaokebang.core.DataManager;

import javax.inject.Inject;

import com.fungo.xiaokebang.contract.video.MakeVideoContract;
import com.fungo.xiaokebang.core.dao.PPTHistoryData;

import java.util.List;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/29.
 */
public class MakeVideoPresenter extends BasePresenter<MakeVideoContract.View> implements MakeVideoContract.Presenter {

    DataManager mDataManager;

    @Inject
    public MakeVideoPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }


    @Override
    public List<PPTHistoryData> loadAllPPTDatas() {
        return mDataManager.loadAllHisPreDatas();
    }

    @Override
    public PPTHistoryData loadPPTDatasByTitle(String title) {
        return mDataManager.loadPPTDatasByTitle(title);
    }


}
