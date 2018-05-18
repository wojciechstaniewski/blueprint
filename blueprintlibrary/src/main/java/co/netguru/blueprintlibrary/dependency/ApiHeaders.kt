package co.netguru.blueprintlibrary.dependency.modules

import android.accounts.AccountManager
import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class ApiHeaders @Inject constructor(private val context: Context,private val accountType:String
                                     ,private val authTokenType:String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request().newBuilder()

        val accountManager = AccountManager.get(context)
        val account = accountManager.accounts.find { it.type == accountType}
        if (account != null) {
            val token = accountManager.peekAuthToken(account, authTokenType)
            if (token != null) {
                requestBuilder.header("Authorization", "Bearer " + token)
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}