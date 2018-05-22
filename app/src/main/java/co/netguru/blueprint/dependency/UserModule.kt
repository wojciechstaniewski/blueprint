package co.netguru.blueprint.dependency

import co.netguru.blueprint.services.user.business.UserManager
import co.netguru.blueprint.services.user.business.UserManagerImpl
import co.netguru.blueprint.services.user.endpoint.UserService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class UserModule {
    @Provides
    @Singleton
    fun provideUserService(adapter: Retrofit): UserService {
        return adapter.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserManager(userManagerImpl: UserManagerImpl): UserManager {
        return userManagerImpl
    }
}
