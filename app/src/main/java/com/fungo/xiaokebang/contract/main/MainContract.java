package com.fungo.xiaokebang.contract.main;

import com.fungo.xiaokebang.base.presenter.AbstractPresenter;
import com.fungo.xiaokebang.base.view.AbstractView;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/28.
 */
public interface MainContract {

     interface View extends AbstractView {

        void showLoginSuccess();

    }

     interface Presenter extends AbstractPresenter<View> {


    }

}
