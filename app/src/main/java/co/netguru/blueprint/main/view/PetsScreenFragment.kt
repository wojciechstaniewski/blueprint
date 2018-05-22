package co.netguru.blueprint.main.view

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.netguru.blueprint.BR
import co.netguru.blueprint.R
import co.netguru.blueprint.databinding.PetsScreenFragmentBinding
import co.netguru.blueprint.main.viewmodel.MainViewModel
import co.netguru.blueprintlibrary.common.view.BaseFragment
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.Disposable

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
        baseViewModel.getAllPets()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        observeAllPets()
        initItems()
        return baseBinding.root
    }

    private fun initItems() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //baseBinding.items.layoutManager = layoutManager
        //baseBinding.items.adapter = BaseItemsAdapter(baseViewModel.items)
    }

    private fun observeAllPets() {
        baseViewModel.allPets.observe(this, Observer {
            baseViewModel.items.clear()
            //baseBinding.items.adapter.notifyDataSetChanged()
        })
    }


    private fun handleAllPetsEventSucces(): Disposable {
        return baseViewModel.allPetsEvent.getSuccessStream().subscribe({

        })
    }

}