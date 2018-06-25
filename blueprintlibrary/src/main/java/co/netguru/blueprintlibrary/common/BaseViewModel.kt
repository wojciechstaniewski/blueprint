package co.netguru.blueprintlibrary.common

import android.arch.lifecycle.ViewModel
import co.netguru.blueprintlibrary.common.utils.HttpStatus
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

open class BaseViewModel : ViewModel() {

    var repository: Repository = Repository()

    fun logout() {
        val responseBody: ResponseBody = ResponseBody.create(MediaType.parse("text/plain"), "logout")
        val response: Response<String> = Response.error(HttpStatus.UNAUTHORIZED.value(), responseBody)
        repository.logoutEvent.onError(HttpException(response))
    }
}