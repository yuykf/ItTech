package com.fungo.xiaokebang.contract.video;

import com.fungo.xiaokebang.base.presenter.AbstractPresenter;
import com.fungo.xiaokebang.base.view.AbstractView;
import com.fungo.xiaokebang.core.bean.PPTPreView;
import com.fungo.xiaokebang.core.dao.PPTHistoryData;

import java.util.List;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/29.
 */
public interface MakeVideoContract {

    interface View extends AbstractView {
    }


    interface Presenter extends AbstractPresenter<View> {

       List<PPTHistoryData> loadAllPPTDatas();

        PPTHistoryData loadPPTDatasByTitle(String title);

    }
}

