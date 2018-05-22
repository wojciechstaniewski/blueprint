package co.netguru.blueprint.services.pet.business

import co.netguru.blueprint.services.pet.domain.Pet
import co.netguru.blueprint.services.pet.domain.Status
import io.reactivex.Single
import retrofit2.Response
import java.io.File

interface PetManager {

    fun addPet(pet: Pet): Single<Pet>

    fun updatePet(pet: Pet): Single<Pet>

    fun findByStatus(status: Status): Single<List<Pet>>

    fun getPet(id: Long?): Single<Pet>

    fun updatePet(id: Long?,
                  name: String,
                  status: String): Single<Response<Unit>>

    fun deletePet(id: Long?): Single<Response<Unit>>

    fun uploadImage(api_key: String, id: Long?,
                    additionalMetadata: String,
                    file: File): Single<Response<Unit>>
}