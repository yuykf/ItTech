package com.fungo.xiaokebang.core;

import com.fungo.xiaokebang.core.bean.BaseResponse;
import com.fungo.xiaokebang.core.bean.login.AuthCodeData;
import com.fungo.xiaokebang.core.bean.login.LoginData;
import com.fungo.xiaokebang.core.dao.PPTHistoryData;
import com.fungo.xiaokebang.core.db.DbHelper;
import com.fungo.xiaokebang.core.http.HttpHelper;
import com.fungo.xiaokebang.core.prfs.PreferenceHelper;

import java.util.List;

import io.reactivex.Observable;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/28.
 */
public class DataManager implements HttpHelper, DbHelper, PreferenceHelper {

    private HttpHelper mHttpHelper;
    private DbHelper mDbHelper;
    private PreferenceHelper mPreferenceHelper;

    public DataManager(HttpHelper httpHelper, DbHelper dbHelper, PreferenceHelper preferencesHelper) {
        mHttpHelper = httpHelper;
        mDbHelper = dbHelper;
        mPreferenceHelper = preferencesHelper;
    }

    @Override
    public void setLoginAccount(String account) {

    }

    @Override
    public void setLoginPassword(String password) {

    }

    @Override
    public String getLoginAccount() {
        return null;
    }

    @Override
    public String getLoginPassword() {
        return null;
    }

    @Override
    public void setLoginStatus(boolean isLogin) {

    }

    @Override
    public boolean getLoginStatus() {
        return false;
    }

    @Override
    public void setCookie(String domain, String cookie) {

    }

    @Override
    public String getCookie(String domain) {
        return null;
    }



    @Override
    public boolean getAutoCacheState() {
        return false;
    }

    @Override
    public void setDeviceToken(String deviceToken) {

    }

    @Override
    public String getDeviceToken() {
        return mPreferenceHelper.getDeviceToken();
    }


    @Override
    public void setAutoCacheState(boolean b) {

    }

    @Override
    public List<PPTHistoryData> addPreDataData(String data, String title) {
        return mDbHelper.addPreDataData(data, title);
    }


    @Override
    public void clearHistoryData() {

    }

    //加载所有
    @Override
    public List<PPTHistoryData> loadAllHisPreDatas() {
        return mDbHelper.loadAllHisPreDatas();
    }


    @Override
    public PPTHistoryData loadPPTDatasByTitle(String title) {
        return mDbHelper.loadPPTDatasByTitle(title);
    }

    @Override
    public List<PPTHistoryData> deleteHistoryDataByTitle(String title) {
      return   mDbHelper.deleteHistoryDataByTitle(title);
    }

    @Override
    public List<PPTHistoryData> renameHistoryTitle(String oldTitle, String newTitle) {
        return mDbHelper.renameHistoryTitle( oldTitle,  newTitle);
    }


    @Override
    public Observable<BaseResponse<AuthCodeData>> getAuthCodeData(String phonenumber, String app, String ip, int ts, String signature) {
        return mHttpHelper.getAuthCodeData(phonenumber, app, ip, ts, signature);
    }

    @Override
    public Observable<BaseResponse<LoginData>> getLoginData(String phoneNumber, String app, String udid, String ip, String osType, String deviceToken, int captcha, String signature) {
        return mHttpHelper.getLoginData(phoneNumber, app, udid, ip, osType, deviceToken, captcha, signature);
    }



}
