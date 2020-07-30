package com.fungo.xiaokebang.di.component;


import com.fungo.xiaokebang.base.activity.BaseActivity;
import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/23.
 */
@Subcomponent(modules = {AndroidInjectionModule.class})
public interface BaseActivityComponent extends AndroidInjector<BaseActivity> {

    /**
     * 每一个继承于BaseActivity的Activity都继承于同一个子组件
     * //每一个继承BaseActivity的Activity，都共享同一个SubComponent
     */
    @Subcomponent.Builder
    abstract class BaseBuilder extends AndroidInjector.Builder<BaseActivity>{
    }

}
