package com.fungo.xiaokebang.presenter.main;

import com.fungo.xiaokebang.base.presenter.BasePresenter;
import com.fungo.xiaokebang.contract.main.CreationContract;
import com.fungo.xiaokebang.core.DataManager;
import com.fungo.xiaokebang.core.dao.PPTHistoryData;

import java.util.List;

import javax.inject.Inject;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/28.
 */
public class CreationPresenter extends BasePresenter<CreationContract.View> implements CreationContract.Presenter {


    private DataManager mDataManager;

    @Inject
    public CreationPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }


    @Override
    public void attachView(CreationContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {

    }

    //获取草稿数据
    @Override
    public void getDraftData() {
        mView.showDraftList(mDataManager.loadAllHisPreDatas());
    }

    //根据title 删除草稿
    @Override
    public void removeFraftByTitle(String title) {
        mView.showDraftList(mDataManager.deleteHistoryDataByTitle(title));
    }

    //重命名
    @Override
    public void changeDratTitle(String oldTitle, String newTitle) {
        mView.showDraftList(mDataManager.renameHistoryTitle( oldTitle,  newTitle));

    }


}
