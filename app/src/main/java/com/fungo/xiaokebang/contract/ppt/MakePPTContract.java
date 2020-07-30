package com.fungo.xiaokebang.contract.ppt;

import com.fungo.xiaokebang.base.presenter.AbstractPresenter;
import com.fungo.xiaokebang.base.view.AbstractView;
import com.fungo.xiaokebang.core.dao.PPTHistoryData;

import java.util.List;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/28.
 */
public interface MakePPTContract {


    interface View extends AbstractView {

    }


    interface Presenter extends AbstractPresenter<View> {


        void removePreByTitle(String title);
        PPTHistoryData getPreByTitle(String title);
        List<PPTHistoryData> addPreDatas(String presJson, String title);//添加pre 集合

    }

}
