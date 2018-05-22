package co.netguru.blueprintlibrary.common.error

import co.netguru.blueprintlibrary.R


enum class GeneralErrors(val title: Int, val message: Int) {
    LOCKED(R.string.user_locked_title, R.string.user_locked_message),
    REMOVED(R.string.user_removed_title, R.string.user_removed_message),
    WAITING_FOR_ACTIVATION(R.string.user_waiting_for_activation_title, R.string.user_waiting_for_activation_message),
    BAD_USER(R.string.bad_user, R.string.bad_user),
    WRONG_PASSWORD(R.string.wrong_password, R.string.wrong_password),
    NO_CONNECTION(R.string.no_connection, R.string.no_connection)
}