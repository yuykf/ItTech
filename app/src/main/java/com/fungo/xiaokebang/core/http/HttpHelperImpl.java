package com.fungo.xiaokebang.core.http;

import com.fungo.xiaokebang.core.bean.BaseResponse;
import com.fungo.xiaokebang.core.bean.login.AuthCodeData;
import com.fungo.xiaokebang.core.bean.login.LoginData;
import com.fungo.xiaokebang.core.http.api.XiaoKeBangApis;
import com.fungo.xiaokebang.utils.TokenHelper;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/28.
 */
public class HttpHelperImpl implements HttpHelper {

    private XiaoKeBangApis mXiaoKeBangApi;

    @Inject
    HttpHelperImpl(XiaoKeBangApis geeksApis) {
        this.mXiaoKeBangApi = geeksApis;
    }

    //String phonenumber, String app, String ip, int ts, String signature) {
    //        return mXiaoKeBangApi.getAuthCode(phonenumber, app, ip, ts, signature
    @Override
    public Observable<BaseResponse<AuthCodeData>> getAuthCodeData(String phonenumber, String app, String ip, int ts, String signature) {
        return mXiaoKeBangApi.getAuthCode(phonenumber, app, ip, ts, signature);
    }

    @Override
    public Observable<BaseResponse<LoginData>> getLoginData(String phoneNumber, String app, String udid, String ip, String osType, String deviceToken,
                                                            int captcha, String signature) {
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("phoneNumber", phoneNumber);
        requestMap.put("app", app);
        requestMap.put("udid", udid);
        requestMap.put("ip", ip);
        requestMap.put("osType", osType);
        requestMap.put("deviceToken", deviceToken);
        requestMap.put("captcha", captcha);
        requestMap.put("signature", signature);
        RequestBody requestBody = TokenHelper.getInstance().createRequestBody(requestMap);

        return mXiaoKeBangApi.getRegisterData(requestBody);
    }


}
