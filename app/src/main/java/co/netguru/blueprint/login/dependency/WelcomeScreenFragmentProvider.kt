package co.netguru.blueprint.login.dependency

import co.netguru.blueprint.login.view.WelcomeScreenFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class WelcomeScreenFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(WelcomeScreenFragmentModule::class))
    internal abstract fun provideWelcomeScreenFragmentFactory(): WelcomeScreenFragment
}