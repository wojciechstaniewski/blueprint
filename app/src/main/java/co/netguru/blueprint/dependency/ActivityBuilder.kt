package co.netguru.blueprint.dependency

import co.netguru.blueprint.login.dependency.LoginActivityModule
import co.netguru.blueprint.login.dependency.LoginFragmentProvider
import co.netguru.blueprint.login.dependency.SplashScreenFragmentProvider
import co.netguru.blueprint.login.dependency.WelcomeScreenFragmentProvider
import co.netguru.blueprint.login.view.LoginActivity
import co.netguru.blueprint.main.dependency.MainActivityModule
import co.netguru.blueprint.main.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class, SplashScreenFragmentProvider::class, WelcomeScreenFragmentProvider::class))
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = arrayOf(LoginActivityModule::class, LoginFragmentProvider::class))
    internal abstract fun bindLoginActivity(): LoginActivity

}
