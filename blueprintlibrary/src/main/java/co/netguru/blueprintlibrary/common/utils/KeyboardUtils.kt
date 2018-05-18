package co.netguru.blueprintlibrary.common.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager


object KeyboardUtils {

    fun hideKeyboard(activity: Activity) {
        val inputMethodManager = getInputMethodManager(activity)
        if (isTheKeyboardVisible(inputMethodManager)) {
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
        }
    }

    fun isTheKeyboardVisible(inputMethodManager: InputMethodManager) =
            inputMethodManager.isAcceptingText

    fun getInputMethodManager(activity: Activity) =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

}