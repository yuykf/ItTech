package com.fungo.xiaokebang.contract.main;

import com.fungo.xiaokebang.base.presenter.AbstractPresenter;
import com.fungo.xiaokebang.base.view.AbstractView;
import com.fungo.xiaokebang.core.dao.PPTHistoryData;

import java.util.List;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/28.
 */
public interface CreationContract {

    interface View extends AbstractView {

        void showDraftList(List<PPTHistoryData> historyData);

    }


    interface Presenter extends AbstractPresenter<View> {

        void getDraftData();

        void removeFraftByTitle(String title);

        void changeDratTitle(String oldTitle, String newTitle);

    }


}
