package com.fungo.xiaokebang.di.module;



import com.fungo.xiaokebang.di.component.BaseFragmentComponent;
import com.fungo.xiaokebang.ui.main.CreationFragment;
import com.fungo.xiaokebang.ui.main.MineFragment;
import com.fungo.xiaokebang.ui.ppt.MakePPTFragment;
import com.fungo.xiaokebang.ui.video.MakeVideoFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/23.
 */
@Module(subcomponents = BaseFragmentComponent.class)
public abstract class AbstractAllFragmentModule {

    @ContributesAndroidInjector(modules = CreationModule.class)
    abstract CreationFragment contributesMainPagerFragmentInject();

    @ContributesAndroidInjector(modules = MakePPTModule.class)
    abstract MakePPTFragment contributesMakePPTFragmentInject();


    @ContributesAndroidInjector(modules = MakeVideoModule.class)
    abstract MakeVideoFragment contributesMakeVideoFragmentInject();


    @ContributesAndroidInjector(modules = MineModele.class)
    abstract MineFragment contributesMineFragmentInject();

}
