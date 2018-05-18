package co.netguru.blueprint.login.view

import android.accounts.Account
import android.accounts.AccountAuthenticatorActivity
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TextInputLayout
import android.view.View
import co.netguru.blueprint.BR
import co.netguru.blueprint.R
import co.netguru.blueprint.authenticator.AccountAuthenticator
import co.netguru.blueprint.databinding.LoginFragmentBinding
import co.netguru.blueprint.login.viewmodel.LoginRegisterViewModel
import co.netguru.blueprintlibrary.common.customViews.CircularProgressButton
import co.netguru.blueprintlibrary.common.utils.DisplayUtils
import co.netguru.blueprintlibrary.common.view.BaseFragment
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


@FragmentWithArgs
class LoginFragment : BaseFragment<LoginRegisterViewModel, LoginFragmentBinding>(R.layout.login_fragment) {

    @Inject
    lateinit var accountManager: AccountManager

    @Arg
    lateinit var title: String

    override fun setVariables() {
        baseBinding.setVariable(BR.viewModel, baseViewModel)
        baseBinding.setVariable(BR.layoutUtils, DisplayUtils)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseViewModel = (activity as LoginActivity).baseViewModel

        handleStartLoginEvent()
        handleSuccessLoginEvent()
        handleErrorLoginEvent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fields.clear()
        fields.add(baseBinding.inputEmail)
        fields.add(baseBinding.inputPassword)
        submitButton = baseBinding.login
        setItemsToValidation()
    }

    private fun handleStartLoginEvent() {
        compositeDisposable.add(baseViewModel.loginEvent.getStartProgressEvent().subscribe {
            dismissNoConnectivityError()
            baseBinding.login.startAnimation()
            Handler().postDelayed({
                baseViewModel.login()
            }, CircularProgressButton.ANIMATION_LENGTH)
        })
    }

    private fun handleSuccessLoginEvent() {
        compositeDisposable.add(baseViewModel.loginEvent.getSuccessStream().subscribe({
            baseBinding.login.stopAnimation()
            finishLogin(it)
        }))
    }

    private fun handleErrorLoginEvent() {
        compositeDisposable.add(baseViewModel.loginEvent.getErrorStream().subscribe({
            baseBinding.login.stopAnimation()
            baseBinding.login.revertAnimation()
            handleError(it, listOf(baseBinding.inputEmail.id, baseBinding.inputPassword.id))
        }))
    }

    private fun finishLogin(intent: Intent) {

        val accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
        val accountPassword = intent.getStringExtra(getString(R.string.password))
        val account = Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE))
        val authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN)

        if (activity!!.intent.getBooleanExtra(getString(R.string.isAddingNewAccount), false)) {
            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            accountManager.addAccountExplicitly(account, accountPassword, null)
            accountManager.setAuthToken(account, AccountAuthenticator.getAuthTokenType(), authToken)
        } else {
            accountManager.setPassword(account, accountPassword)
        }
        setUserData(account, intent)
        (activity as LoginActivity).setAccountAuthenticatorResult(intent.extras)
        (activity as LoginActivity).setResult(AccountAuthenticatorActivity.RESULT_OK, intent)
        (activity as LoginActivity).finish()
    }

    private fun setUserData(account: Account, intent: Intent) {
        /*      if (BuildConfig.ENABLE_DEMO) {
                  AccountUtils.setUserData(accountManager, account, LoginActivity.STATUS,
                          intent.getStringExtra(LoginActivity.STATUS))
              } else {
                  //prod variant

                  AccountUtils.setUserData(accountManager, account, Constants.EXPIRE_IN_TIMESTAMP,
                          intent.getIntExtra(Constants.EXPIRE_IN_TIMESTAMP,
                                  Constants.DEFAULT_EXPIRE_IN_TIME).toString())
              }*/
    }


    override fun afterTexChanged(textInputLayout: TextInputLayout) {
    }

}