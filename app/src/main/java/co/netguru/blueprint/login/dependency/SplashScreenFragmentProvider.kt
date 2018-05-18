package co.netguru.blueprint.login.dependency

import co.netguru.blueprint.login.view.SplashScreenFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SplashScreenFragmentProvider {
    @ContributesAndroidInjector(modules = arrayOf(SplashScreenFragmentModule::class))
    internal abstract fun provideSplashScreenFragmentFactory(): SplashScreenFragment
}