package co.netguru.blueprint.services.user.business

import co.netguru.blueprint.services.pet.domain.Pet
import co.netguru.blueprint.services.user.domain.User
import co.netguru.blueprint.services.user.endpoint.UserService
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class UserManagerImpl @Inject constructor() : UserManager {

    @Inject
    lateinit var userService: UserService

    override fun createUser(user: User): Single<Response<Unit>> = userService.createUser(user)

    override fun createWithArray(userList: List<User>): Single<Response<Unit>> = userService.createWithArray(userList)

    override fun createWithList(userList: List<User>): Single<Response<Unit>> = userService.createWithList(userList)

    override fun login(username: String?, password: String?): Single<Pet> = userService.login(username, password)

    override fun logout(): Single<Response<Unit>> = userService.logout()

    override fun updateUser(username: String, user: User): Single<Response<Unit>> = userService.updateUser(username, user)

    override fun deleteUser(username: String): Single<Response<Unit>> = userService.deleteUser(username)
}