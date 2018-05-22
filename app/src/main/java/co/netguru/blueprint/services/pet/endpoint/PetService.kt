package co.netguru.blueprint.services.pet.endpoint

import co.netguru.blueprint.services.pet.domain.Pet
import co.netguru.blueprint.services.pet.domain.Status
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface PetService {

    @Headers("Content-Type:application/json")
    @POST("/pet")
    fun addPet(@retrofit2.http.Body pet: Pet): Single<Pet>

    @Headers("Content-Type:application/json")
    @PUT("/pet")
    fun updatePet(@retrofit2.http.Body pet: Pet): Single<Pet>

    @Headers("Content-Type:application/json")
    @GET("/pet/findByStatus")
    fun findByStatus(@retrofit2.http.Body status: Status): Single<List<Pet>>

    @Headers("Content-Type:application/json")
    @GET("/pet/{id}")
    fun getPet(@retrofit2.http.Path("id") id: Long?): Single<Pet>

    @Headers("Content-Type:application/json")
    @POST("/pet/{id}")
    fun updatePet(@retrofit2.http.Path("id") id: Long?,
                  name: String,
                  status: String): Single<Response<Unit>>

    @Headers("Content-Type:application/json")
    @DELETE("/pet/{id}")
    fun deletePet(@retrofit2.http.Path("id") id: Long?): Single<Response<Unit>>

    @Headers("Content-Type:application/json")
    @POST("/pet/{id}/uploadImage")
    fun uploadImage(@retrofit2.http.Header("api_key") api_key: String,
                    @retrofit2.http.Path("id") id: Long?,
                    additionalMetadata: String,
                    file: File): Single<Response<Unit>>

}