package co.netguru.blueprint.login.view

import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.View
import co.netguru.blueprint.BR
import co.netguru.blueprint.BuildConfig
import co.netguru.blueprint.R
import co.netguru.blueprint.authenticator.AccountAuthenticator
import co.netguru.blueprint.databinding.SplashScreenFragmentBinding
import co.netguru.blueprint.main.view.MainActivity
import co.netguru.blueprint.main.viewmodel.MainViewModel
import co.netguru.blueprintlibrary.common.Constants
import co.netguru.blueprintlibrary.common.GetAuthTokenCallback
import co.netguru.blueprintlibrary.common.view.BaseFragment
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import dagger.android.support.AndroidSupportInjection

@FragmentWithArgs
class SplashScreenFragment : BaseFragment<MainViewModel, SplashScreenFragmentBinding>(R.layout.splash_screen_fragment) {
    override fun setVariables() {
        baseBinding.setVariable(BR.viewModel, baseViewModel)
    }

    @Arg
    lateinit var title: String

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseViewModel = (activity as MainActivity).baseViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val accountManager = AccountManager.get(context)

        Thread.sleep(Constants.ONE_SECOND)

        accountManager.getAuthTokenByFeatures(AccountAuthenticator.getAccountType(),
                AccountAuthenticator.getAuthTokenType(),
                null,
                activity as MainActivity,
                null,
                null,
                GetAuthTokenCallback(activity as MainActivity, BuildConfig.APP_ACCOUNT_NAME, BuildConfig.AUTH_TOKEN_TYPE), null)
    }

    override fun afterTexChanged(textInputLayout: TextInputLayout) {

    }
}