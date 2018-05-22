package co.netguru.blueprint.dependency

import co.netguru.blueprint.services.pet.business.PetManager
import co.netguru.blueprint.services.pet.business.PetManagerImpl
import co.netguru.blueprint.services.pet.endpoint.PetService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class PetModule {

    @Provides
    @Singleton
    fun providePetService(adapter: Retrofit): PetService {
        return adapter.create(PetService::class.java)
    }

    @Provides
    @Singleton
    fun providePetManager(petManagerImpl: PetManagerImpl): PetManager {
        return petManagerImpl
    }
}