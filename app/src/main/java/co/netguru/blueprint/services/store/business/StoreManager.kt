package co.netguru.blueprint.services.store.business

import co.netguru.blueprint.services.pet.domain.Pet
import io.reactivex.Single
import retrofit2.Response

interface StoreManager {

    fun petInventoriesByStatus(): Single<Map<String, Int>>

    fun placeAnOrderForPet( pet: Pet): Single<Pet>

    fun findPurchaseOrderById(id: Long?): Single<Pet>

    fun deletePurchaseOrderById(id: Long?): Single<Response<Void>>
}