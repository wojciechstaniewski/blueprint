package co.netguru.blueprint.main.dependency

import co.netguru.blueprint.main.navigation.MainNavigationHelper
import co.netguru.blueprint.main.view.MainActivity
import co.netguru.blueprintlibrary.common.utils.SharedPrefsUtils
import dagger.Module
import dagger.Provides

@Module(subcomponents = arrayOf(MainViewModelSubComponent::class))
internal class MainActivityModule {

    @Provides
    fun provideMainNavigationHelper(mainActivity: MainActivity, sharedPrefsUtils: SharedPrefsUtils) =
            MainNavigationHelper(mainActivity,sharedPrefsUtils)

}