package co.netguru.blueprint.login.dependency

import co.netguru.blueprint.login.view.LoginFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LoginFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(LoginFragmentModule::class))
    internal abstract fun provideLoginFragmentFactory(): LoginFragment
}