package co.netguru.blueprintlibrary.common.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import co.netguru.blueprintlibrary.R
import co.netguru.blueprintlibrary.common.events.ActionEvent
import co.netguru.blueprintlibrary.common.exception.NoConnectivityException

class ConnectivityBroadCastReceiver(private val networkEvent: ActionEvent<Boolean>) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val noConnectivity = intent!!.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
        if (noConnectivity) {
            networkEvent.onError(NoConnectivityException(context!!.getString(R.string.no_connection)))
        } else {
            networkEvent.onSuccess(true)
        }
    }
}