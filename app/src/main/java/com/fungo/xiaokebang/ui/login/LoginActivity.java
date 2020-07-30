package com.fungo.xiaokebang.ui.login;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


import com.fungo.xiaokebang.MainActivity;
import com.fungo.xiaokebang.R;
import com.fungo.xiaokebang.base.activity.BaseActivity;
import com.fungo.xiaokebang.contract.login.LoginContract;
import com.fungo.xiaokebang.presenter.login.LoginPresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {


    private Handler handler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void initEventAndData() {
        mPresenter.getAuthCode("17607001623");
//        Log.e("udide: ", getUDID(LoginActivity.this));
        String udid = getUDID(LoginActivity.this);

        Log.e("udide: ", getUDID(LoginActivity.this) + " " + mPresenter.getDeviceToken());
        //String phoneNumber, String app, String udid, String ip, String osType, String deviceToken, int captcha, String signatur
        mPresenter.getRegister("17607001623", "com.fungo.xiaokebang", udid, "192.189.3.4", "0"
                ,mPresenter.getDeviceToken(), 123456, "abcd" );

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);

    }

    @Override
    public void showRegisteSuccess() {

    }

    protected static final String PREFS_FILE = "gank_device_id.xml";
    protected static final String PREFS_DEVICE_ID = "gank_device_id";
    protected static String uuid;

    String[] permissions = {Manifest.permission.READ_PHONE_STATE};
    /**
     * 获取唯一标识码
     * @param mContext
     * @return
     */
    public  String getUDID(Context mContext) {
        if (uuid == null) {
            if (uuid == null) {
                final SharedPreferences prefs = mContext.getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
                final String id = prefs.getString(PREFS_DEVICE_ID, null);

                if (id != null) {
                    // Use the ids previously computed and stored in the prefs file
                    uuid = id;
                } else {

                    final String androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                    // Use the Android ID unless it's broken, in which case fallback on deviceId,
                    // unless it's not available, then fallback on a random number which we store
                    // to a prefs file
                    try {
                        if (!"9774d56d682e549c".equals(androidId)) {
                            uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                        } else {
                            //添加本次
                            //跳转录制视频
                            RxPermissions rxPermissions = new RxPermissions(LoginActivity.this);
                            rxPermissions.request(permissions)
                                    .subscribe(aBoolean -> {
                                        if (aBoolean) {
                                            @SuppressLint("MissingPermission") final String deviceId = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                            uuid = deviceId!=null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID.randomUUID().toString();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "需要相应的权限", Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    }, Throwable::printStackTrace);

                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }

                    // Write the value out to the prefs file
                    prefs.edit().putString(PREFS_DEVICE_ID, uuid).commit();
                }
            }
        }
        return uuid;
    }
}
