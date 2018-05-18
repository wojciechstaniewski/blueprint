package co.netguru.blueprint.login.view

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.View
import co.netguru.blueprint.BR
import co.netguru.blueprint.R
import co.netguru.blueprint.databinding.SignInRegisterFragmentBinding
import co.netguru.blueprint.login.viewmodel.LoginRegisterViewModel
import co.netguru.blueprintlibrary.common.utils.DisplayUtils
import co.netguru.blueprintlibrary.common.view.BaseFragment
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs


@FragmentWithArgs
class SignInAndRegisterFragment : BaseFragment<LoginRegisterViewModel, SignInRegisterFragmentBinding>(R.layout.sign_in_register_fragment) {

    override fun setVariables() {
        baseBinding.setVariable(BR.viewModel, baseViewModel)
        baseBinding.setVariable(BR.layoutUtils, DisplayUtils)
    }

    @Arg
    lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseViewModel = (activity as LoginActivity).baseBinding.viewModel!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fields.clear()
        /*       fields.add(baseBinding.inputEmail)
               fields.add(baseBinding.inputPassword)
               fields.add(baseBinding.reInputPassword)
               submitButton = baseBinding.register*/
        setItemsToValidation()

        handleStartRegisterEvent()
        handleSuccessRegisterEvent()
        handleErrorRegisterEvent()
        handleSuccessPasswordStrengthEvent()
        handleErrorPasswordStrengthEvent()
    }

    private fun handleStartRegisterEvent() {
/*        baseViewModel.registerEvent.getStartProgressEvent().subscribe {
            dismissNoConnectivityError()
            baseBinding.register.startAnimation()
            Handler().postDelayed({
                baseViewModel.register()
            }, CircularProgressButton.ANIMATION_LENGTH)
        }*/
    }

    private fun handleErrorRegisterEvent() {
/*        baseViewModel.registerEvent.getErrorStream().subscribe({
            baseBinding.register.stopAnimation()
            baseBinding.register.revertAnimation()
            handleError(it)
        })*/
    }

    private fun handleSuccessRegisterEvent() {
        /*       baseViewModel.registerEvent.getSuccessStream().subscribe({
                   if (isAdded) {
                       baseBinding.register.stopAnimation()
                       baseViewModel.successText = String.format(getString(R.string.register_success, baseViewModel.registerUserName.get()!!.trim()))
                       baseViewModel.registerUserName.set("")
                       baseViewModel.registerPassword.set("")
                       baseViewModel.registerRePassword.set("")
                       baseViewModel.navigateToSuccessScreen()
                   }
               })*/
    }

    private fun handleSuccessPasswordStrengthEvent() {
        /*     baseViewModel.passwordStrengthEvent.getSuccessStream().subscribe({
                 baseBinding.registerPasswordStrength.progress = it
                 val context = baseBinding.registerPasswordStrength.context
                 baseBinding.registerPasswordStrength.progressDrawable.setColorFilter(
                         when (it) {
                             1 -> ContextCompat.getColor(context, R.color.weak_password)
                             2 -> ContextCompat.getColor(context, R.color.medium_password)
                             else -> ContextCompat.getColor(context, R.color.strong_password)
                         }, PorterDuff.Mode.SRC_IN)
             })*/
    }

    private fun handleErrorPasswordStrengthEvent() {
        // TODO Wojtek: OnErrorNotImplementedException (crash aplikacji)
//        baseViewModel.passwordStrengthEvent.getErrorStream().subscribe({
//            handleError(it)
//        })
    }

    override fun afterTexChanged(textInputLayout: TextInputLayout) {
/*        if (textInputLayout == baseBinding.inputPassword) {
            if (textInputLayout.editText!!.text.isNotEmpty()) {
                baseBinding.registerPasswordStrength.visibility = View.VISIBLE
                baseViewModel.passwordStrength(baseBinding.inputPasswordText.text.toString().trim())
            } else {
                baseBinding.registerPasswordStrength.visibility = View.GONE
            }
        }*/
    }
}