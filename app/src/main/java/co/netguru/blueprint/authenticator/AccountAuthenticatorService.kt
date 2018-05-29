package co.netguru.blueprint.authenticator

import android.accounts.AccountManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.android.AndroidInjection
import javax.inject.Inject

class AccountAuthenticatorService : Service() {

    @Inject
    lateinit var authenticator: AccountAuthenticator

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onBind(intent: Intent): IBinder? {

        return if (intent.action == AccountManager.ACTION_AUTHENTICATOR_INTENT) authenticator.iBinder else null
    }
}