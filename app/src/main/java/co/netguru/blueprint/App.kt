package co.netguru.blueprint

import android.app.Activity
import android.app.Application
import android.app.Service
import co.netguru.blueprint.dependency.AppComponent
import co.netguru.blueprint.dependency.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject


class App : Application(), HasActivityInjector, HasServiceInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var serviceDispatchingAndroidInjector: DispatchingAndroidInjector<Service>


    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return activityDispatchingAndroidInjector
    }

    override fun serviceInjector(): AndroidInjector<Service> {
        return serviceDispatchingAndroidInjector
    }

    val component: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .baseUrl(BuildConfig.SERVER)
                .accountType(BuildConfig.APP_ACCOUNT_NAME)
                .authTokenType(BuildConfig.AUTH_TOKEN_TYPE)
                .application(this)
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.ENABLE_LEAK_CANARY) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not setUser your app in this process.
                return
            }
            LeakCanary.install(this)
        }
        component.inject(this)
        Config.init(this)
    }
}