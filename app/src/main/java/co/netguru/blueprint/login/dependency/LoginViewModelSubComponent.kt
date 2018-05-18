package co.netguru.blueprint.login.dependency

import co.netguru.blueprint.login.viewmodel.LoginRegisterViewModel
import dagger.Subcomponent

@Subcomponent
interface LoginViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): LoginViewModelSubComponent
    }

    fun inject(loginRegisterViewModel: LoginRegisterViewModel)
}
