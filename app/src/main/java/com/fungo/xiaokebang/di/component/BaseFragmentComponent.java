package com.fungo.xiaokebang.di.component;


import com.fungo.xiaokebang.base.fragment.BaseFragment;
import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/23.
 */
@Subcomponent(modules = AndroidInjectionModule.class)
public interface BaseFragmentComponent extends AndroidInjector<BaseFragment> {

    /**
     * 每一个继承于BaseDialogFragment的Fragment都继承于同一个子组件
     */
    @Subcomponent.Builder
    abstract class BaseBuilder extends AndroidInjector.Builder<BaseFragment>{

    }

}
