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
import co.netguru.blueprint.BuildConfig
import co.netguru.blueprint.R
import co.netguru.blueprint.authenticator.AuthenticationResult
import co.netguru.blueprint.databinding.LoginFragmentBinding
import co.netguru.blueprint.login.viewmodel.LoginRegisterViewModel
import co.netguru.blueprintlibrary.common.Constants
import co.netguru.blueprintlibrary.common.customViews.CircularProgressButton
import co.netguru.blueprintlibrary.common.utils.AccountUtils
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

    @Inject
    lateinit var accountUtils: AccountUtils

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
        compositeDisposable.add(baseViewModel.loginEvent.getSuccessStream().subscribe {
            baseBinding.login.stopAnimation()
            finishLogin(it)
        })
    }

    private fun handleErrorLoginEvent() {
        compositeDisposable.add(baseViewModel.loginEvent.getErrorStream().subscribe {
            baseBinding.login.stopAnimation()
            baseBinding.login.revertAnimation()
            handleError(it, listOf(baseBinding.inputEmail.id, baseBinding.inputPassword.id))
        })
    }

    private fun finishLogin(authenticationResult: AuthenticationResult) {

        val accountPassword = authenticationResult.password
        val account = Account(authenticationResult.accountName, authenticationResult.accountType)

        if (activity!!.intent.getBooleanExtra(getString(R.string.isAddingNewAccount), false)) {
            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            accountManager.addAccountExplicitly(account, accountPassword, null)
            accountManager.setAuthToken(account, BuildConfig.AUTH_TOKEN_TYPE, authenticationResult.authToken)
        } else {
            accountManager.setPassword(account, accountPassword)
        }

        val intent = Intent()
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, authenticationResult.accountName)
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, authenticationResult.accountType)
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, authenticationResult.authToken)
        intent.putExtra(getString(R.string.password), authenticationResult.password)


        accountUtils.setUserData(account, Constants.EXPIRE_IN_TIMESTAMP,
                authenticationResult.expireIn)

        (activity as LoginActivity).setAccountAuthenticatorResult(intent.extras)
        (activity as LoginActivity).setResult(AccountAuthenticatorActivity.RESULT_OK, intent)
        (activity as LoginActivity).finish()
    }

    override fun afterTexChanged(textInputLayout: TextInputLayout) {
    }

}