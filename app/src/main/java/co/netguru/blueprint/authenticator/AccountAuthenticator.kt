package co.netguru.blueprint.authenticator

import android.accounts.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import co.netguru.blueprint.BuildConfig
import co.netguru.blueprint.R
import co.netguru.blueprint.login.view.LoginActivity
import co.netguru.blueprint.services.authentication.ApiAuth
import co.netguru.blueprint.services.authentication.AuthenticationManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class AccountAuthenticator(var context: Context) : AbstractAccountAuthenticator(context) {

    @Inject
    lateinit var authenticationManager: AuthenticationManager

    @Inject
    lateinit var accountManager: AccountManager

    private val TAG: String = AccountAuthenticator::class.java.simpleName

    override fun getAuthTokenLabel(authTokenType: String?): String? {
        Log.i(TAG, "getAuthTokenLabel")
        return if (authTokenType!! == context.getString(R.string.account_type)) {
            authTokenType
        } else null
    }

    override fun confirmCredentials(response: AccountAuthenticatorResponse?, account: Account?, bundle: Bundle?): Bundle? {
        Log.i(TAG, "confirmCredentials")
        return null
    }

    @Throws(NetworkErrorException::class)
    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, bundle: Bundle?): Bundle? {
        Log.i(TAG, "updateCredentials")
        return null
    }

    @SuppressLint("CheckResult")
    @Throws(Exception::class)
    override fun getAuthToken(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, bundle: Bundle?): Bundle {
        Log.i(TAG, "getAuthToken")

        var authToken = accountManager.peekAuthToken(account, authTokenType)
        var auth: ApiAuth? = null
        // Lets give another try to authenticate the user

        if (authToken != null && TextUtils.isEmpty(authToken)) {

            authenticationManager.authenticateClient(account!!.name, accountManager.getPassword(account))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        auth = result
                        authToken = result.accessToken
                    }, { e ->
                        //todo handle exception
                        throw (NetworkErrorException(e))
                    })
        }


        // If we get an authToken - we return it
        if (authToken != null && !TextUtils.isEmpty(authToken)) {
            val result = Bundle()
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account?.name)
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account?.type)
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken)
            result.putParcelable(context.getString(R.string.auth), auth)
            return result
        }


        // If you reach here, person needs to login again. or sign up
        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity which is the AccountsActivity in our case.
        val intent = Intent(context, LoginActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(context.getString(R.string.accountType), account?.type)
        intent.putExtra(context.getString(R.string.authTokenType), authTokenType)

        // This is for the case multiple accounts are stored on the device
        // and the AccountPicker dialog chooses an account without auth token.
        // We can pass out the account name chosen to the user of write it
        // again in the Login activity intent returned.
        if (account != null) {
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name)
        }


        val reply = Bundle()
        reply.putParcelable(AccountManager.KEY_INTENT, intent)
        return reply
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?, features: Array<out String>?): Bundle? {
        val result = Bundle()
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false)
        return result
    }

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle? {
        return null
    }

    override fun addAccount(response: AccountAuthenticatorResponse?, accountType: String?, authTokenType: String?, requiredFeatures: Array<out String>?, options: Bundle?): Bundle {
        val intent = Intent(context, LoginActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        // This key can be anything. Try to use your domain/package
        intent.putExtra(context.getString(R.string.accountType), accountType)
        // This key can be anything too. It's just a way of identifying the token's type (used when there are multiple permissions)
        intent.putExtra(context.getString(R.string.authTokenType), authTokenType)
        // This key can be anything too. Used for your reference. Can skip it too.
        intent.putExtra(context.getString(R.string.isAddingNewAccount), true)

        val reply = Bundle()
        reply.putParcelable(AccountManager.KEY_INTENT, intent)
        return reply
    }

    companion object {
        fun getAccountType(): String {
            return BuildConfig.APPLICATION_ID
        }

        fun getAccountName(): String {
            return BuildConfig.APP_ACCOUNT_NAME
        }

        fun getAuthTokenType(): String {
            return BuildConfig.AUTH_TOKEN_TYPE
        }
    }
}