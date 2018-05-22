package co.netguru.blueprintlibrary.common.view

import android.support.design.widget.TextInputLayout
import co.netguru.blueprintlibrary.common.utils.ErrorUtils
import co.netguru.blueprintlibrary.common.utils.HttpStatus
import retrofit2.HttpException
import javax.inject.Inject


open class ErrorHandlingFragment : BaseFormFragment() {

    @Inject
    lateinit var errorUtils: ErrorUtils

    override fun afterTexChanged(textInputLayout: TextInputLayout) {

    }

    fun handleError(throwable: Throwable, errorIds: List<Int>) {
        if (throwable is HttpException && throwable.code() == HttpStatus.UNAUTHORIZED.value()) {
            handleUnauthorizedError(errorIds)
        } else {
            errorUtils.handleError(throwable, view!!, activity as ErrorHandlingActivity,
                    childFragmentManager)
        }
    }

    private fun handleUnauthorizedError(errorIds: List<Int>) {
        for (fieldId in errorIds) {
            handleError(fields.find { it.id == errorIds[fieldId] })
        }
    }

    private fun handleError(textInputLayout: TextInputLayout?) {
        if (textInputLayout != null) {
            error(textInputLayout)
        }
    }

    fun dismissNoConnectivityError() {
        errorUtils.dismissNoConnectivityError()
    }
}