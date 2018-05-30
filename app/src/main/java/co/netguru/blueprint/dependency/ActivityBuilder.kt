package co.netguru.blueprint.dependency

import co.netguru.blueprint.login.dependency.LoginActivityModule
import co.netguru.blueprint.login.dependency.LoginFragmentProvider
import co.netguru.blueprint.login.dependency.SignInAndRegisterFragmentProvider
import co.netguru.blueprint.login.dependency.SplashScreenFragmentProvider
import co.netguru.blueprint.login.view.LoginActivity
import co.netguru.blueprint.main.dependency.MainActivityModule
import co.netguru.blueprint.main.dependency.PetsScreenFragmentProvider
import co.netguru.blueprint.main.dependency.WelcomeScreenFragmentProvider
import co.netguru.blueprint.main.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class,
            SplashScreenFragmentProvider::class,
            WelcomeScreenFragmentProvider::class,
            PetsScreenFragmentProvider::class))
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = arrayOf(LoginActivityModule::class,
            LoginFragmentProvider::class, SignInAndRegisterFragmentProvider::class))
    internal abstract fun bindLoginActivity(): LoginActivity

}
