package co.netguru.blueprintlibrary.common.interceptors

import android.content.Context
import co.netguru.blueprintlibrary.common.error.GeneralErrors
import co.netguru.blueprintlibrary.common.exception.NoConnectivityException
import co.netguru.blueprintlibrary.common.utils.NetworkUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkUtils.isOnline(context)) {
            throw NoConnectivityException(context.getString(GeneralErrors.NO_CONNECTION.message))
        }

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

}
