package co.netguru.blueprint.services.store.endpoint

import co.netguru.blueprint.services.pet.domain.Pet
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface StoreService {

    @Headers("Content-Type:application/json")
    @GET("/store/inventory")
    fun petInventoriesByStatus(): Single<Map<String, Int>>

    @Headers("Content-Type:application/json")
    @POST("/store/order")
    fun placeAnOrderForPet(@retrofit2.http.Body pet: Pet): Single<Pet>

    @Headers("Content-Type:application/json")
    @GET("/store/order/{orderId}")
    fun findPurchaseOrderById(@retrofit2.http.Path("orderId") id: Long?): Single<Pet>

    @Headers("Content-Type:application/json")
    @DELETE("/store/order/{orderId}")
    fun deletePurchaseOrderById(@retrofit2.http.Path("orderId") id: Long?): Single<Response<Void>>
}