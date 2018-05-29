package co.netguru.blueprintlibrary.common.dialogs

import co.netguru.blueprintlibrary.R
import java.io.Serializable

enum class ConfirmationDialogParams(var titleId: Int,
                                    var messageId: Int,
                                    var messageParams: Array<String>? = null,
                                    var iconResId: Int = R.drawable.ic_help,
                                    var positiveButtonResId: Int = R.string.button_yes,
                                    var negativeButtonResId: Int = R.string.button_no) : Serializable {

    LOGOUT(R.string.logout_confirmation_title,R.string.logout_confirmation_message)

}