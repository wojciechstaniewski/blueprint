package co.netguru.blueprint.dependency

import co.netguru.blueprint.authenticator.AccountAuthenticatorService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBuilder {

    @ContributesAndroidInjector
    abstract fun accountAuthenticatorService() : AccountAuthenticatorService
}