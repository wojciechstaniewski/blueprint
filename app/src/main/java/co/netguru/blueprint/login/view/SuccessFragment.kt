package co.netguru.blueprint.login.view

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import co.netguru.blueprint.BR
import co.netguru.blueprint.R
import co.netguru.blueprint.databinding.SuccessFragmentBinding
import co.netguru.blueprint.login.viewmodel.LoginRegisterViewModel
import co.netguru.blueprintlibrary.common.utils.DisplayUtils
import co.netguru.blueprintlibrary.common.view.BaseFragment
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs

@FragmentWithArgs
class SuccessFragment : BaseFragment<LoginRegisterViewModel, SuccessFragmentBinding>(R.layout.success_fragment) {
    override fun setVariables() {
        baseBinding.setVariable(BR.viewModel, baseViewModel)
        baseBinding.setVariable(BR.layoutUtils, DisplayUtils)
    }

    @Arg
    lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseViewModel = (activity as LoginActivity).baseViewModel
    }

    override fun afterTexChanged(textInputLayout: TextInputLayout) {

    }

}