package co.netguru.blueprint.services.authentication

import io.reactivex.Single

interface AuthenticationManager {

    fun authenticateClient(userName: String, password: String): Single<ApiAuth>
}