package co.netguru.blueprint.login.view

import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.View
import co.netguru.blueprint.BR
import co.netguru.blueprint.BuildConfig
import co.netguru.blueprint.R
import co.netguru.blueprint.databinding.SplashScreenFragmentBinding
import co.netguru.blueprint.main.view.MainActivity
import co.netguru.blueprint.main.viewmodel.MainViewModel
import co.netguru.blueprintlibrary.common.GetAuthTokenCallback
import co.netguru.blueprintlibrary.common.utils.AccountUtils
import co.netguru.blueprintlibrary.common.view.BaseFragment
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

@FragmentWithArgs
class SplashScreenFragment : BaseFragment<MainViewModel, SplashScreenFragmentBinding>(R.layout.splash_screen_fragment) {
    override fun setVariables() {
        baseBinding.setVariable(BR.viewModel, baseViewModel)
    }

    @Arg
    lateinit var title: String

    @Inject
    lateinit var accountManager: AccountManager

    @Inject
    lateinit var accountUtils: AccountUtils

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

        accountManager.getAuthTokenByFeatures(BuildConfig.APPLICATION_ID,
                BuildConfig.AUTH_TOKEN_TYPE,
                null,
                activity as MainActivity,
                null,
                null,
                GetAuthTokenCallback(activity as MainActivity, BuildConfig.APPLICATION_ID, BuildConfig.AUTH_TOKEN_TYPE, accountUtils, accountManager), null)
    }

    override fun afterTexChanged(textInputLayout: TextInputLayout) {

    }
}