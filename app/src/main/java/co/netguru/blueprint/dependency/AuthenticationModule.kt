package co.netguru.blueprint.dependency

import android.accounts.AccountManager
import android.content.Context
import co.netguru.blueprint.authenticator.AccountAuthenticator
import co.netguru.blueprint.services.authentication.business.AuthenticationManager
import co.netguru.blueprint.services.authentication.business.AuthenticationManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AuthenticationModule {

    @Provides
    @Singleton
    fun provideApiAuthenticationManager(authenticationManagerImpl: AuthenticationManagerImpl):
            AuthenticationManager = authenticationManagerImpl

    @Provides
    @Singleton
    fun provideAccountAuthenticator(context: Context,
                                    accountManager:AccountManager,
                                    authenticationManager: AuthenticationManager):
            AccountAuthenticator = AccountAuthenticator(context,accountManager,authenticationManager)
}