package co.netguru.blueprintlibrary.common.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import co.netguru.blueprintlibrary.R
import co.netguru.blueprintlibrary.common.Constants
import co.netguru.blueprintlibrary.common.events.ActionEvent
import java.io.Serializable

class GeneralDialogFragment : AppCompatDialogFragment() {

    val buttonClicked = ActionEvent<Unit>()

    companion object {
        val tag = GeneralDialogFragment::class.java.simpleName!!

        fun newInstance(dialogParams: Serializable): GeneralDialogFragment {
            val fragment = GeneralDialogFragment()
            val args = Bundle()
            args.putSerializable(Constants.DIALOG_PARAMS, dialogParams)
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialogBuilder = AlertDialog.Builder(this.activity!!, R.style.AppCompatAlertDialogStyle)
        val dialogParams = arguments?.getSerializable(Constants.DIALOG_PARAMS) as Serializable
        return when (dialogParams) {
            is InfoDialogParams -> getInfoDialog(alertDialogBuilder, dialogParams)
            is ConfirmationDialogParams -> getConfirmationDialog(alertDialogBuilder, dialogParams)
            else -> getErrorDialog(alertDialogBuilder, dialogParams as ErrorDialogParams)
        }
    }

    override fun onCancel(dialogInterface: DialogInterface?) {
        super.onCancel(dialogInterface)
        buttonClicked.onError(Throwable())
        dialogInterface?.dismiss()
    }

    private fun getInfoDialog(alertDialogBuilder: AlertDialog.Builder,
                              infoDialogParams: InfoDialogParams): Dialog {
        alertDialogBuilder.setTitle(getString(infoDialogParams.titleId))
        alertDialogBuilder.setMessage(getString(infoDialogParams.messageId))
        alertDialogBuilder.setIcon(infoDialogParams.iconResId)
        setPositiveButton(alertDialogBuilder, infoDialogParams.positiveButtonTextId)
        return alertDialogBuilder.create()
    }

    private fun getConfirmationDialog(alertDialogBuilder: AlertDialog.Builder,
                                      confirmationDialogParams: ConfirmationDialogParams): Dialog {
        alertDialogBuilder.setTitle(getString(confirmationDialogParams.titleId))
        setMessage(confirmationDialogParams.messageParams,
                confirmationDialogParams.messageId, alertDialogBuilder)
        alertDialogBuilder.setIcon(confirmationDialogParams.iconResId)
        setPositiveButton(alertDialogBuilder, confirmationDialogParams.positiveButtonResId)
        setNegativeButton(alertDialogBuilder, confirmationDialogParams.negativeButtonResId)
        return alertDialogBuilder.create()
    }

    private fun getErrorDialog(alertDialogBuilder: AlertDialog.Builder,
                               errorDialogParams: ErrorDialogParams): Dialog {
        alertDialogBuilder.setTitle(
                getString(errorDialogParams.titleId ?: ErrorDialogParams.ERROR.titleId!!))
        setMessage(errorDialogParams.messageId, errorDialogParams.message, alertDialogBuilder)
        alertDialogBuilder.setIcon(errorDialogParams.iconResId)
        setPositiveButton(alertDialogBuilder, errorDialogParams.positiveButtonTextId)
        if (errorDialogParams.negativeButtonTextId != null) {
            setNegativeButton(alertDialogBuilder, errorDialogParams.negativeButtonTextId!!)
        }
        return alertDialogBuilder.create()
    }

    private fun setMessage(messageId: Int?, message: String?,
                           alertDialogBuilder: AlertDialog.Builder) {
        when {
            message != null -> alertDialogBuilder.setMessage(message)
            messageId != null -> alertDialogBuilder.setMessage(getString(messageId))
        }
    }

    private fun setMessage(messageParams: Array<String>? = null, messageId: Int,
                           alertDialogBuilder: AlertDialog.Builder) {
        when {
            messageParams?.isEmpty() == false -> {
                val format = StringBuilder("%s ")
                for (i in 0 until messageParams.size) {
                    format.append("%s ")
                }
                alertDialogBuilder.setMessage(
                        String.format(format.toString(), getString(messageId), messageParams))
            }
            else -> alertDialogBuilder.setMessage(getString(messageId))
        }
    }

    private fun setPositiveButton(alertDialogBuilder: AlertDialog.Builder,
                                  positiveButtonTextId: Int) {
        alertDialogBuilder.setPositiveButton(activity?.getString(positiveButtonTextId),
                { dialog, _ ->
                    buttonClicked.onSuccess(Unit)
                    dialog.dismiss()
                })
    }

    private fun setNegativeButton(alertDialogBuilder: AlertDialog.Builder,
                                  negativeButtonTextId: Int) {
        alertDialogBuilder.setNegativeButton(activity?.getString(negativeButtonTextId),
                { dialog, _ ->
                    buttonClicked.onError(Throwable())
                    dialog.dismiss()
                })
    }

}