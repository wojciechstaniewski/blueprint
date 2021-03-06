package co.netguru.blueprintlibrary.common.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import co.netguru.blueprintlibrary.common.Repository
import co.netguru.blueprintlibrary.common.events.ActionEvent
import co.netguru.blueprintlibrary.common.utils.DialogUtils
import co.netguru.blueprintlibrary.common.utils.ErrorUtils
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class ErrorHandlingActivity : AppCompatActivity() {

    val accountCreatedEvent = ActionEvent<Any>()

    companion object {
        const val REQ_SIGNUP = 1
    }

    @Inject
    lateinit var errorUtils: ErrorUtils

    @Inject
    lateinit var dialogUtils: DialogUtils

    val logoutEvent = ActionEvent<Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    fun handleError(throwable: Throwable, activityClass: Class<ErrorHandlingActivity>?) {
        if (this::class.java == activityClass) {
            errorUtils.handleError(throwable, window.decorView.rootView, this, supportFragmentManager)
            logoutEvent.onSuccess(Unit)

        } else {
            logoutEvent.onError(throwable)
            //here finish all activities above MainActivity
            val logoutIntent = Intent(this, activityClass)
            logoutIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(logoutIntent)
        }
    }

    fun dismissNoConnectivityError() {
        errorUtils.dismissNoConnectivityError()
    }
}