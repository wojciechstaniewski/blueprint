package co.netguru.blueprint.services.user.business

import co.netguru.blueprint.services.pet.domain.Pet
import co.netguru.blueprint.services.user.domain.User
import io.reactivex.Single
import retrofit2.Response

interface UserManager {

    fun createUser(user: User): Single<Response<Unit>>

    fun createWithArray(userList: List<User>): Single<Response<Unit>>

    fun createWithList(userList: List<User>): Single<Response<Unit>>

    fun login(username: String?, password: String?): Single<Pet>

    fun logout(): Single<Response<Unit>>

    fun updateUser(username: String, user: User): Single<Response<Unit>>

    fun deleteUser(username: String): Single<Response<Unit>>

}