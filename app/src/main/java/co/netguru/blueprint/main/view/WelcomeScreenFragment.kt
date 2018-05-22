package co.netguru.blueprint.main.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.View
import co.netguru.blueprint.AppNavigation
import co.netguru.blueprint.BR
import co.netguru.blueprint.R
import co.netguru.blueprint.databinding.WelcomeScreenFragmentBinding
import co.netguru.blueprint.main.navigation.MainNavigationHelper
import co.netguru.blueprint.main.viewmodel.MainViewModel
import co.netguru.blueprintlibrary.common.view.BaseFragment
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

@FragmentWithArgs
class WelcomeScreenFragment : BaseFragment<MainViewModel, WelcomeScreenFragmentBinding>(R.layout.welcome_screen_fragment) {

    @Inject
    lateinit var mainNavigationHelper: MainNavigationHelper

    override fun setVariables() {
        baseBinding.setVariable(BR.viewModel, baseViewModel)
    }

    @Arg
    var title: String? = null

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

        compositeDisposable.add(baseViewModel.nextEvent.getStartProgressEvent().subscribe {
            baseBinding.letsStart.startAnimation()
            baseViewModel.waitUntilAnimationFinish()
        })

        compositeDisposable.add(baseViewModel.nextEvent.getSuccessStream().subscribe({
            baseBinding.letsStart.stopAnimation()
            mainNavigationHelper.navigate(AppNavigation.PETS)
        }))

        compositeDisposable.add(baseViewModel.nextEvent.getErrorStream().subscribe({
            //the progress event not call async service so no error should be catch
        }))
    }

    override fun afterTexChanged(textInputLayout: TextInputLayout) {

    }

}