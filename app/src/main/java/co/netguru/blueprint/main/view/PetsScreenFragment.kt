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
import co.netguru.blueprint.main.dependency.MainViewModelSubComponent
import co.netguru.blueprint.main.viewmodel.MainViewModel
import co.netguru.blueprint.main.viewmodel.PetViewModel
import co.netguru.blueprint.services.pet.domain.Pet
import co.netguru.blueprint.services.pet.domain.Status
import co.netguru.blueprintlibrary.common.adapters.BaseItemsAdapter
import co.netguru.blueprintlibrary.common.adapters.LayoutItemType
import co.netguru.blueprintlibrary.common.view.BaseFragment
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Provider

@FragmentWithArgs
class PetsScreenFragment : BaseFragment<MainViewModel, PetsScreenFragmentBinding>(R.layout.pets_screen_fragment) {

    @Inject
    lateinit var mainViewModelComponent: Provider<MainViewModelSubComponent.Builder>

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
        baseViewModel.getPets(Status.PENDING)

        compositeDisposable.add(handlePetsEventError())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        observePets()
        initItems()
        return baseBinding.root
    }

    private fun initItems() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        baseBinding.items.layoutManager = layoutManager
        baseBinding.items.adapter = BaseItemsAdapter(baseViewModel.items, { layoutItemAdapter -> (layoutItemAdapter) }, BR.viewModel)
    }

    private fun observePets() {
        baseViewModel.pets.observe(this, Observer {
            for (pet: Pet in it!!) {
                val petViewModel = PetViewModel(pet)
                baseViewModel.items.add(petViewModel)
                mainViewModelComponent.get().build().inject(petViewModel)
            }
            baseBinding.items.adapter.notifyDataSetChanged()
        })
    }


    private fun handlePetsEventError(): Disposable {
        return baseViewModel.petsEvent.getErrorStream().subscribe {
            handleError(it, null)
        }
    }

}