package com.fungo.xiaokebang.di.module;



import com.fungo.xiaokebang.MainActivity;
import com.fungo.xiaokebang.contract.video.MakeVideoContract;
import com.fungo.xiaokebang.di.component.BaseActivityComponent;
import com.fungo.xiaokebang.ui.login.LoginActivity;
import com.fungo.xiaokebang.ui.main.VideoActivity;
import com.orhanobut.logger.LogAdapter;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/23.
 */
@Module(subcomponents = {BaseActivityComponent.class})
public abstract class AbstractAllActivityModule {

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity contributesMainActivityInjector();


    @ContributesAndroidInjector(modules = MakeVideoModule.class)
    abstract VideoActivity contributesVideoActivityInjector();


    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity contributesLoginActivityInjector();
}
