package co.netguru.blueprintlibrary.common

import co.netguru.blueprintlibrary.common.events.ActionEvent

class Repository {
    val logoutEvent = ActionEvent<Unit>()
    val networkConnectivityEvent = ActionEvent<Boolean>()

    fun clean() {

    }
}