package co.netguru.blueprint.services.store.business

import co.netguru.blueprint.services.pet.domain.Pet
import co.netguru.blueprint.services.store.endpoint.StoreService
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class StoreManagerImpl @Inject constructor() : StoreManager {

    @Inject
    lateinit var storeService: StoreService

    override fun petInventoriesByStatus(): Single<Map<String, Int>> = storeService.petInventoriesByStatus()

    override fun placeAnOrderForPet(pet: Pet): Single<Pet> = storeService.placeAnOrderForPet(pet)

    override fun findPurchaseOrderById(id: Long?): Single<Pet> = storeService.findPurchaseOrderById(id)

    override fun deletePurchaseOrderById(id: Long?): Single<Response<Void>> = storeService.deletePurchaseOrderById(id)
}