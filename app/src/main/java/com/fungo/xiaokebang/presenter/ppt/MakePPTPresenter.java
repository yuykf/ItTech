package com.fungo.xiaokebang.presenter.ppt;

import com.fungo.xiaokebang.base.presenter.BasePresenter;
import com.fungo.xiaokebang.contract.ppt.MakePPTContract;
import com.fungo.xiaokebang.core.DataManager;
import com.fungo.xiaokebang.core.dao.PPTHistoryData;

import java.util.List;

import javax.inject.Inject;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/28.
 */
public class MakePPTPresenter extends BasePresenter<MakePPTContract.View> implements MakePPTContract.Presenter {

    DataManager mDataManager;

    @Inject
    public MakePPTPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }


    @Override
    public PPTHistoryData getPreByTitle(String title) {
        return mDataManager.loadPPTDatasByTitle(title);
    }

    //pre
    @Override
    public void removePreByTitle(String title) {
        mDataManager.deleteHistoryDataByTitle(title); //
    }


    //添加pre
    @Override
    public List<PPTHistoryData> addPreDatas(String presJson, String title) {
       return  mDataManager.addPreDataData(presJson, title); //
    }
}
