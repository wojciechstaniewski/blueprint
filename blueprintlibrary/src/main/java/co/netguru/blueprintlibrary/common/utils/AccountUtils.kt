package co.netguru.blueprintlibrary.common.utils

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Build
import android.util.Log
import co.netguru.blueprintlibrary.common.Constants
import co.netguru.blueprintlibrary.common.GetAuthTokenCallback
import co.netguru.blueprintlibrary.common.view.ErrorHandlingActivity
import javax.inject.Inject

class AccountUtils @Inject constructor(private val accountManager: AccountManager,
                                       private val accountType: String,
                                       private val authTokenType: String) {

    fun getAccount(accountName: String, authTokenType: String): Account? {
        val accounts = getAccountByType(authTokenType)
        return accounts.firstOrNull { it.name.equals(accountName, ignoreCase = true) }
    }

    fun getAuthToken(accountType: String): String {
        val account = getAccountByType(accountType)
        return if (account.isEmpty() || account[0] == null) {
            ""
        } else {
            accountManager.peekAuthToken(account[0], authTokenType)
        }
    }

    fun removeAccount(accountType: String) {
        try {
            val account = findAccount(accountType)
            if (account != null && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                accountManager.removeAccountExplicitly(account)
            }
        } catch (e: Exception) {
            Log.e(AccountUtils::class.java.simpleName, "removeAccount()", e)
        }
    }

    fun findAccount(authTokenType: String) =
            accountManager.accounts.find { it.type == authTokenType }

    fun getUserLongData(accountManager: AccountManager, account: Account?, key: String): Long =
            accountManager.getUserData(account, key)?.toLong()
                    ?: Constants.DEFAULT_USER_LONG_DATA


    fun setUserData(account: Account?, key: String, value: String?) =
            accountManager.setUserData(account, key, value)

    private fun getAccountByType(accountType: String) =
            accountManager.getAccountsByType(accountType)

    fun logoutFromAccountManager(errorHandlingActivity: ErrorHandlingActivity) {
        accountManager.invalidateAuthToken(accountType, getAuthToken(accountType))
        accountManager.getAuthTokenByFeatures(accountType, authTokenType, null,
                errorHandlingActivity, null, null,
                GetAuthTokenCallback(activity = errorHandlingActivity,
                        accountType = accountType, authTokenType = authTokenType, accountUtils = this, accountManager = accountManager), null)
    }

}
