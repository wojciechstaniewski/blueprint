package co.netguru.blueprintlibrary.common.view

import android.app.NotificationManager
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.IntentFilter
import android.databinding.DataBindingUtil.setContentView
import android.databinding.ViewDataBinding
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import co.netguru.blueprintlibrary.common.BaseViewModel
import co.netguru.blueprintlibrary.common.events.ActionEvent
import co.netguru.blueprintlibrary.common.permissions.PermissionsActivity
import co.netguru.blueprintlibrary.common.utils.ConnectivityBroadCastReceiver
import co.netguru.blueprintlibrary.common.utils.SnackBarUtils
import co.netguru.blueprintlibrary.common.utils.ValidatorUtils
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


abstract class BaseActivity<T : BaseViewModel, S : ViewDataBinding> constructor(private val layoutResId: Int, private val modelClass: Class<T>? = null) :
        PermissionsActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    lateinit var baseViewModel: T
    lateinit var baseBinding: S
    private lateinit var notificationManager: NotificationManager

    val compositeDisposable = CompositeDisposable()

    private lateinit var connectivityBroadCastReceiver: ConnectivityBroadCastReceiver

    abstract fun setVariables()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseBinding = setContentView(this, layoutResId)


        if (modelClass != null) {
            baseViewModel = ViewModelProviders.of(this).get(modelClass)
        }

        setVariables()

        baseBinding.executePendingBindings()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        compositeDisposable.add(baseViewModel.repository.logoutEvent.getSuccessStream().subscribe {
            baseViewModel.repository.clean()
        })

        compositeDisposable.add(baseViewModel.repository.logoutEvent.getErrorStream().subscribe {
            handleError(it, this.javaClass)
        })

        registerNetworkBroadcastReceiver(baseViewModel.repository.networkConnectivityEvent)
        handleSnackBarOnNetworkConnectivityEvent(baseViewModel.repository.networkConnectivityEvent)
    }

    private fun handleSnackBarOnNetworkConnectivityEvent(networkEvent: ActionEvent<Boolean>) {
        var snackBar: Snackbar? = null
        compositeDisposable.add(
                networkEvent.getSuccessStream().subscribe {
                    if (snackBar != null && snackBar!!.isShown) {
                        snackBar!!.dismiss()
                    }
                })
        compositeDisposable.add(
                networkEvent.getErrorStream().subscribe {
                    snackBar = SnackBarUtils.createSnackBarWithoutRetry(findViewById(android.R.id.content), it.message)
                    snackBar!!.show()
                })
    }

    private fun registerNetworkBroadcastReceiver(networkEvent: ActionEvent<Boolean>) {
        val networkIntentFilter = IntentFilter()
        networkIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        connectivityBroadCastReceiver = ConnectivityBroadCastReceiver(networkEvent)
        registerReceiver(connectivityBroadCastReceiver, networkIntentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityBroadCastReceiver)
        compositeDisposable.dispose()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> =
            fragmentDispatchingAndroidInjector

    fun validateFields(fields: MutableList<TextInputLayout>,
                       listener: ValidatorUtils.InputFieldValidationListener) {
        ValidatorUtils(fields, listener)
    }
}