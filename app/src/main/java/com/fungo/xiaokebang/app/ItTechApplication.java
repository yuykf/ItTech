package com.fungo.xiaokebang.app;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.fungo.xiaokebang.core.dao.DaoMaster;
import com.fungo.xiaokebang.core.dao.DaoSession;
import com.fungo.xiaokebang.core.prfs.PreferenceHelperImpl;
import com.fungo.xiaokebang.di.component.AppComponent;
import com.fungo.xiaokebang.di.component.DaggerAppComponent;
import com.fungo.xiaokebang.di.module.AppModule;
import com.fungo.xiaokebang.di.module.HttpModule;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.mezu.MeizuRegister;
import org.android.agoo.oppo.OppoRegister;
import org.android.agoo.vivo.VivoRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

import javax.inject.Inject;


import dagger.android.DispatchingAndroidInjector;

import dagger.android.AndroidInjector;
import dagger.android.HasActivityInjector;
import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/28.
 */
public class ItTechApplication extends Application implements HasActivityInjector {

    private static final String TAG = "ItTechApplication";
    private static ItTechApplication instance;
    public static boolean isFirstRun = true;
    private static volatile AppComponent appComponent;
    private DaoSession mDaoSession;

    @Inject
    DispatchingAndroidInjector<Activity> mActivityInjector;

    public static synchronized ItTechApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        QupaiHttpFinal.getInstance().initOkHttpFinal();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(instance))
                .httpModule(new HttpModule())
                .build();

        appComponent.inject(this);



        Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(true) // 实际场景建议.debug(BuildConfig.DEBUG)
                /**
                 * 可以获取到{@link me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning}
                 * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                 */
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                        // Bugtags.sendException(e);
                    }
                })
                .install();

        initGreenDao();

        initUMeng();
    }

    //友盟初始化
    private void initUMeng() {
        Log.i(TAG, "注册友盟：deviceToken：-------->  ");

        UMConfigure.init(this, "5f1941e856de271dceff40e7", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "d6e7345f91b87c10e0a572f0636d6159");

        //获取消息推送代理示例
        PushAgent mPushAgent = PushAgent.getInstance(this);

        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.i(TAG, "注册成功：deviceToken：-------->  " + deviceToken);
                PreferenceHelperImpl preferenceHelper = new PreferenceHelperImpl();
                preferenceHelper.setDeviceToken(deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e(TAG, "注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });

        /**
         * 初始化厂商通道
         */
        //小米通道
        MiPushRegistar.register(this, "2882303761518516300", "5401851635300");
        //华为通道，注意华为通道的初始化参数在minifest中配置
//        HuaWeiRegister.register(this);
        //魅族通道
//        MeizuRegister.register(this, "填写您在魅族后台APP对应的app id", "填写您在魅族后台APP对应的app key");
        //OPPO通道
        OppoRegister.register(this, "5fbea5a0c2ea4b738af56d2c74e3dec6", "c4cc2aa3a83d44f9a383bcd5c0d007ff");
        //VIVO 通道，注意VIVO通道的初始化参数在minifest中配置
        VivoRegister.register(this);

    }

    private void initGreenDao() {
        //创建数据库
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, Constants.DB_NAME);
        //获取可写数据库
        SQLiteDatabase database = devOpenHelper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(database);
        //获取dao 对象管理器
        mDaoSession = daoMaster.newSession();
    }


    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return mActivityInjector;
    }
}
