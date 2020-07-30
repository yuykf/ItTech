package com.fungo.xiaokebang.presenter.login;

import android.text.TextUtils;
import android.util.Log;

import com.fungo.xiaokebang.R;
import com.fungo.xiaokebang.app.ItTechApplication;
import com.fungo.xiaokebang.base.presenter.BasePresenter;
import com.fungo.xiaokebang.contract.login.LoginContract;
import com.fungo.xiaokebang.core.DataManager;
import com.fungo.xiaokebang.core.bean.BaseResponse;
import com.fungo.xiaokebang.core.bean.login.AuthCodeData;
import com.fungo.xiaokebang.core.bean.login.LoginData;
import com.fungo.xiaokebang.utils.BaseObserver;
import com.fungo.xiaokebang.utils.RxUtils;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/27.
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.LoginPresenter {

    private DataManager dataManager;

    @Inject
    public LoginPresenter(DataManager dataManager) {
        super(dataManager);
        this.dataManager = dataManager;
    }

    @Override
    public void getAuthCode(String phoneNumber) {
        dataManager.getAuthCodeData(phoneNumber, "com.fungo.xiaokebang", "196.168.1.1", (int)((new Date().getTime())/1000), "haha")
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<BaseResponse<AuthCodeData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse<AuthCodeData> authCodeDataBaseResponse) {
                        Log.e("authBaseResponse: ", authCodeDataBaseResponse.getData().toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("authBaseResponse e: ", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getRegister(String phoneNumber, String app, String udid, String ip, String osType, String deviceToken, int captcha, String signature) {
        dataManager.getLoginData(phoneNumber, app, udid, ip,osType, deviceToken, captcha, signature)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<BaseResponse<LoginData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse<LoginData> loginDataBaseResponse) {
//                        Log.e("authLoginResponse: ", ((LoginData)(loginDataBaseResponse.getData())).toString());
                        Log.e("authLoginResponse: ", ((loginDataBaseResponse.getData() == null)) + "" + loginDataBaseResponse.getErrorCode() + loginDataBaseResponse.getErrorMsg());
//{"uid":740279,"nickname":"17607001623"}
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("authLoginResponse e: ", e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });

//                        .compose(RxUtils.rxSchedulerHelper())
//                        .compose(RxUtils.handleResult())
//                        .subscribeWith(new BaseObserver<LoginData>(mView,"获取注册信息错误",
//                                false) {
//                            @Override
//                            public void onNext(LoginData loginData) {
//                                Log.e("authLoginResponse: ", loginData.toString());
//
//                            }
//                        });
        //
//                        .subscribeWith(new BaseObserver<BaseResponse<LoginData>>(mView,
//                                "获取注册信息失败",false) {
//                            @Override
//                            public void onNext(BaseResponse<LoginData> loginDataBaseResponse) {
//
//                            }
//                        }));
    }

    @Override
    public String getDeviceToken() {
      return   dataManager.getDeviceToken();
    }


}
