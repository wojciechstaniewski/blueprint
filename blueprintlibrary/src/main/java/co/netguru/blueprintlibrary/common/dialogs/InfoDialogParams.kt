package co.netguru.blueprintlibrary.common.dialogs

import co.netguru.blueprintlibrary.R
import java.io.Serializable

enum class InfoDialogParams(var titleId: Int,
                            var messageId: Int,
                            var iconResId: Int = R.drawable.ic_info,
                            var positiveButtonTextId: Int = R.string.ok) : Serializable {

}