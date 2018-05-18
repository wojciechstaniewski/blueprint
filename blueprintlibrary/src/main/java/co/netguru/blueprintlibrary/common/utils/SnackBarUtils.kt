package co.netguru.blueprintlibrary.common.utils

import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import co.netguru.blueprintlibrary.R

object SnackBarUtils {

    fun createSnackBarWithoutRetry(view: View?, message: String?): Snackbar {
        val snackBar = Snackbar.make(view!!, message!!, Snackbar.LENGTH_INDEFINITE)
        val sbView = snackBar.view
        val textView = sbView.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(view.context, R.color.notificationError))
        return snackBar
    }

    fun createSnackBarWithRetry(view: View?, message: String?, listener: View.OnClickListener): Snackbar {
        val snackBar = createSnackBarWithoutRetry(view, message)
        snackBar.setAction(view!!.context.getString(R.string.retry), listener)
        snackBar.setActionTextColor(ContextCompat.getColor(view.context, R.color.white))
        return snackBar
    }
}