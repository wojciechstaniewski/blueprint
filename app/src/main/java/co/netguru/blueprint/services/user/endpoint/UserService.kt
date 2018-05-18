package co.netguru.blueprint.services.user.endpoint

import co.netguru.blueprint.services.pet.domain.Pet
import co.netguru.blueprint.services.user.domain.User
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @Headers("Content-Type:application/json")
    @POST("/user")
    fun createUser(@retrofit2.http.Body user: User): Single<Response<Unit>>

    @Headers("Content-Type:application/json")
    @POST("/user/createWithArray")
    fun createWithArray(@retrofit2.http.Body userList: List<User>): Single<Response<Unit>>

    @Headers("Content-Type:application/json")
    @POST("/user/createWithList")
    fun createWithList(@retrofit2.http.Body userList: List<User>): Single<Response<Unit>>

    @Headers("Content-Type:application/json")
    @GET("/user/login")
    fun login(@retrofit2.http.Query("username") username: String?,
              @retrofit2.http.Query("password ") password: String?): Single<Pet>

    @Headers("Content-Type:application/json")
    @GET("/user/logout")
    fun logout(): Single<Response<Unit>>

    @Headers("Content-Type:application/json")
    @PUT("/user/{username}")
    fun updateUser(@retrofit2.http.Path("username") username: String,
                   @retrofit2.http.Body user: User): Single<Response<Unit>>


    @Headers("Content-Type:application/json")
    @DELETE("/user/{username}")
    fun deleteUser(@retrofit2.http.Path("username") username: String): Single<Response<Unit>>

}