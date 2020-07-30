package com.fungo.xiaokebang.di.component;



import com.fungo.xiaokebang.core.DataManager;
import com.fungo.xiaokebang.di.module.AbstractAllFragmentModule;
import com.fungo.xiaokebang.di.module.AppModule;

import javax.inject.Singleton;

import com.fungo.xiaokebang.app.ItTechApplication;
import com.fungo.xiaokebang.di.module.AbstractAllActivityModule;
import com.fungo.xiaokebang.di.module.HttpModule;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/23.
 */
@Singleton
@Component(modules = {AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        AbstractAllActivityModule.class,
        AbstractAllFragmentModule.class,
        AppModule.class,
        HttpModule.class})
public interface AppComponent {

    /**
     * 注入WanAndroidApp实例
     *
     * @param application WanAndroidApp
     */
    void inject(ItTechApplication application);

    /**
     * 提供App的Context
     *
     * @return GeeksApp context
     */
    ItTechApplication getContext();

    /**
     * 数据中心
     *
     * @return DataManager
     */
    DataManager getDataManager();


}
