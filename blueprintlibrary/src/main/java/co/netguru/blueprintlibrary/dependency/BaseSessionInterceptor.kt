package co.netguru.blueprintlibrary.dependency

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import co.netguru.blueprintlibrary.common.Constants
import co.netguru.blueprintlibrary.common.utils.AccountUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject


class BaseSessionInterceptor
@Inject constructor(private val accountManager: AccountManager,
                    private val accountUtils: AccountUtils,
                    private val authTokenType: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val account = accountUtils.findAccount(authTokenType)
        return if (account != null) {
            when {
                checkIfSessionHasExpired(accountManager, account) -> {
                    accountUtils.setUserData(account,
                            Constants.LAST_REQUEST_TIMESTAMP, null)
                    chain.proceed(chain.request()).newBuilder()
                            .code(Constants.SESSION_EXPIRED_ERROR_CODE).build()
                }
                else -> {
                    accountUtils.setUserData(account,
                            Constants.LAST_REQUEST_TIMESTAMP, System.currentTimeMillis().toString())
                    chain.proceed(chain.request())
                }
            }
        } else {
            chain.proceed(chain.request())
        }
    }

    private fun checkIfSessionHasExpired(accountManager: AccountManager,
                                         account: Account?): Boolean {
        val lastRequestTimeout = accountUtils.getUserLongData(accountManager, account,
                Constants.LAST_REQUEST_TIMESTAMP)
        val expireInTimeout = accountUtils.getUserLongData(accountManager, account,
                Constants.EXPIRE_IN_TIMESTAMP)
        return lastRequestTimeout > 0 && expireInTimeout > 0 &&
                System.currentTimeMillis() - lastRequestTimeout > expireInTimeout
    }

}