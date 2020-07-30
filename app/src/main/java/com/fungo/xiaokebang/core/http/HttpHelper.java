package com.fungo.xiaokebang.core.http;

import com.fungo.xiaokebang.core.bean.BaseResponse;
import com.fungo.xiaokebang.core.bean.login.AuthCodeData;
import com.fungo.xiaokebang.core.bean.login.LoginData;

import io.reactivex.Observable;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/28.
 */
public interface HttpHelper {



    /**
     * 获取验证码
     * @param phonenumber
     * @return
     */
    Observable<BaseResponse<AuthCodeData>> getAuthCodeData(String phonenumber,String app,String ip,int ts, String signature);


    /**
     *
     * @param phoneNumber
     * @param app
     * @param udid
     * @param ip
     * @param osType
     * @param deviceToken
     * @param captcha
     * @param signature
     * @return
     */
    Observable<BaseResponse<LoginData>> getLoginData(String phoneNumber, String app, String udid, String ip,
                                                     String osType, String deviceToken, int captcha, String signature);


}
