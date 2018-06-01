package co.netguru.blueprint.login.dependency

import co.netguru.blueprint.login.view.ForgotPasswordFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ForgotPasswordFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(LoginFragmentModule::class))
    internal abstract fun provideForgotPasswordFragmentFactory(): ForgotPasswordFragment
}