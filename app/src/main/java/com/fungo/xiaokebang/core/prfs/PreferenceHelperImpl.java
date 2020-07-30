package com.fungo.xiaokebang.core.prfs;

import android.content.Context;
import android.content.SharedPreferences;

import com.fungo.xiaokebang.app.Constants;
import com.fungo.xiaokebang.app.ItTechApplication;

import javax.inject.Inject;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/20.
 */
public class PreferenceHelperImpl implements PreferenceHelper {

    private final SharedPreferences mPreferences;

    @Inject
    public PreferenceHelperImpl() {
        mPreferences = ItTechApplication.getInstance().getSharedPreferences(Constants.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
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
        mPreferences.edit().putString(Constants.DEVICETOKEN, deviceToken).apply();
    }

    @Override
    public String getDeviceToken() {
        return mPreferences.getString(Constants.DEVICETOKEN, "");
    }



    @Override
    public void setAutoCacheState(boolean b) {

    }
}
