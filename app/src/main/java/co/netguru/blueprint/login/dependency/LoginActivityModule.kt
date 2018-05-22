package co.netguru.blueprint.login.dependency

import co.netguru.blueprint.login.navigation.LoginNavigationHelper
import co.netguru.blueprint.login.view.LoginActivity
import dagger.Module
import dagger.Provides

@Module(subcomponents = arrayOf(LoginViewModelSubComponent::class))
class LoginActivityModule {

    @Provides
    fun provideLoginNavigationHelper(loginActivity: LoginActivity) =
            LoginNavigationHelper(loginActivity)
}
