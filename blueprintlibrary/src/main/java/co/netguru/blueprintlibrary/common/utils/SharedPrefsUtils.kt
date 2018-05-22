package co.netguru.blueprintlibrary.common.utils

import android.content.SharedPreferences
import co.netguru.blueprintlibrary.common.Constants
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SharedPrefsUtils @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun setFirstRunning(firstRunning: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(Constants.FIRST_RUNNING, firstRunning)
        editor.apply()
    }

    fun isFirstRunning(): Boolean {
        return sharedPreferences.getBoolean(Constants.FIRST_RUNNING, true)
    }

    fun shouldOpenLinksInInternalBrowser(): Boolean {
        return sharedPreferences.getBoolean(Constants.OPEN_LINKS_IN_INTERNAL_BROWSER, false)
    }

    fun userAllowsPushes(): Boolean {
        return sharedPreferences.getBoolean(Constants.USER_ALLOWS_PUSHES, false)
    }

}