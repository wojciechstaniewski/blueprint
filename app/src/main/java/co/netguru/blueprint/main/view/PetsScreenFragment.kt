package co.netguru.blueprint.main.view

import android.content.Context
import android.os.Bundle
import co.netguru.blueprint.BR
import co.netguru.blueprint.R
import co.netguru.blueprint.databinding.PetsScreenFragmentBinding
import co.netguru.blueprint.main.viewmodel.MainViewModel
import co.netguru.blueprintlibrary.common.view.BaseFragment
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import dagger.android.support.AndroidSupportInjection

@FragmentWithArgs
class PetsScreenFragment : BaseFragment<MainViewModel, PetsScreenFragmentBinding>(R.layout.pets_screen_fragment) {
    @Arg
    var title: String? = null

    override fun setVariables() {
        baseBinding.setVariable(BR.viewModel, baseViewModel)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseViewModel = (activity as MainActivity).baseViewModel
    }

}