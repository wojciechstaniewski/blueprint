package co.netguru.blueprint.main.dependency

import co.netguru.blueprint.main.viewmodel.MainViewModel
import co.netguru.blueprint.main.viewmodel.PetViewModel
import dagger.Subcomponent

@Subcomponent
interface MainViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): MainViewModelSubComponent
    }

    fun inject(mainViewModel: MainViewModel)

    fun inject(petViewModel: PetViewModel)
}