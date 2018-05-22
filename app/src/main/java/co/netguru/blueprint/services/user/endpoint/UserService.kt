package co.netguru.blueprint.services.user.endpoint

import co.netguru.blueprint.services.pet.domain.Pet
import co.netguru.blueprint.services.user.domain.User
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @Headers("Content-Type:application/json")
    @POST("/v2/user")
    fun createUser(@retrofit2.http.Body user: User): Single<Response<Unit>>

    @Headers("Content-Type:application/json")
    @POST("/v2/user/createWithArray")
    fun createWithArray(@retrofit2.http.Body userList: List<User>): Single<Response<Unit>>

    @Headers("Content-Type:application/json")
    @POST("/v2/user/createWithList")
    fun createWithList(@retrofit2.http.Body userList: List<User>): Single<Response<Unit>>

    @Headers("Content-Type:application/json")
    @GET("/v2/user/login")
    fun login(@retrofit2.http.Query("username") username: String?,
              @retrofit2.http.Query("password ") password: String?): Single<Pet>

    @Headers("Content-Type:application/json")
    @GET("/v2/user/logout")
    fun logout(): Single<Response<Unit>>

    @Headers("Content-Type:application/json")
    @PUT("/v2/user/{username}")
    fun updateUser(@retrofit2.http.Path("username") username: String,
                   @retrofit2.http.Body user: User): Single<Response<Unit>>


    @Headers("Content-Type:application/json")
    @DELETE("/v2/user/{username}")
    fun deleteUser(@retrofit2.http.Path("username") username: String): Single<Response<Unit>>

}