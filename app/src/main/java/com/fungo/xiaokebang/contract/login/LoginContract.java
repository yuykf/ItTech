package com.fungo.xiaokebang.contract.login;

import com.fungo.xiaokebang.base.presenter.AbstractPresenter;
import com.fungo.xiaokebang.base.view.AbstractView;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/27.
 */
public interface LoginContract {

    interface View extends AbstractView {

        void showRegisteSuccess();

    }


    interface LoginPresenter extends AbstractPresenter<View> {

        void getAuthCode(String phoneNumber); //获取验证码

        void getRegister(String phoneNumber, String app, String udid, String ip,
                         String osType, String deviceToken, int captcha, String signature);//

        String getDeviceToken();

    }


}
