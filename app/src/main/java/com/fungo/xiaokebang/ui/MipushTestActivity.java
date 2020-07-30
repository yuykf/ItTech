package com.fungo.xiaokebang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.fungo.xiaokebang.MainActivity;
import com.fungo.xiaokebang.utils.JSONUtils;
import com.orhanobut.logger.Logger;
import com.umeng.message.UmengNotifyClickActivity;

import org.android.agoo.common.AgooConstants;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/27.
 */
public class MipushTestActivity extends UmengNotifyClickActivity {

    private static String TAG = MipushTestActivity.class.getName();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);
        final String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        Log.i(TAG, body);
        WrapUmengNotificationClickHandler handler = new WrapUmengNotificationClickHandler();
        try {
            handler.handleAction(this, JSONUtils.getString(body, "extra", "{}"));
        } catch (Exception e) {
            Logger.e(e.getMessage());

            startActivity(new Intent(MipushTestActivity.this, MainActivity.class));
            finish();
        }
        finish();
    }

}
