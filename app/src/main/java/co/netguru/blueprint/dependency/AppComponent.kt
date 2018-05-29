package co.netguru.blueprint.dependency

import android.app.Application
import co.netguru.blueprint.App
import co.netguru.blueprintlibrary.dependency.AccountType
import co.netguru.blueprintlibrary.dependency.AuthTokenType
import co.netguru.blueprintlibrary.dependency.BaseUrl
import co.netguru.blueprintlibrary.dependency.modules.AppModule
import co.netguru.blueprintlibrary.dependency.modules.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        AppModule::class,
        NetworkModule::class,
        AuthenticationModule::class,
        PetModule::class,
        StoreModule::class,
        UserModule::class,
        ActivityBuilder::class,
        ServiceBuilder::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun baseUrl(@BaseUrl baseUrl: String): Builder

        @BindsInstance
        fun accountType(@AccountType accountType: String): Builder

        @BindsInstance
        fun authTokenType(@AuthTokenType authTokenType: String): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}