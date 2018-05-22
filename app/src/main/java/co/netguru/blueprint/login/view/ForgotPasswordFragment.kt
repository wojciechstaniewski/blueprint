package co.netguru.blueprint.login.view

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TextInputLayout
import android.view.View
import co.netguru.blueprint.BR
import co.netguru.blueprint.R
import co.netguru.blueprint.databinding.ForgotPasswordFragmentBinding
import co.netguru.blueprint.login.viewmodel.LoginRegisterViewModel
import co.netguru.blueprintlibrary.common.customViews.CircularProgressButton
import co.netguru.blueprintlibrary.common.utils.DisplayUtils
import co.netguru.blueprintlibrary.common.view.BaseFragment
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs

@FragmentWithArgs
class ForgotPasswordFragment : BaseFragment<LoginRegisterViewModel,
        ForgotPasswordFragmentBinding>(R.layout.forgot_password_fragment) {

    override fun setVariables() {
        baseBinding.setVariable(BR.viewModel, baseViewModel)
        baseBinding.setVariable(BR.layoutUtils, DisplayUtils)
    }

    @Arg
    lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseViewModel = (activity as LoginActivity).baseViewModel

        handleStartPasswordResetEvent()
        handleSuccessPasswordResetEvent()
        handleErrorPasswordResetEvent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fields.clear()
        fields.add(baseBinding.inputEmail)
        submitButton = baseBinding.sentResetLink
        setItemsToValidation()
    }

    private fun handleStartPasswordResetEvent() {
        compositeDisposable.add(baseViewModel.passwordResetEvent.getStartProgressEvent().subscribe {
            dismissNoConnectivityError()
            baseBinding.sentResetLink.startAnimation()
            Handler().postDelayed({
                baseViewModel.reset()
            }, CircularProgressButton.ANIMATION_LENGTH)
        })
    }

    private fun handleErrorPasswordResetEvent() {
        compositeDisposable.add(baseViewModel.passwordResetEvent.getErrorStream().subscribe({
            baseBinding.sentResetLink.stopAnimation()
            baseBinding.sentResetLink.revertAnimation()
            handleError(it, listOf(baseBinding.inputEmail.id))
        }))
    }

    private fun handleSuccessPasswordResetEvent() {
        compositeDisposable.add(baseViewModel.passwordResetEvent.getSuccessStream().subscribe({
            baseBinding.sentResetLink.stopAnimation()
            baseViewModel.successText = String.format(getString(R.string.reset_success,
                    baseViewModel.forgotUserName.get()!!.trim()))
            baseViewModel.forgotUserName.set("")
            baseViewModel.navigateToSuccessScreen()
        }))
    }

    override fun afterTexChanged(textInputLayout: TextInputLayout) {

    }
}