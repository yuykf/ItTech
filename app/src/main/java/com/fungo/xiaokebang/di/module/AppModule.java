package com.fungo.xiaokebang.di.module;



import com.fungo.xiaokebang.core.DataManager;
import com.fungo.xiaokebang.core.http.HttpHelper;
import com.fungo.xiaokebang.core.http.HttpHelperImpl;

import javax.inject.Singleton;

import com.fungo.xiaokebang.app.ItTechApplication;
import com.fungo.xiaokebang.core.db.DbHelper;
import com.fungo.xiaokebang.core.db.DbHelperImpl;
import com.fungo.xiaokebang.core.prfs.PreferenceHelper;
import com.fungo.xiaokebang.core.prfs.PreferenceHelperImpl;
import dagger.Module;
import dagger.Provides;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/23.
 */
@Module
public class AppModule {

    private final ItTechApplication application;

    public AppModule(ItTechApplication application) {
        this.application = application;
    }


    @Provides
    @Singleton
    public DataManager provideDataManager(HttpHelper httpHelper, DbHelper dbhelper, PreferenceHelper preferencesHelper) {
        return new DataManager(httpHelper, dbhelper, preferencesHelper);
    }


    @Provides
    @Singleton
    ItTechApplication provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    HttpHelper provideHttpHelper(HttpHelperImpl httpHelperImpl) {
        return httpHelperImpl;
    }

    @Provides
    @Singleton
    DbHelper provideDBHelper(DbHelperImpl realmHelper) {
        return realmHelper;
    }

    @Provides
    @Singleton
    PreferenceHelper providePreferencesHelper(PreferenceHelperImpl implPreferencesHelper) {
        return implPreferencesHelper;
    }

}
