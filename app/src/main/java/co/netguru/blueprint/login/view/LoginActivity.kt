package co.netguru.blueprint.login.view

import android.accounts.AccountAuthenticatorActivity
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.netguru.blueprint.AppNavigation
import co.netguru.blueprint.R
import co.netguru.blueprint.databinding.ActivityLoginBinding
import co.netguru.blueprint.login.dependency.LoginViewModelSubComponent
import co.netguru.blueprint.login.navigation.LoginNavigationHelper
import co.netguru.blueprint.login.viewmodel.LoginRegisterViewModel
import co.netguru.blueprintlibrary.common.view.BaseActivity
import javax.inject.Inject
import javax.inject.Provider


class LoginActivity : BaseActivity<LoginRegisterViewModel, ActivityLoginBinding>(R.layout.activity_login, LoginRegisterViewModel::class.java) {


    override fun setVariables() {
        baseBinding.setVariable(co.netguru.blueprint.BR.viewModel, baseViewModel)
    }

    @Inject
    lateinit var loginNavigationHelper: LoginNavigationHelper

    @Inject
    lateinit var loginViewModelComponent: Provider<LoginViewModelSubComponent.Builder>

    private var accountAuthenticatorResponse: AccountAuthenticatorResponse? = null
    private var resultBundle: Bundle? = null


    companion object {

        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModelComponent.get().build().inject(baseViewModel)

        accountAuthenticatorResponse = intent.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)
        if (accountAuthenticatorResponse != null) {
            accountAuthenticatorResponse!!.onRequestContinued()
        }

        if (savedInstanceState == null) {
            loginNavigationHelper.navigate(AppNavigation.LOGIN)
        }
    }

    fun setAccountAuthenticatorResult(result: Bundle) {
        resultBundle = result
    }

    override fun finish() {
        if (accountAuthenticatorResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            if (resultBundle != null) {
                accountAuthenticatorResponse!!.onResult(resultBundle)
            } else {
                accountAuthenticatorResponse!!.onError(AccountManager.ERROR_CODE_CANCELED, "canceled")
            }
            accountAuthenticatorResponse = null
        }
        super.finish()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1] is LoginFragment) {
            finish()
        }
        setResult(AccountAuthenticatorActivity.RESULT_CANCELED)
        super.onBackPressed()
    }
}

