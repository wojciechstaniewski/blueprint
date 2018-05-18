package co.netguru.blueprint.main.dependency

import co.netguru.blueprint.main.viewmodel.MainViewModel
import dagger.Subcomponent

@Subcomponent
interface MainViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): MainViewModelSubComponent
    }

    fun inject(mainViewModel: MainViewModel)
}