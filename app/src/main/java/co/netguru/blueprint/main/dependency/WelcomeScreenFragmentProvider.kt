package co.netguru.blueprint.main.dependency

import co.netguru.blueprint.main.view.WelcomeScreenFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class WelcomeScreenFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(WelcomeScreenFragmentModule::class))
    internal abstract fun provideWelcomeScreenFragmentFactory(): WelcomeScreenFragment
}