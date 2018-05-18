package co.netguru.blueprint.services.pet.business

import co.netguru.blueprint.services.pet.domain.Pet
import co.netguru.blueprint.services.pet.domain.Status
import co.netguru.blueprint.services.pet.endpoint.PetService
import io.reactivex.Single
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class PetManagerImpl @Inject constructor() : PetManager {

    @Inject
    lateinit var petService: PetService

    override fun addPet(pet: Pet): Single<Pet> = petService.addPet(pet)

    override fun updatePet(pet: Pet): Single<Pet> = petService.updatePet(pet)

    override fun findByStatus(status: Status): Single<List<Pet>> = petService.findByStatus(status)

    override fun getPet(id: Long?): Single<Pet> = petService.getPet(id)

    override fun updatePet(id: Long?, name: String, status: String): Single<Response<Unit>> =
            petService.updatePet(id, name, status)

    override fun deletePet(id: Long?): Single<Response<Unit>> = petService.deletePet(id)

    override fun uploadImage(api_key: String, id: Long?, additionalMetadata: String, file: File):
            Single<Response<Unit>> = petService.uploadImage(api_key, id, additionalMetadata, file)


}