package co.netguru.blueprintlibrary.common.dialogs

import co.netguru.blueprintlibrary.R
import java.io.Serializable

enum class ErrorDialogParams(var titleId: Int? = null,
                             var messageId: Int? = null,
                             var message: String? = null,
                             var iconResId: Int = R.drawable.ic_error,
                             var positiveButtonTextId: Int = R.string.ok,
                             var negativeButtonTextId: Int? = null) : Serializable {
    ERROR(R.string.error_title),
    GENERAL_ERROR()
}