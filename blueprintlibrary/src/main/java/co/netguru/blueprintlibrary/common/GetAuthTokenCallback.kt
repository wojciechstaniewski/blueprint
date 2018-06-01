package co.netguru.blueprintlibrary.common

import android.accounts.*
import android.content.Intent
import android.os.Bundle
import co.netguru.blueprintlibrary.BuildConfig
import co.netguru.blueprintlibrary.R
import co.netguru.blueprintlibrary.common.utils.AccountUtils
import co.netguru.blueprintlibrary.common.view.ErrorHandlingActivity
import javax.inject.Inject


class GetAuthTokenCallback(var activity: ErrorHandlingActivity?, val accountType: String, val authTokenType: String,val accountUtils: AccountUtils, val accountManager: AccountManager) : AccountManagerCallback<Bundle> {

    override fun run(result: AccountManagerFuture<Bundle>) {
        val bundle: Bundle
        val authToken: String?

        try {
            bundle = result.result

            val intent = bundle.get(AccountManager.KEY_INTENT) as Intent?
            if (null != intent) {
                activity!!.startActivityForResult(intent, ErrorHandlingActivity.REQ_SIGNUP)
            } else {
                authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN)
                val accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME)
                // If the logged account didn't exist, we need to create it on the device
                var account = accountUtils.getAccount(accountName, accountType)
                if (null == account) {
                    account = Account(accountName, accountType)
                    accountManager.addAccountExplicitly(account, bundle.getString(activity!!.getString(R.string.password)), null)
                    accountManager.setAuthToken(account, authTokenType, authToken)
                    accountManager.setUserData(account, Constants.EXPIRE_IN_TIMESTAMP, bundle.getInt(Constants.EXPIRE_IN_TIMESTAMP).toString())
                }
                activity!!.accountCreatedEvent.onSuccess(Unit)

            }
        } catch (e: OperationCanceledException) {
            // If signup was cancelled, force activity termination
            onCreateAccountError(e)
        } catch (e: Exception) {
            onCreateAccountError(e)
        }
    }

    private fun onCreateAccountError(e: Exception) {
        activity!!.accountCreatedEvent.onError(e)
        activity!!.finish()
    }

    fun destroy(){
        activity = null
    }
}