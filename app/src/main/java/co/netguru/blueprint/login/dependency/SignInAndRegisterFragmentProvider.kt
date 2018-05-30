package co.netguru.blueprint.login.dependency

import co.netguru.blueprint.login.view.SignInAndRegisterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SignInAndRegisterFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(LoginFragmentModule::class))
    internal abstract fun provideSignInAndRegisterFragmentFactory(): SignInAndRegisterFragment
}