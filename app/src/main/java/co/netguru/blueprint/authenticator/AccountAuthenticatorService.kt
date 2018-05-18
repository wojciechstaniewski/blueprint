package co.netguru.blueprint.authenticator

import android.accounts.AccountManager
import android.app.Service
import android.content.Intent
import android.os.IBinder

class AccountAuthenticatorService : Service() {

    private val authenticator: AccountAuthenticator
        get() {
            var authenticator: AccountAuthenticator? = null
            if (authenticator == null)
                authenticator = AccountAuthenticator(this)
            return authenticator
        }

    override fun onBind(intent: Intent): IBinder? {
        return if (intent.action == AccountManager.ACTION_AUTHENTICATOR_INTENT) authenticator.iBinder else null
    }
}