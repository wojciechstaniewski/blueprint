package co.netguru.blueprintlibrary.common.utils

import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.view.View
import co.netguru.blueprintlibrary.common.Constants
import co.netguru.blueprintlibrary.common.dialogs.ErrorDialogParams
import co.netguru.blueprintlibrary.common.exception.NoConnectivityException
import co.netguru.blueprintlibrary.common.view.ErrorHandlingActivity
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.security.auth.login.LoginException


class ErrorUtils @Inject constructor(private val accountUtils: AccountUtils,
                                     private val dialogUtils: DialogUtils,
                                     private val accountType: String) {

    private var snackBar: Snackbar? = null

    fun handleError(throwable: Throwable, view: View, activity: ErrorHandlingActivity,
                    fragmentManager: FragmentManager) {
        when (throwable) {
            is NoConnectivityException -> {
                snackBar = SnackBarUtils.createSnackBarWithoutRetry(view, throwable.message)
                snackBar!!.show()
            }
            is LoginException -> {
                //todo handle on view error in login
            }
            is HttpException -> {
                handleHttpException(throwable, activity, fragmentManager)
            }
            is SocketTimeoutException -> {
                showGeneralDialogFragment(throwable, activity, fragmentManager)
            }
        }
    }

    private fun handleHttpException(exception: HttpException,
                                    activity: ErrorHandlingActivity,
                                    fragmentManager: FragmentManager) {
        when {
            exception.code() == Constants.SESSION_EXPIRED_ERROR_CODE ||
                    exception.code() == HttpStatus.UNAUTHORIZED.value()
            -> {
                accountUtils.logoutFromAccountManager(activity)
                accountUtils.removeAccount(accountType)
            }
            else -> showGeneralDialogFragment(exception, activity, fragmentManager)
        }
    }

    private fun showGeneralDialogFragment(throwable: Throwable, activity: ErrorHandlingActivity, fragmentManager: FragmentManager) {
        val errorDialogMessages = ErrorDialogParams.ERROR
        errorDialogMessages.message = throwable.message
        dialogUtils.showDialog(errorDialogMessages, fragmentManager)
    }

    fun dismissNoConnectivityError() {
        if (snackBar != null && snackBar!!.isShown) {
            snackBar!!.dismiss()
        }
    }

}