package com.fungo.xiaokebang.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.fungo.xiaokebang.BuildConfig;
import com.fungo.xiaokebang.MainActivity;
import com.fungo.xiaokebang.app.Constants;
import com.orhanobut.logger.Logger;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONObject;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/28.
 */
public class WrapUmengNotificationClickHandler extends UmengNotificationClickHandler {


    @Override
    public void dealWithCustomAction(Context context, UMessage msg) {
        super.dealWithCustomAction(context, msg);

        final String key = "feed_id";
        try {
            JSONObject objAll = msg.getRaw();
            // JSONObject objExtra = objAll.getJSONObject("extra");
            // JSONObject obj = objExtra.getJSONObject("key1");
            // Utils.parsePushMessage(objKey1, context);
            if (BuildConfig.DEBUG)
                Logger.i(msg.getRaw().toString());

            handleAction(context, objAll.getString("extra"));

        } catch (Exception e) {
            Log.e("dealWithCustomAction: ", e.getMessage());
//            Utils.goBackToMainFragmentActivity(context);
        }

    }


    public void handleAction(Context context, String actionContent) throws Exception {

        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);

    }

}

