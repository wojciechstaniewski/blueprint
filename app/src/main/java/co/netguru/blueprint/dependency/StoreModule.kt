package co.netguru.blueprint.dependency

import co.netguru.blueprint.services.store.business.StoreManager
import co.netguru.blueprint.services.store.business.StoreManagerImpl
import co.netguru.blueprint.services.store.endpoint.StoreService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class StoreModule {

    @Provides
    @Singleton
    fun provideStoreService(adapter: Retrofit): StoreService {
        return adapter.create(StoreService::class.java)
    }

    @Provides
    @Singleton
    fun provideStoreManager(storeManagerImpl: StoreManagerImpl): StoreManager {
        return storeManagerImpl
    }
}