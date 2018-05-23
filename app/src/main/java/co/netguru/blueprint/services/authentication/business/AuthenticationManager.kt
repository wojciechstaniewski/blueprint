package co.netguru.blueprint.services.authentication.business

import co.netguru.blueprint.services.authentication.domain.ApiAuth
import io.reactivex.Single

interface AuthenticationManager {

    fun authenticateClient(userName: String, password: String): Single<ApiAuth>
}